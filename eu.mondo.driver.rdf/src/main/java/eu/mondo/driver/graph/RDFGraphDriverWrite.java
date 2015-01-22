package eu.mondo.driver.graph;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverWrite {

	// inserts/deletes single triple: ( sourceURI ) --[ type ]--> ( targetURI )
	public void insertEdge(String sourceURI, String targetURI, String type) throws IOException;

	public void deleteEdge(String sourceURI, String targetURI, String type) throws IOException;

	// inserts/deletes multiple triples: ( sourceURI ) --[ type ]--> ( targetURI )
	public void insertEdges(Multimap<String, String> edges, String type) throws IOException;

	public void deleteEdges(Multimap<String, String> edges, String type) throws IOException;

	// inserts/deletes single triple: ( uri ) --[ rdf:type ]--> ( type )
	public void insertVertex(String uri, String type) throws IOException;

	public void deleteVertex(String uri) throws IOException;

	// inserts/deletes multiple triples: ( uri ) --[ rdf:type ]--> ( type )
	public void insertVertices(List<String> uris, String type) throws IOException;

	public void deleteVertices(List<String> uri) throws IOException;

	// deletes ( vertexURI ) --[ type ]--> ( * )
	// inserts ( vertexURI ) --[ type ]--> ( value )
	public void updateProperty(String vertexURI, String type, Object value) throws IOException;

	// runs updateProperty for multiple vertices
	public void updateProperties(Map<String, Object> properties, String type) throws IOException;

	public void insertEdgeWithVertex(String sourceURI, String targetURI, String edgeType, String targetVertexType) throws IOException;

	public void insertEdgesWithVertex(Multimap<String, String> edges, String edgeType, String targetVertexType) throws IOException;

}