package eu.mondo.driver.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverReadOnly;

/**
 * Driver for collecting the nodes.
 * @author szarnyasg
 *
 */
public class FileGraphDriverReadOnly implements RDFGraphDriverReadOnly {

	protected static final String RDF_BASE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String RDF_TYPE = RDF_BASE + "type";

	protected final Collection<Statement> statements = new HashSet<>();

	protected final String regex = ".*x(\\d+)";
	protected final Pattern pattern = Pattern.compile(regex);

	public FileGraphDriverReadOnly(final String connectionString) throws RDFParseException, RDFHandlerException, IOException {
		RDFFormat format = Rio.getParserFormatForFileName(connectionString);
		RDFParser parser = Rio.createParser(format);
		StatementCollector collector = new StatementCollector(statements);
		parser.setRDFHandler(collector);

		String filePath = connectionString.split("rdf://")[1];
		InputStream in = new FileInputStream(filePath);
		String baseURI = "";
		parser.parse(in, baseURI);
	}

	@Override
	public long countVertices(final String type) {
		return collectVertices(type).size();
	}

	@Override
	public long countEdges(String type) {
		return collectEdges(type).size();
	}

	@Override
	public long countProperties(String type) {
		return collectProperties(type).size();
	}

	@Override
	public List<Long> collectVertices(final String type) {
		List<Long> vertices = new ArrayList<>();

		URI rdfType = new URIImpl(RDF_TYPE);
		URI vertexType = new URIImpl(type);

		for (Statement statement : statements) {
			if (statement.getPredicate().equals(rdfType) && statement.getObject().equals(vertexType)) {
				Long id = extractId(statement.getSubject().toString());
				vertices.add(id);
			}
		}
		return vertices;
	}

	@Override
	public Multimap<Long, Long> collectEdges(final String type) {
		Multimap<Long, Long> edges = ArrayListMultimap.create();
		URI edgeType = new URIImpl(type);

		for (Statement statement : statements) {
			if (statement.getPredicate().equals(edgeType)) {
				Long v0 = extractId(statement.getSubject().toString());
				Long v1 = extractId(statement.getObject().toString());
				edges.put(v0, v1);
			}
		}
		return edges;
	}

	@Override
	public Multimap<Long, String> collectProperties(final String type) {
		Multimap<Long, String> properties = ArrayListMultimap.create();
		URI propertyType = new URIImpl(type);

		for (Statement statement : statements) {
			if (statement.getPredicate().equals(propertyType)) {
				Long v = extractId(statement.getSubject().toString());
				String s = statement.getObject().toString();
				properties.put(v, s);
			}
		}

		return properties;
	}

	protected Long extractId(final String string) {
		final Matcher matcher = pattern.matcher(string);
		if (matcher.matches()) {
			return new Long(matcher.group(1));
		} else {
			throw new IllegalStateException("No match found.");
		}
	}

}
