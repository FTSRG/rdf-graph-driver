package eu.mondo.driver.graph.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import eu.mondo.driver.graph.RDFGraphDriverRead;

public abstract class RDFGraphDriverReadTest extends RDFGraphDriverTest {

	protected static RDFGraphDriverRead driver;

	// collect methods

	// vertices
	@Test
	public void testCollectSegment() throws IOException {
		assertEquals(SEGMENT_COUNT, driver.collectVertices(SEGMENT).size());
	}

	// @Test
	// public void testCollectSwitch() throws IOException {
	// assertEquals(SWITCH_COUNT, driver.collectVertices(SWITCH).size());
	// }
	//
	// // edges
	// @Test
	// public void testCollectConnectsTo() throws IOException {
	// assertEquals(CONNECTSTO_COUNT, driver.collectEdges(CONNECTSTO).size());
	// }
	//
	// // properties
	// @Test
	// public void testCollectLength() throws IOException {
	// assertEquals(LENGTH_COUNT, driver.collectProperties(LENGTH).size());
	// }
	//
	// // count methods
	//
	// @Test
	// public void testCountSegment() throws IOException {
	// assertEquals(SEGMENT_COUNT, driver.countVertices(SEGMENT));
	// }
	//
	// @Test
	// public void testCountSwitch() throws IOException {
	// assertEquals(SWITCH_COUNT, driver.countVertices(SWITCH));
	// }
	//
	// // edges
	// @Test
	// public void testCountConnectsTo() throws IOException {
	// assertEquals(CONNECTSTO_COUNT, driver.countEdges(CONNECTSTO));
	// }
	//
	// // properties
	// @Test
	// public void testCountLength() throws IOException {
	// assertEquals(LENGTH_COUNT, driver.countProperties(LENGTH));
	// }

}
