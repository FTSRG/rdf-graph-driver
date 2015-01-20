package eu.mondo.driver.graph;

import java.io.IOException;
import java.util.Collection;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverWrite {

	// inserts/deletes single triple: ( ...#sourceId ) --[ type ]--> ( ...#targetId )
	public void insertEdge(long sourceId, long targetId, String type) throws IOException;
	public void deleteEdge(long sourceId, long targetId, String type) throws IOException;

	// inserts/deletes multiple triples: ( ...#sourceId ) --[ type ]--> ( ...#targetId )
	public void insertEdges(Multimap<String, String> edges, String type) throws IOException;
	public void deleteEdges(Multimap<String, String> edges, String type) throws IOException;

	// inserts/deletes single triple: ( ...#id ) --[ rdf:type ]--> ( type )
	public long insertVertex(long id, String type) throws IOException;
	public void deleteVertex(long id) throws IOException;
	
	// inserts/deletes multiple triples: ( ...#id ) --[ rdf:type ]--> ( type )
	public long insertVertices(long id, String type) throws IOException;
	public void deleteVertices(Collection<String> vertices) throws IOException;

	// deletes ( vertex ) --[ property ]--> ( * )
	// inserts ( vertex ) --[ property ]--> ( value )
	public void updateProperty(String vertex, String property, String value) throws IOException;
	
	// runs updateProperty for multiple vertices
	public void updateProperties(Multimap<String, Object> properties, String type) throws IOException;


	// public void insertEdgesWithVertex(Multimap<String, String> edges, String
	// edgeType, String vertexType) throws IOException;

	
}