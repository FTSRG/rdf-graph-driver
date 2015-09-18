package eu.mondo.driver.file.test;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import eu.mondo.driver.file.FileGraphDriverRead;
import eu.mondo.driver.graph.test.RDFGraphDriverReadTest;

public class FileGraphDriverReadTest extends RDFGraphDriverReadTest {

	@Test
	public void testRead() throws RDFParseException, RDFHandlerException, IOException {
		final String filePath = "../eu.mondo.driver.rdf/src/test/resources/railway-example.ttl";
		final String absolutePath = new File(filePath).getAbsolutePath();
		final String uri = "file:///" + absolutePath;
		driver = new FileGraphDriverRead(uri);
	}

}
