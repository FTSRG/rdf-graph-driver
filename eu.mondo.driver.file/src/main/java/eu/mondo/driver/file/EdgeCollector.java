package eu.mondo.driver.file;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class EdgeCollector extends RDFHandlerBase {

	public EdgeCollector(final String type) {
		edgeType = new URIImpl(type);
	}

	private final URI edgeType;

	@Override
	public void handleStatement(final Statement statement) throws RDFHandlerException {
		if (statement.getPredicate().equals(edgeType)) {
			try {
				final Resource subject = statement.getSubject();
				final Resource object = (Resource) statement.getObject();
				edges.put(subject, object);
			} catch (final IllegalStateException e) {
				// drop statement if id is not valid
			}
		}
	}

	private final Multimap<Resource, Resource> edges = ArrayListMultimap.create();

	public Multimap<Resource, Resource> getEdges() {
		return edges;
	}

}
