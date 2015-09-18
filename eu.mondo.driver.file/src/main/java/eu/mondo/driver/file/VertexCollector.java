package eu.mondo.driver.file;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class VertexCollector extends RDFHandlerBase {

	public VertexCollector(final String type) {
		vertexType = new URIImpl(type);
	}

	private final URI vertexType;

	@Override
	public void handleStatement(final Statement statement) throws RDFHandlerException {
		if (statement.getPredicate().equals(RDF_TYPE) && statement.getObject().equals(vertexType)) {
			vertices.add(statement.getSubject());
		}
	}

	private static final URI RDF_TYPE = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

	private final List<Resource> vertices = new ArrayList<>();

	public List<Resource> getVertices() {
		return vertices;
	}

}
