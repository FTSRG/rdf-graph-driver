package hu.bme.mit.bigmodel.rdf.util.test;

import hu.bme.mit.bigmodel.rdf.util.UnixUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

public class UnixUtilsTest {

	@Test
	public void test() throws FileNotFoundException, IOException {
		BufferedReader reader = UnixUtils.exec("ls /", Collections.<String, String> emptyMap(), false);
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

//	@Test
//	public void test4s() throws FileNotFoundException, IOException {
//		String queryCommand = "4s-query trainbenchmark_cluster -f text -s -1 'PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
//				+ "SELECT (COUNT(?x) AS ?count) WHERE {?x rdf:type <http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#Segment>}' ";
//
//		CommandLine commandLine = new CommandLine("/bin/bash");
//		commandLine.addArguments(new String[] { "-c", queryCommand }, false);
//		new DefaultExecutor().execute(commandLine);
//	}

	@Test(expected = IOException.class)
	public void testFail() throws FileNotFoundException, IOException {
		BufferedReader reader = UnixUtils.exec("ls :", Collections.<String, String> emptyMap(), false);
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println(line);
		}
	}

}
