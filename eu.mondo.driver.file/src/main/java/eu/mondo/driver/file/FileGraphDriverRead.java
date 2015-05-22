package eu.mondo.driver.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Literal;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverRead;
import eu.mondo.driver.graph.util.LiteralParser;

public class FileGraphDriverRead implements RDFGraphDriverRead {

    protected static final String RDF_BASE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    protected static final String RDF_TYPE = RDF_BASE + "type";
    protected static final String ID_PREFIX = "_";

    protected final Collection<Statement> statements = new HashSet<>();

    protected final String regex = ".*" + ID_PREFIX + "(\\d+)";
    protected final Pattern pattern = Pattern.compile(regex);

    public FileGraphDriverRead(final String urlString) throws RDFParseException, RDFHandlerException, IOException {
        final RDFFormat format = Rio.getParserFormatForFileName(urlString);
        final RDFParser parser = Rio.createParser(format);
        final StatementCollector collector = new StatementCollector(statements);
        parser.setRDFHandler(collector);

        final URL url = new URL(urlString);
        final InputStream in = url.openStream();
        final String baseURI = "";
        parser.parse(in, baseURI);
        in.close();
    }

    @Override
    public long countVertices(final String type) {
        return collectVertices(type).size();
    }

    @Override
    public long countEdges(final String type) {
        return collectEdges(type).size();
    }

    @Override
    public long countProperties(final String type) {
        return collectProperties(type).size();
    }

    @Override
    public List<Long> collectVertices(final String type) {
        final List<Long> vertices = new ArrayList<>();

        final URI rdfType = new URIImpl(RDF_TYPE);
        final URI vertexType = new URIImpl(type);

        for (final Statement statement : statements) {
            if (statement.getPredicate().equals(rdfType) && statement.getObject().equals(vertexType)) {
                final Long id = extractId(statement.getSubject().toString());
                vertices.add(id);
            }
        }
        return vertices;
    }

    @Override
    public Multimap<Long, Long> collectEdges(final String type) {
        final Multimap<Long, Long> edges = ArrayListMultimap.create();
        final URI edgeType = new URIImpl(type);

        for (final Statement statement : statements) {
            if (statement.getPredicate().equals(edgeType)) {
                try {
                    final Long subjectId = extractId(statement.getSubject().toString());
                    final Long objectId = extractId(statement.getObject().toString());
                    edges.put(subjectId, objectId);
                } catch (final IllegalStateException e) {
                    // drop statement if id is not valid
                }
            }
        }
        return edges;
    }

    @Override
    public Multimap<Long, Object> collectProperties(final String type) {
        final Multimap<Long, Object> properties = ArrayListMultimap.create();
        final URI propertyType = new URIImpl(type);

        for (final Statement statement : statements) {
            if (statement.getPredicate().equals(propertyType)) {

                final Long id = extractId(statement.getSubject().toString());
                final Value value = statement.getObject();
                if (value instanceof Literal) {
                    final Literal literal = (Literal) value;
                    final Object object = LiteralParser.literalToObject(literal);
                    properties.put(id, object);
                }
            }
        }

        return properties;
    }

    protected Long extractId(final String string) {
        final Matcher matcher = pattern.matcher(string);
        if (matcher.matches()) {
            return new Long(matcher.group(1));
        } else {
            throw new IllegalStateException("No match found for ID pattern "+pattern+" in URL "+string);
        }
    }

    @Override
    public List<String> getVertexTypes() throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getEdgeTypes() throws IOException {
        throw new UnsupportedOperationException();
    }

}
