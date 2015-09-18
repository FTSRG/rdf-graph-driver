package eu.mondo.driver.file;

import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class PropertyCollector extends RDFHandlerBase {

	public PropertyCollector(final String type) {
		propertyType = new URIImpl(type);
	}

	private final URI propertyType;

	@Override
	public void handleStatement(final Statement statement) throws RDFHandlerException {
		if (statement.getPredicate().equals(propertyType)) {
			final Resource subject = statement.getSubject();
			final Value object = statement.getObject();
			properties.put(subject, object);
		}
	}

	private final Multimap<Resource, Value> properties = ArrayListMultimap.create();

	public Multimap<Resource, Value> getProperties() {
		return properties;
	}

}
