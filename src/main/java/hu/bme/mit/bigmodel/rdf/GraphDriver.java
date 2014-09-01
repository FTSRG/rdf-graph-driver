/*******************************************************************************
 * Copyright (c) 2010-2014, Gabor Szarnyas, Daniel Stein, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Gabor Szarnyas - initial API and implementation
 * Daniel Stein - initial API and implementation
 *******************************************************************************/
package hu.bme.mit.bigmodel.rdf;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Multimap;

public interface GraphDriver {

	/**
	 * Insert a new vertex with the given type.
	 * 
	 * @param vertexType
	 *            the type of the new vertex.
	 * @param vertexURI
	 *            URI of the vertex to be added.
	 * @throws IOException
	 */
	public abstract void insertVertex(String vertexType, String vertexURI) throws IOException;

	/**
	 * Insert a new edge between the two vertices of a given edge type.
	 * 
	 * @param sourceVertexURI
	 *            URI of the source vertex.
	 * @param destinationVertexURI
	 *            URI of the destination vertex.
	 * @param edgeURI
	 *            URI of the connecting edge.
	 * @throws IOException
	 */
	public abstract void insertEdge(String sourceVertexURI, String destinationVertexURI, String edgeURI)
			throws IOException;

	/**
	 * Insert multiple edges between vertices of a given type.
	 * 
	 * @param edges
	 *            Multimap of the vertices to be connected.
	 * @param edgeURI
	 *            type URI of the edge connecting the vertices.
	 * @throws IOException
	 */
	public abstract void insertEdges(Multimap<String, String> edges, String edgeURI) throws IOException;

	/**
	 * Insert vertices and connect them to the given (already existing) vertex with the given edge type.
	 * 
	 * @param edges
	 *            Multimap of the vertices to be connected.
	 * @param edgeURI
	 *            type URI of the edge connecting the vertices.
	 * @param vertexTypeURI
	 *            type of the newly created vertices.
	 * @throws IOException
	 */
	public abstract void insertEdgesWithVertex(Multimap<String, String> edges, String edgeURI, String vertexTypeURI)
			throws IOException;

	/**
	 * Collect the vertices by given type.
	 * 
	 * (( V )) --[ rdf:type ] -> [ typeURI ]
	 * 
	 * @param typeURI
	 *            type of the vertices to be collected.
	 * @return List of the URIs of the vertices.
	 * @throws IOException
	 */
	public abstract List<String> collectVertices(String typeURI) throws IOException;

	/**
	 * Collect the vertices with given property.
	 * 
	 * (( V )) --[ propertyURI ] -> (( P ))
	 * 
	 * @param propertyURI
	 *            vertices with this type of property to be collected.
	 * @return Map of vertices with their property. ( V -> P )
	 * @throws IOException
	 */
	public abstract Map<String, String> collectVerticesWithProperty(String propertyURI) throws IOException;

	/**
	 * Collect edges with given URI.
	 * 
	 * (( S )) --[ edgeURI ] -> (( O ))
	 * 
	 * @param edgeURI
	 *            vertex pairs connected with this type of edge to be collected.
	 * @return Multimap of vertex pairs connected. ( S -> O )
	 * @throws IOException
	 */
	public abstract Multimap<String, String> collectEdges(String edgeURI) throws IOException;

	/**
	 * Collect vertices from the given type and the associated property values of the given type.
	 * 
	 * [ vertexTypeURI ] <- [ rdf:type ] -- (( V )) -- [ edgeURI ] -> (( P ))
	 * 
	 * @param vertexTypeURI
	 *            type URI of the vertices to check.
	 * @param edgeURI
	 *            type URI of the edges to collect.
	 * @return Multimap of the vertices and the connecting property values. (V -> P)
	 * @throws IOException
	 */
	public abstract Multimap<String, String> collectOutgoingEdges(String vertexTypeURI, String edgeURI)
			throws IOException;

	/**
	 * Update the selected property of the given vertex. If the property does not exist, a new one is created. If
	 * multiple occurrences are found, all of them are updated.
	 * 
	 * @param vertexURI
	 *            URI of the vertex to be updated
	 * @param propertyURI
	 *            the property URI to be modified
	 * @param value
	 *            the value to update to
	 * @throws IOException
	 */
	public abstract void updateProperty(String vertexURI, String propertyURI, String value) throws IOException;

	/**
	 * Update the selected property of the given vertices. If the property does not exist, a new one is created. If
	 * multiple occurrences are found, all of them are updated.
	 * 
	 * @param vertexURIAndPropertyValues
	 *            Map of the vertex URIs and the value to modify the property to
	 * @param propertyURI
	 *            the property URI to be modified
	 * @throws IOException
	 */
	public abstract void updateProperties(Map<String, String> vertexURIAndPropertyValues, String propertyURI)
			throws IOException;

	/**
	 * Delete the vertices and every connection/property associated with them.
	 * 
	 * @param vertexURIs
	 *            Collection of the vertices to be deleted.
	 * @throws IOException
	 */
	public abstract void deleteVertices(Collection<String> vertexURIs) throws IOException;

	/**
	 * Delete the vertex and every connection/property associated with it.
	 * 
	 * @param vertexURI
	 *            The URI of the vertex to be deleted.
	 * @throws IOException
	 */
	public abstract void deleteVertex(String vertexURI) throws IOException;

	/**
	 * Delete edge between the given vertex pairs with given URI.
	 * 
	 * @param vertexURIs
	 *            Multimap of URIs connected.
	 * @param edgeURI
	 *            URI of the connecting edge.
	 * @throws IOException
	 */
	public abstract void deleteEdges(Multimap<String, String> vertexURIs, String edgeURI) throws IOException;

	/**
	 * Delete edge between the two vertices with given URI.
	 * 
	 * @param sourceVertexURI
	 *            URI of the source vertex.
	 * @param destinationVertexURI
	 *            URI of the destination vertex.
	 * @param edgeURI
	 *            URI of the connecting edge.
	 * @throws IOException
	 */
	public abstract void deleteEdge(String sourceVertexURI, String destinationVertexURI, String edgeURI)
			throws IOException;

}