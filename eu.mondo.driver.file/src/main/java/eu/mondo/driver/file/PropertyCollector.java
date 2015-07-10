package eu.mondo.driver.file;

import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.helpers.RDFHandlerBase;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.util.LiteralParser;

public class PropertyCollector extends RDFHandlerBase {

    public PropertyCollector(String type) {
    	propertyType = new URIImpl(type);
	}
    
    private final URI propertyType;

    @Override
    public void handleStatement(Statement statement) throws RDFHandlerException {
    	if (statement.getPredicate().equals(propertyType)) {
            final Long id = IdExtractor.extractId(statement.getSubject().toString());
            final Value value = statement.getObject();
            if (value instanceof Literal) {
                final Literal literal = (Literal) value;
                final Object object = LiteralParser.literalToObject(literal);
                properties.put(id, object);
            }
        }
    }
    
    private final Multimap<Long, Object> properties = ArrayListMultimap.create();

    public Multimap<Long, Object> getProperties() {
		return properties;
	}

}
