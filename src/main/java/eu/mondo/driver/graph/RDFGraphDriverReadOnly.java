package eu.mondo.driver.graph;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverReadOnly {

	public long countVertex(String type) throws IOException;

	public long countEdge(String type) throws IOException;

	public long countProperty(String type) throws IOException;

	public List<Long> collectVertex(String type) throws IOException;

	public Multimap<Long, Long> collectEdge(String type) throws IOException;

	public Map<Long, String> collectProperty(String type) throws IOException;

}