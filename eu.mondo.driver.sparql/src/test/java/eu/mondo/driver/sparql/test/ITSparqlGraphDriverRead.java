package eu.mondo.driver.sparql.test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import eu.mondo.driver.sparql.SparqlGraphDriverRead;

public class ITSparqlGraphDriverRead {

	@Test
	public void wikidata() throws IOException {
		SparqlGraphDriverRead driver = new SparqlGraphDriverRead("https://query.wikidata.org/bigdata/namespace/wdq/sparql?format=json&query=");
		assertTrue(driver.countEdges("http://www.wikidata.org/prop/direct/P31") > 0); 
	}

}
