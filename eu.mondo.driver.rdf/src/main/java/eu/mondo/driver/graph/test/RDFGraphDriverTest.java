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

	protected static final String BASE_URI = "http://www.semanticweb.org/ontologies/2015/trainbenchmark#";
	protected static final String ID_PREFIX = "_";

	// vertices
	protected static final String TRACKELEMENT = BASE_URI + "TrackElement";
	protected static final String SEGMENT = BASE_URI + "Segment";
	protected static final String SWITCH = BASE_URI + "Switch";

	// attributes
	protected static final String LENGTH = BASE_URI + "length";
	protected static final String CURRENTPOSITION = BASE_URI + "currentPosition";

	// properties
	protected static final String CONNECTSTO = BASE_URI + "connectsTo";

	// counts
	protected static final int CONNECTSTO_COUNT = 1;
	protected static final int TRACKELEMENT_COUNT = 2;
	protected static final int SEGMENT_COUNT = 1;
	protected static final int SWITCH_COUNT = 1;
	protected static final int LENGTH_COUNT = 1;
	protected static final int CURRENTPOSITION_COUNT = 1;

}
