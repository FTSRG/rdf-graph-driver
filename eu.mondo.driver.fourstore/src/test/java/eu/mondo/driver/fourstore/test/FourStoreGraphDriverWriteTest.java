/// *******************************************************************************
// * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Gabor Szarnyas - initial API and implementation
// *******************************************************************************/
// package eu.mondo.driver.fourstore.test;
//
// import static org.junit.Assert.assertEquals;
//
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;
//
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
//
// import com.google.common.collect.ArrayListMultimap;
// import com.google.common.collect.Multimap;
//
// import eu.mondo.driver.fourstore.FourStoreGraphDriverReadWrite;
// import eu.mondo.driver.graph.test.RDFGraphDriverWriteTest;
//
// public class FourStoreGraphDriverWriteTest extends RDFGraphDriverWriteTest {
//
// protected static FourStoreGraphDriverReadWrite fourStoreDriver;
//
// @Before
// public void before() throws FileNotFoundException, IOException, InterruptedException {
// final String connectionString = "trainbenchmark_cluster";
// driver = fourStoreDriver = new FourStoreGraphDriverReadWrite(connectionString);
//
// fourStoreDriver.start();
// final File file = new File("src/test/resources/models/railway-repair-1.ttl");
// fourStoreDriver.load(file.getAbsolutePath());
// }
//
// @After
// public void after() throws FileNotFoundException, IOException {
// fourStoreDriver.stop();
// }
//
// @Test
// public void deleteEdges() throws IOException {
// final Multimap<Long, Long> edges0 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(19, edges0.keySet().size());
// assertEquals(843, edges0.values().size());
//
// // act
// // delete 0 edges
// final Multimap<String, String> edges1 = ArrayListMultimap.create();
// driver.deleteEdges(edges1, ROUTE_ROUTEDEFINITION);
// // assert
// final Multimap<Long, Long> edges2 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(19, edges2.keySet().size());
// assertEquals(843, edges2.values().size());
//
// // act
// // delete 1 edge
// final Multimap<String, String> edges3 = ArrayListMultimap.create();
// edges3.put(id(2547), id(2549));
// driver.deleteEdges(edges3, ROUTE_ROUTEDEFINITION);
// // assert
// // we expect 843 - 1 = 842 target vertices
// final Multimap<Long, Long> edges4 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(19, edges4.keySet().size());
// assertEquals(842, edges4.values().size());
//
// // act
// // delete 4 edges
// final Multimap<String, String> edges5 = ArrayListMultimap.create();
// edges5.put(id(3217), id(3263));
// edges5.put(id(3217), id(3271));
// edges5.put(id(3217), id(3277));
// edges5.put(id(3217), id(3283));
// driver.deleteEdges(edges5, ROUTE_ROUTEDEFINITION);
// // assert
// // we expect 842 - 4 = 838 target vertices
// final Multimap<Long, Long> edges6 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(19, edges6.keySet().size());
// assertEquals(838, edges6.values().size());
// }
//
// @Test
// public void insertEdges() throws IOException {
// // act
// // insert 0 edges
// final Multimap<String, String> edges1 = ArrayListMultimap.create();
// driver.insertEdges(edges1, ROUTE_ROUTEDEFINITION);
// // assert
// final Multimap<Long, Long> edges2 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(19, edges2.keySet().size());
// assertEquals(843, edges2.values().size());
//
// // act
// // insert 1 edge
// final Multimap<String, String> edges3 = ArrayListMultimap.create();
//
// edges3.put(id(123123123), id(456456456));
// driver.insertEdges(edges3, ROUTE_ROUTEDEFINITION);
// // assert
// final Multimap<Long, Long> edges4 = driver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(20, edges4.keySet().size());
// assertEquals(844, edges4.values().size());
//
// // act
// // insert 3 edge
// final Multimap<String, String> edges5 = ArrayListMultimap.create();
// edges5.put(id(1231231237L), id(4564564567L));
// edges5.put(id(1231231238L), id(4564564568L));
// edges5.put(id(1231231239L), id(4564564569L));
// driver.insertEdges(edges5, ROUTE_ROUTEDEFINITION);
// // assert
// final Multimap<Long, Long> edges6 = fourStoreDriver.collectEdges(ROUTE_ROUTEDEFINITION);
// assertEquals(23, edges6.keySet().size());
// assertEquals(847, edges6.values().size());
//
// // act
// // insert 0 edge with vertex
// final Multimap<String, String> edges7 = ArrayListMultimap.create();
// final String aURI = "A";
// final String bURI = "B";
// driver.insertEdgesWithVertex(edges7, aURI, bURI);
// // assert
// final List<Long> vertices1 = fourStoreDriver.collectVertices(bURI);
// assertEquals(0, vertices1.size());
// final Multimap<Long, Long> edges8 = fourStoreDriver.collectEdges(aURI);
// assertEquals(0, edges8.size());
//
// // act
// // insert 1 edge with vertex
// final Multimap<String, String> edges9 = ArrayListMultimap.create();
// edges9.put(id(987654321L), id(654321987L));
// fourStoreDriver.insertEdgesWithVertex(edges9, aURI, bURI);
// // assert
// final List<Long> vertices2 = fourStoreDriver.collectVertices(bURI);
// assertEquals(1, vertices2.size());
// final Multimap<Long, Long> edges10 = fourStoreDriver.collectEdges(aURI);
// assertEquals(1, edges10.size());
//
// // act
// // insert 3 edge with vertex
// final Multimap<String, String> edges11 = ArrayListMultimap.create();
// edges11.put(id(9876543217L), id(6543219871L));
// edges11.put(id(9876543218L), id(6543219872L));
// edges11.put(id(9876543219L), id(6543219873L));
// fourStoreDriver.insertEdgesWithVertex(edges11, aURI, bURI);
// // assert
// final List<Long> vertices3 = fourStoreDriver.collectVertices(bURI);
// assertEquals(4, vertices3.size());
// final Multimap<Long, Long> edges12 = fourStoreDriver.collectEdges(aURI);
// assertEquals(4, edges12.size());
// }
//
// @Test
// public void testVertices() throws IOException, InterruptedException {
// final long segmentCount = fourStoreDriver.countVertices(SEGMENT);
// assertEquals(4835, segmentCount);
//
// final List<Long> vertices0 = fourStoreDriver.collectVertices(SEGMENT);
// assertEquals(4835, vertices0.size());
//
// // act
// final List<String> vertexIdsToDelete1 = new ArrayList<>();
// fourStoreDriver.deleteVertices(vertexIdsToDelete1);
// // assert
// final List<Long> vertices1 = fourStoreDriver.collectVertices(SEGMENT);
// assertEquals(4835, vertices1.size());
//
// // act
// final List<String> vertexIdsToDelete2 = Arrays.asList(id(1751));
// fourStoreDriver.deleteVertices(vertexIdsToDelete2);
// // assert
// final List<Long> vertices2 = fourStoreDriver.collectVertices(SEGMENT);
// assertEquals(4834, vertices2.size());
//
// // act
// final List<String> vertexIdsToDelete3 = Arrays.asList(id(4161), id(3708), id(4870));
// fourStoreDriver.deleteVertices(vertexIdsToDelete3);
// // assert
// final List<Long> vertices3 = fourStoreDriver.collectVertices(SEGMENT);
// assertEquals(4831, vertices3.size());
//
// // act
// final Map<String, Object> properties1 = new HashMap<>();
// driver.updateProperties(properties1, SEGMENT_LENGTH);
// // assert
// final Multimap<Long, Object> collectedProperties1 = driver.collectProperties(SEGMENT_LENGTH);
// assertEquals(4831, collectedProperties1.size());
//
// // act
// final Map<String, Object> properties2 = new HashMap<>();
// properties2.put(id(6), number(500));
// driver.updateProperties(properties2, SEGMENT_LENGTH);
// // assert
// final Multimap<Long, Object> collectedProperties2 = driver.collectProperties(SEGMENT_LENGTH);
// assertEquals(4831, collectedProperties2.size());
// assertEquals(500, collectedProperties2.get(6L).iterator().next());
//
// // // act
// final Map<String, Object> properties3 = new HashMap<>();
// properties3.put(id(12), 2);
// properties3.put(id(13), 3);
// properties3.put(id(14), 4);
// driver.updateProperties(properties3, SEGMENT_LENGTH);
// // assert
// final Multimap<Long, Object> collectedProperties3 = driver.collectProperties(SEGMENT_LENGTH);
// assertEquals(4831, collectedProperties3.size());
// assertEquals(2, collectedProperties3.get(12L).iterator().next());
// assertEquals(3, collectedProperties3.get(13L).iterator().next());
// assertEquals(4, collectedProperties3.get(14L).iterator().next());
// }
//
// private String id(final long l) {
// return BASE_URI + ID_PREFIX + l;
// }
//
// private String number(final long l) {
// return Long.toString(l);
// }
//
// }
