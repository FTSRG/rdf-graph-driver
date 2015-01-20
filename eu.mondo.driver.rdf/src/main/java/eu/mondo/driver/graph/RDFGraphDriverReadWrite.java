package eu.mondo.driver.graph;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverReadWrite {

	// C
	public void insertEdgesWithVertex(Multimap<Long, Long> edgesChunk, String trackelementSensor, String sensor);

	// U
	public void updateProperties(Map<Long, Integer> properties, String type);

	// D
	public void deleteEdges(Multimap<Long, Long> edges, String type);
	public void deleteVertices(List<Long> vertices);
	
}