package eu.mondo.driver.rdf;
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

import org.junit.Assert;
import org.junit.Test;

import eu.mondo.driver.graph.util.RDFUtil;

public class RDFUtilTest {
	
	@Test
	public void testLiteral() {
		final String literal = RDFUtil.toLiteral("42");
		
		final String expectedLiteral = "\"42\"^^<http://www.w3.org/2001/XMLSchema#int>";
		Assert.assertEquals(expectedLiteral, literal);
	}
	
}
