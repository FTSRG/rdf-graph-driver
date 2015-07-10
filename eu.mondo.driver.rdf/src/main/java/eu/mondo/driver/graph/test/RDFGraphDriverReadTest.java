package eu.mondo.driver.graph.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import eu.mondo.driver.graph.RDFGraphDriverRead;

public abstract class RDFGraphDriverReadTest extends RDFGraphDriverTest {

	protected static RDFGraphDriverRead driver;

	private static final int SEGMENT_LENGTH_COUNT = 4835;
	private static final int TRACKELEMENT_SENSOR_COUNT = 5772;
	private static final int ROUTE_ROUTEDEFINITION_COUNT = 843;
	private static final int ROUTE_ENTRY_COUNT = 16;
	private static final int SWITCH_COUNT = 217;

	// collect methods

	// vertices
	@Test
	public void testSegment() throws IOException {
		assertEquals(SWITCH_COUNT, driver.collectVertices(SWITCH).size());
	}

	// edges
	@Test
	public void testRoute_entry() throws IOException {
		assertEquals(ROUTE_ENTRY_COUNT, driver.collectEdges(ROUTE_ENTRY).size());
	}

	@Test
	public void testRoute_routeDefinition() throws IOException {
		assertEquals(ROUTE_ROUTEDEFINITION_COUNT, driver.collectEdges(ROUTE_ROUTEDEFINITION).size());
	}

	@Test
	public void testTrackElement_sensor() throws IOException {
		assertEquals(TRACKELEMENT_SENSOR_COUNT, driver.collectEdges(TRACKELEMENT_SENSOR).size());
	}

	// properties
	@Test
	public void testSegment_length() throws IOException {
		assertEquals(SEGMENT_LENGTH_COUNT, driver.collectProperties(SEGMENT_LENGTH).size());
	}

	// count methods

	// vertices
	@Test
	public void testCountVertexSegment() throws IOException {
		assertEquals(SWITCH_COUNT, driver.countVertices(SWITCH));
	}

	// edges
	@Test
	public void testCountEdgeRoute_entry() throws IOException {
		assertEquals(ROUTE_ENTRY_COUNT, driver.countEdges(ROUTE_ENTRY));
	}

	@Test
	public void testCountEdgeRoute_routeDefinition() throws IOException {
		assertEquals(ROUTE_ROUTEDEFINITION_COUNT, driver.countEdges(ROUTE_ROUTEDEFINITION));
	}

	@Test
	public void testCountEdgeTrackElement_sensor() throws IOException {
		assertEquals(TRACKELEMENT_SENSOR_COUNT, driver.countEdges(TRACKELEMENT_SENSOR));
	}

	// properties
	@Test
	public void testCountPropertySegment_length() throws IOException {
		assertEquals(4835, driver.countProperties(SEGMENT_LENGTH));
	}
}
