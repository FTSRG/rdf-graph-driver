package hu.bme.mit.bigmodel.rdf;

public class RDFHelper {

	public static String brackets(String URL) {
		if (URL.startsWith("<")) {
			return URL;
		} else {
			return "<" + URL + ">";
		}
	}
	
}
