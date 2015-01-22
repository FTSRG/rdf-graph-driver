package eu.mondo.driver.fourstore.test.temp;
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
//package hu.bme.mit.bigmodel.fourstore.test;
//
//import static hu.bme.mit.bigmodel.rdf.RDFHelper.brackets;
//import static org.junit.Assert.assertEquals;
//import hu.bme.mit.bigmodel.fourstore.FourStoreDriverTrainBenchmark;
//import hu.bme.mit.bigmodel.rdf.RDFFormat;
//import hu.bme.mit.bigmodel.rdf.util.UnixUtils;
//
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import com.google.common.collect.ArrayListMultimap;
//import com.google.common.collect.Multimap;
//
//public class FourStoreDriverTest {
//
//	private void deleteEdges() throws IOException {
//		final String routeRouteDefinitionType = brackets(BASE_PREFIX + "Route_routeDefinition");
//		final Multimap<Long, Long> edges0 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(19, edges0.keySet().size());
//		assertEquals(843, edges0.values().size());
//
//		// act
//		// delete 0 edges
//		final Multimap<String, String> edgesToDelete1 = ArrayListMultimap.create();
//		driver.deleteEdges(edgesToDelete1, routeRouteDefinitionType);
//		// assert
//		final Multimap<Long, Long> edges1 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(19, edges1.keySet().size());
//		assertEquals(843, edges1.values().size());
//
//		// act
//		// delete 1 edge
//		final Multimap<String, String> edgesToDelete2 = ArrayListMultimap.create();
//		edgesToDelete2.put(number(2546), number(2609));
//		driver.deleteEdges(edgesToDelete2, routeRouteDefinitionType);
//		// assert
//		// we expect 843 - 1 = 842 target vertices
//		final Multimap<Long, Long> edges2 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(19, edges2.keySet().size());
//		assertEquals(842, edges2.values().size());
//
//		// act
//		// delete 4 edges
//		final Multimap<String, String> edgesToDelete3 = ArrayListMultimap.create();
//		edgesToDelete3.put(number(3216L), number(3251L));
//		edgesToDelete3.put(number(3216L), number(3429L));
//		edgesToDelete3.put(number(3216L), number(3337L));
//		edgesToDelete3.put(number(1L), number(227L));
//		driver.deleteEdges(edgesToDelete3, routeRouteDefinitionType);
//		// assert
//		// we expect 842 - 4 = 838 target vertices
//		final Multimap<Long, Long> edges3 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(19, edges3.keySet().size());
//		assertEquals(838, edges3.values().size());
//	}
//
//	private void insertEdges() throws IOException {
//		final String routeRouteDefinitionType = brackets(BASE_PREFIX + "Route_routeDefinition");
//		// act
//		// insert 0 edges
//		final Multimap<String, String> edgesToInsert1 = ArrayListMultimap.create();
//		driver.insertEdges(edgesToInsert1, routeRouteDefinitionType);
//		// assert
//		final Multimap<Long, Long> edges4 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(19, edges4.keySet().size());
//		assertEquals(838, edges4.values().size());
//
//		// act
//		// insert 1 edge
//		final Multimap<String, String> edgesToInsert2 = ArrayListMultimap.create();
//
//		edgesToInsert2.put(number(123123123), number(456456456));
//		driver.insertEdges(edgesToInsert2, routeRouteDefinitionType);
//		// assert
//		final Multimap<Long, Long> edges5 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(20, edges5.keySet().size());
//		assertEquals(839, edges5.values().size());
//
//		// act
//		// insert 3 edge
//		final Multimap<String, String> edgesToInsert3 = ArrayListMultimap.create();
//		edgesToInsert3.put(number(1231231237L), number(4564564567L));
//		edgesToInsert3.put(number(1231231238L), number(4564564568L));
//		edgesToInsert3.put(number(1231231239L), number(4564564569L));
//		driver.insertEdges(edgesToInsert3, routeRouteDefinitionType);
//		// assert
//		final Multimap<Long, Long> edges6 = driver.collectEdges(routeRouteDefinitionType);
//		assertEquals(23, edges6.keySet().size());
//		assertEquals(842, edges6.values().size());
//
//		// act
//		// insert 0 edge with vertex
//		final Multimap<String, String> edgeAndVerticesToInsert1 = ArrayListMultimap.create();
//		String aURI = brackets("A");
//		String bURI = brackets("B");
//		driver.insertEdgesWithVertex(edgeAndVerticesToInsert1, aURI, bURI);
//		// assert
//		final List<Long> vertices1 = driver.collectVertices(bURI);
//		assertEquals(0, vertices1.size());
//		final Multimap<Long, Long> edgesWithVertex1 = driver.collectEdges(aURI);
//		assertEquals(0, edgesWithVertex1.size());
//
//		// act
//		// insert 1 edge with vertex
//		final Multimap<String, String> edgeAndVerticesToInsert2 = ArrayListMultimap.create();
//		edgeAndVerticesToInsert2.put(number(987654321L), number(654321987L));
//		driver.insertEdgesWithVertex(edgeAndVerticesToInsert2, aURI, bURI);
//		// assert
//		final List<Long> vertices2 = driver.collectVertices(bURI);
//		assertEquals(1, vertices2.size());
//		final Multimap<Long, Long> edgesWithVertex2 = driver.collectEdges(aURI);
//		assertEquals(1, edgesWithVertex2.size());
//
//		// act
//		// insert 3 edge with vertex
//		final Multimap<String, String> edgeAndVerticesToInsert3 = ArrayListMultimap.create();
//		edgeAndVerticesToInsert3.put(number(9876543217L), number(6543219871L));
//		edgeAndVerticesToInsert3.put(number(9876543218L), number(6543219872L));
//		edgeAndVerticesToInsert3.put(number(9876543219L), number(6543219873L));
//		driver.insertEdgesWithVertex(edgeAndVerticesToInsert3, aURI, bURI);
//		// assert
//		final List<Long> vertices3 = driver.collectVertices(bURI);
//		assertEquals(4, vertices3.size());
//		final Multimap<Long, Long> edgesWithVertex3 = driver.collectEdges(aURI);
//		assertEquals(4, edgesWithVertex3.size());
//	}
//
//	@Test
//	public void testVertices() throws IOException, InterruptedException {
//		final String segmentType = brackets(BASE_PREFIX + "Segment");
//
//		long segmentCount = driver.getNodeCount(segmentType);
//		assertEquals(4835, segmentCount);
//
//		final List<Long> vertices0 = driver.collectVertices(segmentType);
//		assertEquals(4835, vertices0.size());
//
//		// act
//		final Collection<String> vertexIdsToDelete1 = new HashSet<>();
//		driver.deleteVertices(vertexIdsToDelete1);
//		// assert
//		final List<Long> vertices1 = driver.collectVertices(segmentType);
//		assertEquals(4835, vertices1.size());
//
//		// act
//		final Collection<String> vertexIdsToDelete2 = Arrays.asList(number(1751));
//		driver.deleteVertices(vertexIdsToDelete2);
//		// assert
//		final List<Long> vertices2 = driver.collectVertices(segmentType);
//		assertEquals(4834, vertices2.size());
//
//		// act
//		final Collection<String> vertexIdsToDelete3 = Arrays.asList(number(4161), number(3708), number(4870));
//		driver.deleteVertices(vertexIdsToDelete3);
//		// assert
//		final List<Long> vertices3 = driver.collectVertices(segmentType);
//		assertEquals(4831, vertices3.size());
//
//		// final String SEGMENT_LENGTH = brackets(BASE_PREFIX + "Segment_length");
//		// // act
//		// final Map<String, Integer> vertexIdAndPropertyValues1 = new HashMap<>();
//		// driver.updateProperties(vertexIdAndPropertyValues1, SEGMENT_LENGTH);
//		// // assert
//		// final Map<Long, Integer> verticesWithProperty1 = driver.collectVerticesWithProperty(SEGMENT_LENGTH);
//		// assertEquals(4831, verticesWithProperty1.size());
//		//
//		// // act
//		// final Map<String, Integer> vertexIdAndPropertyValues2 = new HashMap<>();
//		// vertexIdAndPropertyValues2.put(number(6), 500);
//		// driver.updateProperties(vertexIdAndPropertyValues2, SEGMENT_LENGTH);
//		// // assert
//		// final Map<Long, Integer> verticesWithProperty2 = driver.collectVerticesWithProperty(SEGMENT_LENGTH);
//		// assertEquals(4831, verticesWithProperty2.size());
//		// assertEquals(new Integer(500), verticesWithProperty2.get(6L));
//		//
//		// // act
//		// final Map<String, Integer> vertexIdAndPropertyValues3 = new HashMap<>();
//		// vertexIdAndPropertyValues3.put(number(12), 2);
//		// vertexIdAndPropertyValues3.put(number(13), 3);
//		// vertexIdAndPropertyValues3.put(number(14), 4);
//		// driver.updateProperties(vertexIdAndPropertyValues3, SEGMENT_LENGTH);
//		// // assert
//		// final Map<Long, Integer> verticesWithProperty3 = driver.collectVerticesWithProperty(SEGMENT_LENGTH);
//		// assertEquals(4831, verticesWithProperty3.size());
//		// assertEquals(new Integer(2), verticesWithProperty3.get(12L));
//		// assertEquals(new Integer(3), verticesWithProperty3.get(13L));
//		// assertEquals(new Integer(4), verticesWithProperty3.get(14L));
//	}
//
//}
