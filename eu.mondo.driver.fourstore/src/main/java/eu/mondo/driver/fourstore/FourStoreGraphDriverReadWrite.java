/*******************************************************************************
 * Copyright (c) 2010-2015, Benedek Izso, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Benedek Izso - initial API and implementation
 *   Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package eu.mondo.driver.fourstore;

import static eu.mondo.driver.graph.util.RDFUtil.brackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverWrite;
import eu.mondo.utils.UnixUtils;

public class FourStoreGraphDriverReadWrite extends FourStoreGraphDriverRead implements RDFGraphDriverWrite {

	private static final int PARTITION_SIZE = 500;

	public FourStoreGraphDriverReadWrite(String connectionString) {
		super(connectionString);
	}

	// multiple

	// C

	@Override
	public void insertVertices(List<String> uris, String type) throws IOException {
		if (uris.isEmpty()) {
			return;
		}

		List<List<String>> partitions = Lists.partition(uris, PARTITION_SIZE);
		for (List<String> partition : partitions) {
			insertVerticesBlock(partition, type);
		}
	}

	private void insertVerticesBlock(Collection<String> uris, String type) throws IOException {
		final StringBuilder insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		insertQueryBuilder.append("INSERT DATA {");
		for (final String uri : uris) {
			insertQueryBuilder.append(String.format(". %s rdf:type %s", brackets(uri), brackets(type)));
		}
		insertQueryBuilder.append("}");

		// run the update
		runUpdate(insertQueryBuilder.toString());
	}

	public void insertEdges(final Multimap<String, String> edges, final String type) throws IOException {
		if (edges.isEmpty()) {
			return;
		}

		final ArrayList<String> sourceVertices = new ArrayList<>(edges.keySet());		
		final List<List<String>> sourceVerticesPartitions = Lists.partition(sourceVertices, PARTITION_SIZE);
		for (final List<String> sourceVerticesPartition : sourceVerticesPartitions) {
						
			final Multimap<String, String> edgeBlock = ArrayListMultimap.create();
			for (String sourceVertexURI : sourceVerticesPartition) {
				Collection<String> targetVertexURIs = edges.get(sourceVertexURI);
				edgeBlock.putAll(sourceVertexURI, targetVertexURIs);
			}
			
			insertEdgesBlock(edgeBlock, type);
		}
		
	}

	private void insertEdgesBlock(final Multimap<String, String> edges, final String type) throws IOException {
		final StringBuilder insertQueryBuilder = new StringBuilder("INSERT DATA {");
		edgesToTriples(edges, type, insertQueryBuilder);
		insertQueryBuilder.append("}");

		// run the update
		runUpdate(insertQueryBuilder.toString());
	}

	@Override
	public void insertEdgesWithVertex(Multimap<String, String> edges, String edgeType, String targetVertexType) throws IOException {
		if (edges.isEmpty()) {
			return;
		}

		// TODO
		insertEdgesWithVertexBlock(edges, edgeType, targetVertexType);
	}

	private void insertEdgesWithVertexBlock(Multimap<String, String> edges, String edgeType, String targetVertexType) throws IOException {
		final StringBuilder insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		insertQueryBuilder.append("INSERT DATA {");
		edgesToTriples(edges, edgeType, insertQueryBuilder);
		for (final String targetVertex : edges.values()) {
			insertQueryBuilder.append(String.format(". %s rdf:type %s", brackets(targetVertex), brackets(targetVertexType)));
		}
		insertQueryBuilder.append("}");

		// run the update
		runUpdate(insertQueryBuilder.toString());
	}

	// U

	@Override
	public void updateProperties(Map<String, Object> properties, String type) throws IOException {
		if (properties.isEmpty()) {
			return;
		}

		// TODO
		updatePropertiesBlock(properties, type);
	}

	private void updatePropertiesBlock(Map<String, Object> properties, String type) throws IOException {
		final StringBuilder updateQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		int i = 0;

		// delete
		for (final Entry<String, Object> property : properties.entrySet()) {
			final String vertex = property.getKey();

			i++;
			updateQueryBuilder.append(String.format("DELETE { %s %s ?a%d } WHERE { %s %s ?a%d }; ", brackets(vertex), brackets(type), i,
					brackets(vertex), brackets(type), i));
		}

		// insert
		boolean first = true;
		updateQueryBuilder.append("INSERT DATA {");
		for (final Entry<String, Object> property : properties.entrySet()) {
			if (first) {
				first = false;
			} else {
				updateQueryBuilder.append(".");
			}
			final String vertex = property.getKey();
			final String value = property.getValue().toString();
			updateQueryBuilder.append(String.format(" %s %s %s ", brackets(vertex), brackets(type), brackets(value)));
		}
		updateQueryBuilder.append("}");

		// run the update
		runUpdate(updateQueryBuilder.toString());
	}

	// D

	@Override
	public void deleteVertices(final List<String> uris) throws IOException {
		if (uris.isEmpty()) {
			return;
		}

		List<List<String>> partitions = Lists.partition(uris, PARTITION_SIZE);
		for (List<String> partition : partitions) {
			deleteVertexBlock(partition);
		}
	}

	private void deleteVertexBlock(final List<String> vertices) throws IOException {
		final StringBuilder deleteQueryBuilder = new StringBuilder();

		// add a number to each variable number in the SPARQL query in order to make it unique
		long i = 0;
		for (final String vertex : vertices) {
			// if we try to use DELETE DATA (as in the deleteEdge() method), 4store throws an error:
			// DELETE WHERE { x } not yet supported, use DELETE { x } WHERE { x }
			i++;
			// delete "incoming edges"
			deleteQueryBuilder.append(String.format("DELETE { ?a%d ?b%d %s } WHERE { ?a%d ?b%d %s }; ", i, i, brackets(vertex), i, i,
					brackets(vertex)));
			i++;
			// delete "outgoing edges" and "properties"
			deleteQueryBuilder.append(String.format("DELETE { %s ?a%d ?b%d } WHERE { %s ?a%d ?b%d }; ", brackets(vertex), i, i,
					brackets(vertex), i, i));
		}

		runUpdate(deleteQueryBuilder.toString());
	}

	@Override
	public void deleteEdges(Multimap<String, String> edges, String type) throws IOException {
		if (edges.isEmpty()) {
			return;
		}

		final StringBuilder deleteQueryBuilder = new StringBuilder("DELETE DATA {");
		edgesToTriples(edges, type, deleteQueryBuilder);
		deleteQueryBuilder.append("}");
		runUpdate(deleteQueryBuilder.toString());
	}

	// single elements

	// C

	@Override
	public void insertVertex(final String uri, final String type) throws IOException {
		List<String> uris = new ArrayList<>();
		uris.add(uri);
		insertVertices(uris, type);
	}

	@Override
	public void insertEdge(final String sourceVertexURI, final String targetVertexURI, final String type) throws IOException {
		Multimap<String, String> edges = HashMultimap.create();
		edges.put(sourceVertexURI, targetVertexURI);
		insertEdges(edges, type);
	}

	@Override
	public void insertEdgeWithVertex(String sourceURI, String targetURI, String edgeType, String targetVertexType) throws IOException {
		Multimap<String, String> edges = HashMultimap.create();
		edges.put(sourceURI, targetURI);
		insertEdgesWithVertex(edges, edgeType, targetVertexType);
	}

	// U

	@Override
	public void updateProperty(final String vertex, final String type, final Object value) throws IOException {
		Map<String, Object> properties = new HashMap<>();
		properties.put(vertex, value);
		updateProperties(properties, type);
	}

	// D

	@Override
	public void deleteVertex(final String uri) throws IOException {
		List<String> vertices = new ArrayList<>();
		vertices.add(uri);
		deleteVertices(vertices);
	}

	@Override
	public void deleteEdge(final String sourceVertexURI, final String targetVertexURI, final String type) throws IOException {
		Multimap<String, String> edges = HashMultimap.create();
		edges.put(sourceVertexURI, targetVertexURI);
		deleteEdges(edges, type);
	}

	// helper methods

	protected void runUpdate(final String query) throws IOException {
		final String command = String.format("4s-update $FOURSTORE_CLUSTER_NAME '%s'", query);

		if (showCommands) {
			System.out.println(command);
		}
		UnixUtils.exec(command, environment);
	}

	protected StringBuilder initBuilder() {
		StringBuilder insertQueryBuilder;
		insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		insertQueryBuilder.append("INSERT DATA {");
		return insertQueryBuilder;
	}

	protected void edgesToTriples(final Multimap<String, String> edges, final String edgeLabel, final StringBuilder insertQueryBuilder) {
		boolean first = true;
		for (final Entry<String, String> edge : edges.entries()) {
			if (first) {
				first = false;
			} else {
				insertQueryBuilder.append(".");
			}
			final String sourceVertex = edge.getKey();
			final String targetVertex = edge.getValue();

			insertQueryBuilder.append(String.format(" %s %s %s ", brackets(sourceVertex), brackets(edgeLabel), brackets(targetVertex)));
		}
	}

}
