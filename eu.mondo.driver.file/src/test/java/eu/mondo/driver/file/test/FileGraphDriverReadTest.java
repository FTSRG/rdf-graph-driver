package eu.mondo.driver.file.test;

import java.io.IOException;

import org.junit.Test;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import eu.mondo.driver.file.FileGraphDriverRead;
import eu.mondo.driver.graph.test.RDFGraphDriverReadTest;

public class FileGraphDriverReadTest extends RDFGraphDriverReadTest {
	
	@Test
	public void testRead() throws RDFParseException, RDFHandlerException, IOException {
		String filePath = this.getClass().getClassLoader().getResource("models/railway-xform-1.ttl").toString();
		driver = new FileGraphDriverRead(filePath);
	}

}
