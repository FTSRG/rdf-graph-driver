package eu.mondo.driver.graph.util;

public class RDFUtil {

	public static String brackets(String URL) {
		if (URL.startsWith("<")) {
			return URL;
		} else {
			return "<" + URL + ">";
		}
	}

}
