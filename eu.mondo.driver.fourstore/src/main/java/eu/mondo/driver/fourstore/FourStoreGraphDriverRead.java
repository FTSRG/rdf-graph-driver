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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverRead;
import eu.mondo.driver.graph.util.LiteralParser;
import eu.mondo.driver.graph.util.RDFUtil;

public class FourStoreGraphDriverRead extends FourStoreGraphDriverQueryExecutor implements RDFGraphDriverRead {

	protected static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String SPARQL_RDF_PREFIX = "PREFIX rdf: <" + RDF_PREFIX + "> ";

	public FourStoreGraphDriverRead(final String connectionString) {
		super(connectionString);
	}

	@Override
	public long countVertices(final String type) throws IOException {
		final String query = SPARQL_RDF_PREFIX
				+ String.format("SELECT (COUNT(?x) AS ?count) WHERE {?x rdf:type %s}", RDFUtil.brackets(type));
		final BufferedReader reader = runQuery(query);

		// skip the first line
		reader.readLine();

		final String line = reader.readLine();
		return Integer.parseInt(line);
	}

	@Override
	public long countEdges(final String type) throws IOException {
		final String query = String.format("SELECT (COUNT(?x) AS ?count) WHERE {?x %s ?y}", RDFUtil.brackets(type));
		final BufferedReader reader = runQuery(query);

		// skip the first line
		reader.readLine();

		final String line = reader.readLine();
		return Integer.parseInt(line);
	}

	@Override
	public long countProperties(final String type) throws IOException {
		return countEdges(type);
	}

	@Override
	public List<Long> collectVertices(final String type) throws IOException {
		final String queryString = String.format(SPARQL_RDF_PREFIX + "SELECT ?a WHERE { ?a rdf:type %s }", RDFUtil.brackets(type));
		return queryIds(queryString);
	}

	@Override
	public Multimap<Long, Long> collectEdges(final String type) throws IOException {
		final Multimap<Long, Long> edges = ArrayListMultimap.create();

		final String query = String.format("SELECT ?a ?b WHERE { ?a %s ?b }", RDFUtil.brackets(type));
		final BufferedReader reader = runQuery(query);

		// collecting ids
		final Pattern pattern = Pattern.compile("#" + ID_PREFIX +"(\\d+)>");
		String line;
		while ((line = reader.readLine()) != null) {
			final Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				final String sourceString = matcher.group(1);
				if (matcher.find()) {
					final String targetString = matcher.group(1);
					final Long source = new Long(sourceString);
					final Long destination = new Long(targetString);
					edges.put(source, destination);
				}
			}
		}

		return edges;
	}

	@Override
	public Multimap<Long, Object> collectProperties(final String type) throws IOException {
		final Multimap<Long, Object> properties = ArrayListMultimap.create();

		final String query = String.format("SELECT ?a ?b WHERE { ?a %s ?b }", RDFUtil.brackets(type));
		final BufferedReader reader = runQuery(query);

		// example (tab-separated output)
		// @formatter:off
		// <http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#[ID_PREFIX]87947>	"653"^^<http://www.w3.org/2001/XMLSchema#int>
		// @formatter:on
		final String regex = "<.*#" + ID_PREFIX + "(\\d+)>\\t(.*)";
		final Pattern pattern = Pattern.compile(regex);

		String line;
		while ((line = reader.readLine()) != null) {
			final Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				final Long id = new Long(matcher.group(1));
				final String propertyString = matcher.group(2);
				final Object propertyObject = LiteralParser.stringToObject(propertyString);

				properties.put(id, propertyObject);
			}
		}
		return properties;
	}

	@Override
	public List<String> getVertexTypes() throws IOException {
		final String query = SPARQL_RDF_PREFIX + "SELECT DISTINCT ?x WHERE { ?_ rdf:type ?x }";
		return getTypes(query);
	}

	@Override
	public List<String> getEdgeTypes() throws IOException {
		final String query = "SELECT DISTINCT ?x WHERE { ?_a ?x ?_b } ";
		return getTypes(query);
	}

	private List<String> getTypes(final String query) throws IOException {
		final BufferedReader reader = runQuery(query);

		final List<String> types = new ArrayList<>();
		String line;

		// skip the first line
		reader.readLine();

		// read the rest
		while ((line = reader.readLine()) != null) {
			types.add(line);
		}
		reader.readLine();

		return types;
	}

	public boolean ask(final String query) throws IOException {
		final BufferedReader reader = runQuery(query);
		final String line = reader.readLine();
		
		switch (line) {
		case "true":
			return true;
		case "false":
			return false;
		default:
			throw new IOException("Result for ASK query should be 'true' or 'false'. Received '" + line + "' instead.");
		}
	}
	
}
