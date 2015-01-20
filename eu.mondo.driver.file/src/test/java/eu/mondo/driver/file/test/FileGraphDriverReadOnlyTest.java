package eu.mondo.driver.file.test;

import java.io.IOException;

import org.junit.BeforeClass;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;

import eu.mondo.driver.file.FileGraphDriverReadOnly;
import eu.mondo.driver.graph.test.RDFGraphDriverReadOnlyTest;

public class FileGraphDriverReadOnlyTest extends RDFGraphDriverReadOnlyTest {
	
	@BeforeClass
	public static void setUp() throws RDFParseException, RDFHandlerException, IOException {
		String connectionString = "rdf://src/test/resources/models/railway-xform-1.ttl";
		driver = new FileGraphDriverReadOnly(connectionString);
	}

}
