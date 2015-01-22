package eu.mondo.driver.graph.util;


public class RDFUtil {

	public static String brackets(String URI) {
		if (URI.startsWith("<")) {
			return URI;
		} else {
			return "<" + URI + ">";
		}
	}

	public static String toURI(String prefix, long id) {
		return prefix + "x" + id;
	}
	
}
