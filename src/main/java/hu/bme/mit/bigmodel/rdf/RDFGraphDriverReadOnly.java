package hu.bme.mit.bigmodel.rdf;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverReadOnly {

	public long countVertex(String type);

	public long countEdge(String type);

	public long countProperty(String type);

	public List<Long> collectVertex(String type);

	public Multimap<Long, Long> collectEdge(String type);

	public Map<Long, String> collectProperty(String type);

}