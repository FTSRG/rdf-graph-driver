package eu.mondo.driver.file;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EdgeCollector extends RDFHandlerBase {

	public EdgeCollector(String type) {
		edgeType = new URIImpl(type);
	}
	
	private final URI edgeType;

	@Override
	public void handleStatement(Statement statement) throws RDFHandlerException {
        if (statement.getPredicate().equals(edgeType)) {
            try {
                final Long subjectId = IdExtractor.extractId(statement.getSubject().toString());
                final Long objectId = IdExtractor.extractId(statement.getObject().toString());
                edges.put(subjectId, objectId);
            } catch (final IllegalStateException e) {
                // drop statement if id is not valid
            }
        }
	}

	private final Multimap<Long, Long> edges = ArrayListMultimap.create();

	public Multimap<Long, Long> getEdges() {
		return edges;
	}

}
