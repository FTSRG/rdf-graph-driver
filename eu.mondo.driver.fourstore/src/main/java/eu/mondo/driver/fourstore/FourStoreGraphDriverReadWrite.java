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

import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverWrite;
import eu.mondo.utils.UnixUtils;

public class FourStoreGraphDriverReadWrite extends FourStoreGraphDriverRead implements RDFGraphDriverWrite {

	protected boolean showUpdateCommands = false;

	public FourStoreGraphDriverReadWrite(String connectionString) {
		super(connectionString);
	}
	
	// multiple

	// C
//
//	@Override
//	public void insertEdgesWithVertex(Multimap<String, String> edges, String edgeType, String vertexType) throws IOException {
//		if (edges.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
//		insertQueryBuilder.append("INSERT DATA {");
//		edgesToTriples(edges, edgeType, insertQueryBuilder);
//		for (final String targetVertex : edges.values()) {
//			insertQueryBuilder.append(String.format(". %s rdf:type %s", targetVertex, vertexType));
//		}
//		insertQueryBuilder.append("}");
//
//		// run the update
//		runUpdate(insertQueryBuilder.toString());
//	}
	
	
	public void insertEdges(final Multimap<String, String> edges, final String type) throws IOException {
		if (edges.isEmpty()) {
			return;
		}

		final StringBuilder insertQueryBuilder = new StringBuilder("INSERT DATA {");
		edgesToTriples(edges, type, insertQueryBuilder);
		insertQueryBuilder.append("}");

		// run the update
		runUpdate(insertQueryBuilder.toString());
	}

	// U

	@Override
	public void updateProperties(Multimap<String, Object> properties, String type) throws IOException {
		if (properties.isEmpty()) {
			return;
		}

		final StringBuilder updateQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		int i = 0;

		// delete
		for (final Entry<String, Object> idAndPropertyValue : properties.entries()) {
			final String vertex = idAndPropertyValue.getKey();

			i++;
			updateQueryBuilder.append(String.format("DELETE { %s %s ?a%d } WHERE { %s %s ?a%d }; ", vertex,
					type, i, vertex, type, i));
		}

		// insert
		boolean first = true;
		updateQueryBuilder.append("INSERT DATA {");
		for (final Entry<String, Object> idAndPropertyValue : properties.entries()) {
			if (first) {
				first = false;
			} else {
				updateQueryBuilder.append(".");
			}
			final String vertex = idAndPropertyValue.getKey();
			final Object value = idAndPropertyValue.getValue();
			updateQueryBuilder.append(String.format(" %s %s %s ", vertex, type, value));
		}
		updateQueryBuilder.append("}");

		// run the update
		runUpdate(updateQueryBuilder.toString());
	}
	
	// D

	@Override
	public void deleteVertices(final Collection<String> vertices) throws IOException {
		if (vertices.isEmpty()) {
			return;
		}

		final StringBuilder deleteQueryBuilder = new StringBuilder();

		// add a number to each variable number in the SPARQL query in order to make it unique
		long i = 0;
		for (final String vertex : vertices) {
			i++;
			// delete "incoming edges"
			deleteQueryBuilder.append(String.format("DELETE { ?a%d ?b%d %s } WHERE { ?a%d ?b%d %s }; ", i, i, vertex,
					i, i, vertex));
			i++;
			// delete "outgoing edges" and "properties"
			deleteQueryBuilder.append(String.format("DELETE { %s ?a%d ?b%d } WHERE { %s ?a%d ?b%d }; ", vertex, i, i,
					vertex, i, i));
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

	public long insertVertex(final String vertexType, final long vertexId) throws IOException {
		final String insertQuery = String.format(SPARQL_RDF_PREFIX + "INSERT DATA { %s rdf:type %s }", vertexId,
				vertexType);
		runUpdate(insertQuery);

		return vertexId;
	}

	public void insertEdge(final long sourceVertexId, final long destinationVertexId, final String type)
			throws IOException {
		final String insertQuery = String.format("INSERT DATA { %s %s %s }", sourceVertexId, type,
				destinationVertexId);
		runUpdate(insertQuery);
	}
	
	// U

	public void updateProperty(final String vertex, final String propertyName, final String value) throws IOException {
		final String deleteQuery = String.format("DELETE { %s %s ?b } WHERE { %s %s ?b }", vertex, propertyName,
				vertex, propertyName);
		runUpdate(deleteQuery);

		final String insertQuery = String.format("INSERT DATA { %s %s %s }", vertex, propertyName, value);
		runUpdate(insertQuery);
	}

	// D

	public void deleteVertex(final Long id) throws IOException {
		// if we try to use DELETE DATA (as in the deleteEdge() method), 4store
		// throws an error:
		// DELETE WHERE { x } not yet supported, use DELETE { x } WHERE { x }

		// delete "incoming edges"
		final String deleteQuery1 = String.format(" DELETE { ?a ?b %s } WHERE { ?a ?b %s }", id, id);
		// delete "outgoing edges" and "properties"
		final String deleteQuery2 = String.format(" DELETE { %s ?a ?b } WHERE { %s ?a ?b }", id, id);

		runUpdate(deleteQuery1);
		runUpdate(deleteQuery2);
	}

	public void deleteEdge(final Long sourceVertexId, final Long destinationVertexId, final String type) throws IOException {
		final String deleteQuery = String.format("DELETE DATA { %s %s %s }", sourceVertexId, type, destinationVertexId);
		runUpdate(deleteQuery);
	}

	// helper methods
	
	protected void runUpdate(final String query) throws IOException {
		if (showUpdateCommands) {
			System.out.println("Running update query " + query);
		}

		final String command = String.format("4s-update $FOURSTORE_CLUSTER_NAME '%s'", query);
		UnixUtils.exec(command, environment);
	}

	protected StringBuilder initBuilder() {
		StringBuilder insertQueryBuilder;
		insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
		insertQueryBuilder.append("INSERT DATA {");
		return insertQueryBuilder;
	}
	
	protected void edgesToTriples(final Multimap<String, String> edges, final String edgeLabel,
			final StringBuilder insertQueryBuilder) {
		boolean first = true;
		for (final Entry<String, String> edge : edges.entries()) {
			if (first) {
				first = false;
			} else {
				insertQueryBuilder.append(".");
			}
			final String sourceVertex = edge.getKey();
			final String targetVertex = edge.getValue();

			insertQueryBuilder.append(String.format(" %s %s %s ", sourceVertex, edgeLabel, targetVertex));
		}
	}

	@Override
	public void deleteEdge(long sourceId, long targetId, String type) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertVertex(long id, String type) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteVertex(long id) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public long insertVertices(long id, String type) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

}
