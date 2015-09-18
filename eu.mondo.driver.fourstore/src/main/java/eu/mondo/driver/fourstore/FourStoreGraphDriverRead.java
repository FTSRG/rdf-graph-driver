/*******************************************************************************
 * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package eu.mondo.driver.fourstore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverRead;
import eu.mondo.driver.graph.util.RDFUtil;

public class FourStoreGraphDriverRead extends FourStoreGraphDriverQueryExecutor implements RDFGraphDriverRead {

	protected static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String SPARQL_RDF_PREFIX = "PREFIX rdf: <" + RDF_PREFIX + "> ";

	public FourStoreGraphDriverRead(final String connectionString) {
		super(connectionString);
	}

	@Override
	public long countVertices(final String type) throws IOException {
		final String queryDefinition = SPARQL_RDF_PREFIX
				+ String.format("SELECT (COUNT(?x) AS ?count) WHERE {?x rdf:type %s}", RDFUtil.brackets(type));
		final Collection<BindingSet> bindingSets = runQuery(queryDefinition);
		for (final BindingSet bindingSet : bindingSets) {
			final Literal l = (Literal) bindingSet.getBinding("count").getValue();
			final long count = l.longValue();
			return count;
		}

		throw new IOException("Database did not respond.");

	}

	@Override
	public long countEdges(final String type) throws IOException {
		final String queryDefinition = String.format("SELECT (COUNT(?x) AS ?count) WHERE {?x %s ?y}", RDFUtil.brackets(type));
		final Collection<BindingSet> bindingSets = runQuery(queryDefinition);
		for (final BindingSet bindingSet : bindingSets) {
			final Literal l = (Literal) bindingSet.getBinding("count").getValue();
			final long count = l.longValue();
			return count;
		}

		throw new IOException("Database did not respond.");
	}

	@Override
	public long countProperties(final String type) throws IOException {
		return countEdges(type);
	}

	@Override
	public List<Resource> collectVertices(final String type) throws IOException {
		final List<Resource> vertices = new ArrayList<>();

		final String queryDefinition = String.format(SPARQL_RDF_PREFIX + "SELECT ?x WHERE { ?x rdf:type %s }", RDFUtil.brackets(type));
		final Collection<BindingSet> bindingSets = runQuery(queryDefinition);
		for (final BindingSet bindingSet : bindingSets) {
			final Resource vertex = (Resource) bindingSet.getBinding("x").getValue();
			vertices.add(vertex);
		}

		return vertices;
	}

	@Override
	public Multimap<Resource, Resource> collectEdges(final String type) throws IOException {
		final Multimap<Resource, Resource> edges = ArrayListMultimap.create();

		final String queryDefinition = String.format("SELECT ?x ?y WHERE { ?x %s ?y }", RDFUtil.brackets(type));
		final Collection<BindingSet> bindingSets = runQuery(queryDefinition);
		for (final BindingSet bindingSet : bindingSets) {
			final Resource v1 = (Resource) bindingSet.getBinding("x").getValue();
			final Resource v2 = (Resource) bindingSet.getBinding("y").getValue();
			edges.put(v1, v2);
		}

		return edges;
	}

	@Override
	public Multimap<Resource, Value> collectProperties(final String type) throws IOException {
		final Multimap<Resource, Value> properties = ArrayListMultimap.create();

		final String queryDefinition = String.format("SELECT ?x ?y WHERE { ?x %s ?y }", RDFUtil.brackets(type));
		final Collection<BindingSet> bindingSets = runQuery(queryDefinition);
		for (final BindingSet bindingSet : bindingSets) {
			final Resource v = (Resource) bindingSet.getBinding("x").getValue();
			final Value p = bindingSet.getBinding("y").getValue();
			properties.put(v, p);
		}

		return properties;
	}

	@Override
	public List<String> getVertexTypes() throws IOException {
		final String query = SPARQL_RDF_PREFIX + "SELECT DISTINCT ?t WHERE { ?_ rdf:type ?t }";
		return getTypes(query);
	}

	@Override
	public List<String> getEdgeTypes() throws IOException {
		final String query = "SELECT DISTINCT ?t WHERE { ?_x ?t ?_y } ";
		return getTypes(query);
	}

	private List<String> getTypes(final String query) throws IOException {
		// final BufferedReader reader = runQuery(query);
		final Collection<BindingSet> bindingSet = runQuery(query);

		// final List<String> types = new ArrayList<>();
		// String line;
		//
		// // skip the first line
		// reader.readLine();
		//
		// // read the rest
		// while ((line = reader.readLine()) != null) {
		// types.add(line);
		// }
		// reader.readLine();

		return null;
	}

	public boolean ask(final String query) throws IOException {
		// final BufferedReader reader = runQuery(query);
		// final String line = reader.readLine();
		//
		// switch (line) {
		// case "true":
		// return true;
		// case "false":
		// return false;
		// default:
		// throw new IOException("Result for ASK query should be 'true' or 'false'. Received '" + line + "' instead.");
		// }
		return false;
	}

}
