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

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openrdf.model.Literal;

import eu.mondo.driver.graph.util.LiteralParser;

public class LiteralParserTest {
	
	@Test
	public void testInt() {
		String string = "\"653\"^^<http://www.w3.org/2001/XMLSchema#int>";
		Literal literal = LiteralParser.parseLiteral(string);
		int intValue = literal.intValue();

		assertEquals(653, intValue);
	}
	
	@Test
	public void testObject() {
		String string = "\"653\"^^<http://www.w3.org/2001/XMLSchema#int>";
		Object object = LiteralParser.stringToObject(string);

		assertEquals(653, object);
	}
	
}
