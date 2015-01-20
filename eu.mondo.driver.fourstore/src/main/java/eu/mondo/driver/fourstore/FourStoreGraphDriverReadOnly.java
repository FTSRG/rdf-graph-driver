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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverReadOnly;
import eu.mondo.driver.graph.util.RDFUtil;

public class FourStoreGraphDriverReadOnly extends FourStoreGraphDriverQueryExecutor implements RDFGraphDriverReadOnly {

	protected static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String SPARQL_RDF_PREFIX = "PREFIX rdf: <" + RDF_PREFIX + "> ";

	public FourStoreGraphDriverReadOnly(final String connectionString) {
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
		final Pattern pattern = Pattern.compile("#x(.*?)>");
		String line;
		while ((line = reader.readLine()) != null) {
			final Matcher matcher = pattern.matcher(line);

			if (matcher.find()) {
				final String sourceString = matcher.group(1);
				if (matcher.find()) {
					final String destinationString = matcher.group(1);
					final Long source = new Long(sourceString);
					final Long destination = new Long(destinationString);
					edges.put(source, destination);
				}
			}
		}

		return edges;
	}

	@Override
	public Multimap<Long, String> collectProperties(final String type) throws IOException {
		final Multimap<Long, String> properties = ArrayListMultimap.create();

		final String query = String.format("SELECT ?a ?b WHERE { ?a %s ?b }", RDFUtil.brackets(type));
		final BufferedReader reader = runQuery(query);

		// example (tab-separated output)
		// @formatter:off
		// <http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#x87947>	"653"^^<http://www.w3.org/2001/XMLSchema#int>
		// @formatter:on
		final String regex = "<.*x(\\d+)>\\t(.*)";
		final Pattern pattern = Pattern.compile(regex);

		String line;
		while ((line = reader.readLine()) != null) {
			final Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				final Long id = new Long(matcher.group(1));
				final String property = matcher.group(2);
				properties.put(id, property);
			}
		}
		return properties;
	}

}
