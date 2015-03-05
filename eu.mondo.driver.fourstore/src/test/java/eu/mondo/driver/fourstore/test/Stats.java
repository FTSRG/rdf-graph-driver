package eu.mondo.driver.fourstore.test;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import eu.mondo.driver.fourstore.FourStoreGraphDriverRead;

public class Stats {
	
	@Test
	public void testRead() throws RDFParseException, RDFHandlerException, IOException, InterruptedException {
		final FourStoreGraphDriverRead fsd = new FourStoreGraphDriverRead("trainbenchmark_cluster");
		fsd.start();
		fsd.load("scripts/models/railway-repair-1.ttl");
		
		System.out.println("vertices");
		final List<String> vertexTypes = fsd.getVertexTypes();
		for (final String vertexType : vertexTypes) {
			final long count = fsd.countVertices(vertexType);
			System.out.println(vertexType + ": " + count);
		}
		
		System.out.println();
		System.out.println("edges");
		final List<String> edgeTypes = fsd.getEdgeTypes();
		for (final String edgeType : edgeTypes) {
			final long count = fsd.countEdges(edgeType);
			System.out.println(edgeType + ": " + count);
		}
		
	}

}
