package eu.mondo.driver.file;

import java.util.ArrayList;
import java.util.List;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

public class VertexCollector extends RDFHandlerBase {

    public VertexCollector(String type) {
    	vertexType = new URIImpl(type);
	}

    private final URI vertexType;
    
	@Override
	public void handleStatement(Statement statement) throws RDFHandlerException {
        if (statement.getPredicate().equals(RDF_TYPE) && statement.getObject().equals(vertexType)) {
            final Long id = IdExtractor.extractId(statement.getSubject().toString());
            vertices.add(id);
        }
	}

	private static final URI RDF_TYPE = new URIImpl("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

	private final List<Long> vertices = new ArrayList<>();

	public List<Long> getVertices() {
		return vertices;
	}

}
