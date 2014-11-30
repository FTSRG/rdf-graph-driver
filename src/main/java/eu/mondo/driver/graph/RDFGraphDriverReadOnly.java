package eu.mondo.driver.graph;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface RDFGraphDriverReadOnly {

	/**
	 * Collect the vertices by given type.
	 * 
	 * * (( V )) --[ rdf:type ] -> [ typeURI ]
	 * 
	 * @param type
	 *            Vertices with this type URI are collected.
	 * @return List of the URIs of the vertices.
	 * @throws IOException
	 */
	public List<Long> collectVertex(String type) throws IOException;

	/**
	 * Collect edges with given URI.
	 * 
	 * (( S )) --[ edgeURI ] -> (( O ))
	 * 
	 * @param type
	 *            Vertex pairs connected with this type of edge are collected.
	 * @return Multimap of vertex pairs connected. ( S -> O )
	 * @throws IOException
	 */
	public Multimap<Long, Long> collectEdge(String type) throws IOException;

	/**
	 * Collect the vertices with given property.
	 * 
	 * (( V )) --[ propertyURI ] -> (( P ))
	 * 
	 * @param type
	 *            Vertices with type of property are collected.
	 * @return Map of vertices with their property. ( V -> P )
	 * @throws IOException
	 */
	public Map<Long, String> collectProperty(String type) throws IOException;

	/**
	 * Count the vertices of a given type.
	 * 
	 * @param type
	 *            Vertices with this type URI are counted.
	 * @return
	 * @throws IOException
	 */
	public long countVertex(String type) throws IOException;

	/**
	 * Count the edges of a given type.
	 * 
	 * @param type
	 *            Vertex pairs connected with this type of edge are counted.
	 * @return
	 * @throws IOException
	 */
	public long countEdge(String type) throws IOException;

	/**
	 * Count the properties of a given type.
	 * 
	 * @param type
	 *            Vertices with type of property are collected.
	 * @return
	 * @throws IOException
	 */
	public long countProperty(String type) throws IOException;
	
}