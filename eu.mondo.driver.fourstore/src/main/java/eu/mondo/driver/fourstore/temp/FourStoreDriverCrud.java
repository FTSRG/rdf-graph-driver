package eu.mondo.driver.fourstore.temp;
///*******************************************************************************
// * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Gabor Szarnyas - initial API and implementation
// *******************************************************************************/
//package eu.mondo.driver.fourstore;
//
//import static hu.bme.mit.bigmodel.rdf.RDFHelper.brackets;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Multimap;
//import com.hp.hpl.jena.rdf.model.Model;
//import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.hp.hpl.jena.rdf.model.Statement;
//import com.hp.hpl.jena.rdf.model.StmtIterator;
//
//import eu.mondo.utils.UnixUtils;
//
///**
// * Adds CRUD operations to the 4store loader class.
// * 
// * @author szarnyasg
// * 
// */
//public class FourStoreDriverCrud extends FourStoreGraphDriverLoader {
//
//	protected static final String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//	protected static final String SPARQL_RDF_PREFIX = "PREFIX rdf: <" + RDF_PREFIX + "> ";
//
//	public ArrayList<String> getVertexTypes() throws IOException {
//		String query = SPARQL_RDF_PREFIX + "SELECT DISTINCT ?x WHERE { ?_ rdf:type ?x }";
//		return getTypes(query);
//	}
//
//	public ArrayList<String> getEdgeTypes() throws IOException {
//		String query = "SELECT DISTINCT ?x WHERE { ?_a ?x ?_b } ";
//		return getTypes(query);
//	}
//
//	public void runUpdate(final String query) throws IOException {
//		if (showUpdateCommands) {
//			System.out.println("Running update query " + query);
//		}
//
//		final String command = String.format("4s-update $FOURSTORE_CLUSTER_NAME '%s'", query);
//		UnixUtils.exec(command, environment);
//	}
//
//	private ArrayList<String> getTypes(String query) throws IOException {
//		BufferedReader reader = runQuery(query);
//
//		ArrayList<String> types = new ArrayList<>();
//		String line;
//		
//		// skip the first line
//		reader.readLine();
//		
//		// read the rest
//		while ((line = reader.readLine()) != null) {
//			types.add(line);
//		}
//		reader.readLine();
//
//		return types;
//	}
//
//	// modifications
//
//	public void updateProperty(final String vertex, final String propertyName, final String value) throws IOException {
//		final String deleteQuery = String.format("DELETE { %s %s ?b } WHERE { %s %s ?b }", vertex, propertyName,
//				vertex, propertyName);
//		runUpdate(deleteQuery);
//
//		final String insertQuery = String.format("INSERT DATA { %s %s %s }", vertex, propertyName, value);
//		runUpdate(insertQuery);
//	}
//
//	public void deleteVertices(final Collection<String> vertices) throws IOException {
//		if (vertices.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder deleteQueryBuilder = new StringBuilder();
//
//		// add a number to each variable number in the SPARQL query in order to make it unique
//		long i = 0;
//		for (final String vertex : vertices) {
//			i++;
//			// delete "incoming edges"
//			deleteQueryBuilder.append(String.format("DELETE { ?a%d ?b%d %s } WHERE { ?a%d ?b%d %s }; ", i, i, vertex,
//					i, i, vertex));
//			i++;
//			// delete "outgoing edges" and "properties"
//			deleteQueryBuilder.append(String.format("DELETE { %s ?a%d ?b%d } WHERE { %s ?a%d ?b%d }; ", vertex, i, i,
//					vertex, i, i));
//		}
//
//		runUpdate(deleteQueryBuilder.toString());
//	}
//
//	public void deleteEdges(final Multimap<String, String> edges, final String edgeLabel) throws IOException {
//		if (edges.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder deleteQueryBuilder = new StringBuilder("DELETE DATA {");
//		edgesToTriples(edges, edgeLabel, deleteQueryBuilder);
//		deleteQueryBuilder.append("}");
//		runUpdate(deleteQueryBuilder.toString());
//	}
//
//	public void deleteTriples(final String turtleFile) throws IOException {
//		Model model = ModelFactory.createDefaultModel();
//		model.read(new FileInputStream(turtleFile),
//				null, "TTL");
//
//		StmtIterator statements = model.listStatements();
//
//		List<String> triples = new ArrayList<>();
//		while (statements.hasNext()) {
//			Statement statement = statements.next();
//			String triple = brackets(statement.getSubject().toString()) + " "
//					+ brackets(statement.getPredicate().toString()) + " " + brackets(statement.getObject().toString());
//			triples.add(triple);
//		}
//
//		List<List<String>> partitions = Lists.partition(triples, 100);
//		for (List<String> partition : partitions) {
//			final StringBuilder deleteQueryBuilder = new StringBuilder("DELETE DATA {");
//
//			boolean first = true;
//			for (String triple : partition) {
//				if (first) {
//					first = false;
//				} else {
//					deleteQueryBuilder.append(".");
//				}
//				deleteQueryBuilder.append(" " + triple + " ");
//			}
//			deleteQueryBuilder.append("}");
//			runUpdate(deleteQueryBuilder.toString());
//		}
//	}
//
//	public void updateProperties(final Map<String, String> vertexIdAndPropertyValues, final String propertyURI)
//			throws IOException {
//		if (vertexIdAndPropertyValues.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder updateQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
//		int i = 0;
//
//		// delete
//		for (final Entry<String, String> idAndPropertyValue : vertexIdAndPropertyValues.entrySet()) {
//			final String vertex = idAndPropertyValue.getKey();
//
//			i++;
//			updateQueryBuilder.append(String.format("DELETE { %s %s ?a%d } WHERE { %s %s ?a%d }; ", vertex,
//					propertyURI, i, vertex, propertyURI, i));
//		}
//
//		// insert
//		boolean first = true;
//		updateQueryBuilder.append("INSERT DATA {");
//		for (final Entry<String, String> idAndPropertyValue : vertexIdAndPropertyValues.entrySet()) {
//			if (first) {
//				first = false;
//			} else {
//				updateQueryBuilder.append(".");
//			}
//			final String vertex = idAndPropertyValue.getKey();
//			final String value = idAndPropertyValue.getValue();
//
//			updateQueryBuilder.append(String.format(" %s %s %s ", vertex, propertyURI, value));
//		}
//		updateQueryBuilder.append("}");
//
//		// run the update
//		runUpdate(updateQueryBuilder.toString());
//	}
//
//	public void insertEdges(final Multimap<String, String> edges, final String edgeLabel) throws IOException {
//		if (edges.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder insertQueryBuilder = new StringBuilder("INSERT DATA {");
//		edgesToTriples(edges, edgeLabel, insertQueryBuilder);
//		insertQueryBuilder.append("}");
//
//		// run the update
//		runUpdate(insertQueryBuilder.toString());
//	}
//
//	public void insertEdgesWithVertex(final Multimap<String, String> edges, final String edgeLabel,
//			final String vertexType) throws IOException {
//		if (edges.isEmpty()) {
//			return;
//		}
//
//		final StringBuilder insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
//		insertQueryBuilder.append("INSERT DATA {");
//		edgesToTriples(edges, edgeLabel, insertQueryBuilder);
//		for (final String targetVertex : edges.values()) {
//			insertQueryBuilder.append(String.format(". %s rdf:type %s", targetVertex, vertexType));
//		}
//		insertQueryBuilder.append("}");
//
//		// run the update
//		runUpdate(insertQueryBuilder.toString());
//	}
//
//	private void edgesToTriples(final Multimap<String, String> edges, final String edgeLabel,
//			final StringBuilder insertQueryBuilder) {
//		boolean first = true;
//		for (final Entry<String, String> edge : edges.entries()) {
//			if (first) {
//				first = false;
//			} else {
//				insertQueryBuilder.append(".");
//			}
//			final String sourceVertex = edge.getKey();
//			final String targetVertex = edge.getValue();
//
//			insertQueryBuilder.append(String.format(" %s %s %s ", sourceVertex, edgeLabel, targetVertex));
//		}
//	}
//
//	// deletions
//
//	public void deleteVertex(final Long id) throws IOException {
//		// if we try to use DELETE DATA (as in the deleteEdge() method), 4store throws an error:
//		// DELETE WHERE { x } not yet supported, use DELETE { x } WHERE { x }
//
//		// delete "incoming edges"
//		final String deleteQuery1 = String.format(" DELETE { ?a ?b %s } WHERE { ?a ?b %s }", id, id);
//		// delete "outgoing edges" and "properties"
//		final String deleteQuery2 = String.format(" DELETE { %s ?a ?b } WHERE { %s ?a ?b }", id, id);
//
//		runUpdate(deleteQuery1);
//		runUpdate(deleteQuery2);
//	}
//
//	public void deleteEdge(final Long sourceVertexId, final Long destinationVertexId, final String edgeLabel)
//			throws IOException {
//		final String deleteQuery = String.format("DELETE DATA { %s %s %s }", sourceVertexId, edgeLabel,
//				destinationVertexId);
//		runUpdate(deleteQuery);
//	}
//
//	// insertions
//
//	public long insertVertex(final String vertexType, final long vertexId) throws IOException {
//		final String insertQuery = String.format(SPARQL_RDF_PREFIX + "INSERT DATA { %s rdf:type %s }", vertexId,
//				vertexType);
//		runUpdate(insertQuery);
//
//		return vertexId;
//	}
//
//	public void insertEdge(final long sourceVertexId, final long destinationVertexId, final String edgeLabel)
//			throws IOException {
//		final String insertQuery = String.format("INSERT DATA { %s %s %s }", sourceVertexId, edgeLabel,
//				destinationVertexId);
//		runUpdate(insertQuery);
//	}
//
//	public long getMemoryConsumption() throws FileNotFoundException, IOException {
//		final String command = UnixUtils.createTempFileFromScript("4s-memory.sh");
//		final BufferedReader reader = UnixUtils.exec(command, environment);
//		final String line = reader.readLine();
//
//		long memoryConsumption;
//		try {
//			memoryConsumption = Long.parseLong(line);
//		} catch (final NumberFormatException e) {
//			memoryConsumption = 0;
//		}
//		return memoryConsumption;
//	}
//
//	protected StringBuilder initBuilder() {
//		StringBuilder insertQueryBuilder;
//		insertQueryBuilder = new StringBuilder(SPARQL_RDF_PREFIX);
//		insertQueryBuilder.append("INSERT DATA {");
//		return insertQueryBuilder;
//	}
//
//}
