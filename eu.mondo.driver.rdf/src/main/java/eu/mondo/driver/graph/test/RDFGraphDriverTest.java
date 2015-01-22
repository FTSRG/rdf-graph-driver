/*******************************************************************************
 * Copyright (c) 2010-2015, Benedek Izso, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Benedek Izso - initial API and implementation
 *   Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package eu.mondo.driver.graph.test;

public abstract class RDFGraphDriverTest {

	protected static final String BASE_URI = "http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#";

	protected static final String SEGMENT = BASE_URI + "Segment";
	protected static final String SEGMENT_LENGTH = BASE_URI + "Segment_length";
	protected static final String TRACKELEMENT_SENSOR = BASE_URI + "TrackElement_sensor";
	protected static final String ROUTE_ROUTEDEFINITION = BASE_URI + "Route_routeDefinition";
	protected static final String ROUTE_ENTRY = BASE_URI + "Route_entry";
	protected static final String SWITCH = BASE_URI + "Switch";
	
}
