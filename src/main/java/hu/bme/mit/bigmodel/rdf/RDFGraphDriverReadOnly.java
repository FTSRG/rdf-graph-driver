package hu.bme.mit.bigmodel.rdf;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverReadOnly {

	public abstract long countVertex(String type);

	public abstract long countEdge(String type);

	public abstract long countProperty(String type);

	public abstract List<Long> collectVertex(String type);

	public abstract Multimap<Long, Long> collectEdge(String type);

	public abstract Map<Long, String> collectProperty(String propertyName);

}