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
package eu.mondo.driver.sparql;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryResultHandlerException;
import org.openrdf.query.resultio.QueryResultParseException;
import org.openrdf.query.resultio.QueryResultParser;
import org.openrdf.query.resultio.helpers.QueryResultCollector;
import org.openrdf.query.resultio.sparqljson.SPARQLResultsJSONParser;

import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverRead;
import eu.mondo.driver.graph.util.RDFUtil;

public class SparqlGraphDriverRead implements RDFGraphDriverRead {

	private static final String ERROR_MESSAGE = "SPARQL endpoint did not respond.";
	protected static final String ID_PREFIX = "_";
	protected static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String SPARQL_RDF_PREFIX = "PREFIX rdf: <" + RDF_PREFIX + "> ";
	private final String queryUrlPrefix;

	public SparqlGraphDriverRead(final String queryUrlPrefix) {
		this.queryUrlPrefix = queryUrlPrefix;
	}

	public Collection<BindingSet> runQuery(final String queryDefinition) throws IOException {
		final QueryResultParser parser = new SPARQLResultsJSONParser();
		final QueryResultCollector collector = new QueryResultCollector();
		parser.setQueryResultHandler(collector);
		final InputStream inputStream = new URL(queryUrlPrefix + URLEncoder.encode(queryDefinition, "UTF-8")).openStream();
		try {
			parser.parseQueryResult(inputStream);
			return collector.getBindingSets();
		} catch (QueryResultParseException | QueryResultHandlerException e) {
			throw Throwables.propagate(e);
		}
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

		throw new IOException(ERROR_MESSAGE);

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

		throw new IOException(ERROR_MESSAGE);
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
		throw new UnsupportedOperationException(); 
	}

	@Override
	public List<String> getEdgeTypes() throws IOException {
		throw new UnsupportedOperationException();
	}


}
