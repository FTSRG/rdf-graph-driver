package eu.mondo.driver.graph.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import eu.mondo.driver.graph.RDFGraphDriverReadOnly;

public abstract class RDFGraphDriverReadOnlyTest {

	protected static RDFGraphDriverReadOnly driver;

	protected static final String TRAINBENCHMARK_BASE = "http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#";

	private static final String SEGMENT_LENGTH = TRAINBENCHMARK_BASE + "Segment_length";
	private static final String TRACKELEMENT_SENSOR = TRAINBENCHMARK_BASE + "TrackElement_sensor";
	private static final String ROUTE_ROUTEDEFINITION = TRAINBENCHMARK_BASE + "Route_routeDefinition";
	private static final String ROUTE_ENTRY = TRAINBENCHMARK_BASE + "Route_entry";
	private static final String SWITCH = TRAINBENCHMARK_BASE + "Switch";

	// collect methods

	// vertices
	@Test
	public void testSegment() throws IOException {
		assertEquals(217, driver.collectVertices(SWITCH).size());
	}

	// edges
	@Test
	public void testRoute_entry() throws IOException {
		assertEquals(16, driver.collectEdges(ROUTE_ENTRY).size());
	}

	@Test
	public void testRoute_routeDefinition() throws IOException {
		assertEquals(843, driver.collectEdges(ROUTE_ROUTEDEFINITION).size());
	}

	@Test
	public void testTrackElement_sensor() throws IOException {
		assertEquals(5772, driver.collectEdges(TRACKELEMENT_SENSOR).size());
	}

	// properties
	@Test
	public void testSegment_length() throws IOException {
		assertEquals(4835, driver.collectProperties(SEGMENT_LENGTH).size());
	}

	// count methods

	// vertices
	@Test
	public void testCountVertexSegment() throws IOException {
		assertEquals(217, driver.countVertices(SWITCH));
	}

	// edges
	@Test
	public void testCountEdgeRoute_entry() throws IOException {
		assertEquals(16, driver.countEdges(ROUTE_ENTRY));
	}

	@Test
	public void testCountEdgeRoute_routeDefinition() throws IOException {
		assertEquals(843, driver.countEdges(ROUTE_ROUTEDEFINITION));
	}

	@Test
	public void testCountEdgeTrackElement_sensor() throws IOException {
		assertEquals(5772, driver.countEdges(TRACKELEMENT_SENSOR));
	}

	// properties
	@Test
	public void testCountPropertySegment_length() throws IOException {
		assertEquals(4835, driver.countProperties(SEGMENT_LENGTH));
	}
}
