package eu.mondo.driver.file;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandler;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;

import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Multimap;

import eu.mondo.driver.graph.RDFGraphDriverRead;

public class FileGraphDriverRead implements RDFGraphDriverRead {

	private final URL url;
	private final RDFFormat format;

	public FileGraphDriverRead(final String urlString) {
		try {
			url = new URL(urlString);
			format = Rio.getParserFormatForFileName(urlString);
		} catch (final MalformedURLException e) {
			throw Throwables.propagate(e);
		}
	}

	private void parse(final RDFHandler handler) throws IOException, RDFParseException, RDFHandlerException {
		final RDFParser parser = Rio.createParser(format);
		parser.setRDFHandler(handler);
		final InputStream in = url.openStream();
		parser.parse(in, "");
		in.close();
	}

	// Vertices

	@Override
	public List<Resource> collectVertices(final String type) {
		return vertexCache.getUnchecked(type);
	}

	private final LoadingCache<String, List<Resource>> vertexCache = CacheBuilder.newBuilder()
			.build(new CacheLoader<String, List<Resource>>() {
				@Override
				public List<Resource> load(final String type) throws Exception {
					final VertexCollector collector = new VertexCollector(type);
					parse(collector);
					return collector.getVertices();
				}
			});

	@Override
	public long countVertices(final String type) {
		return collectVertices(type).size();
	}

	@Override
	public List<String> getVertexTypes() throws IOException {
		throw new UnsupportedOperationException();
	}

	// Edges

	@Override
	public Multimap<Resource, Resource> collectEdges(final String type) {
		return edgeCache.getUnchecked(type);
	}

	private final LoadingCache<String, Multimap<Resource, Resource>> edgeCache = CacheBuilder.newBuilder()
			.build(new CacheLoader<String, Multimap<Resource, Resource>>() {
				@Override
				public Multimap<Resource, Resource> load(final String type) throws Exception {
					final EdgeCollector collector = new EdgeCollector(type);
					parse(collector);
					return collector.getEdges();
				}
			});

	@Override
	public long countEdges(final String type) {
		return collectEdges(type).size();
	}

	@Override
	public List<String> getEdgeTypes() throws IOException {
		throw new UnsupportedOperationException();
	}

	// Properties

	@Override
	public Multimap<Resource, Value> collectProperties(final String type) {
		return propertyCache.getUnchecked(type);
	}

	private final LoadingCache<String, Multimap<Resource, Value>> propertyCache = CacheBuilder.newBuilder()
			.build(new CacheLoader<String, Multimap<Resource, Value>>() {
				@Override
				public Multimap<Resource, Value> load(final String type) throws Exception {
					final PropertyCollector collector = new PropertyCollector(type);
					parse(collector);
					return collector.getProperties();
				}
			});

	@Override
	public long countProperties(final String type) {
		return collectProperties(type).size();
	}

}
