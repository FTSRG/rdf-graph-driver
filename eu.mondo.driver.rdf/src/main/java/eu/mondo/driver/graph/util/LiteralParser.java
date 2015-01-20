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
package eu.mondo.driver.graph.util;

import info.aduna.text.StringUtil;

import org.openrdf.model.Literal;
import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.ValueFactoryImpl;

/**
 * Based on Sesame's org.openrdf.query.resultio.text.ts.SPARQLResultsTSVParser
 */
public class LiteralParser {

	protected static ValueFactory valueFactory = ValueFactoryImpl.getInstance();
	
	/**
	 * Parses a literal, creates an object for it and returns this object.
	 * 
	 * @param literal
	 *        The literal to parse.
	 * @return An object representing the parsed literal.
	 * @throws IllegalArgumentException
	 *         If the supplied literal could not be parsed correctly.
	 */
	public static Literal parseLiteral(String literal)
		throws IllegalArgumentException
	{
		if (literal.startsWith("\"")) {
			// Find string separation points
			int endLabelIdx = findEndOfLabel(literal);

			if (endLabelIdx != -1) {
				int startLangIdx = literal.indexOf("@", endLabelIdx);
				int startDtIdx = literal.indexOf("^^", endLabelIdx);

				if (startLangIdx != -1 && startDtIdx != -1) {
					throw new IllegalArgumentException("Literals can not have both a language and a datatype");
				}

				// Get label
				String label = literal.substring(1, endLabelIdx);
				label = unescapeString(label);

				if (startLangIdx != -1) {
					// Get language
					String language = literal.substring(startLangIdx + 1);
					return valueFactory.createLiteral(label, language);
				}
				else if (startDtIdx != -1) {
					// Get datatype
					String datatype = literal.substring(startDtIdx + 2);
					datatype = datatype.substring(1, datatype.length() - 1);
					URI dtURI = valueFactory.createURI(datatype);
					return valueFactory.createLiteral(label, dtURI);
				}
				else {
					return valueFactory.createLiteral(label);
				}
			}
		}

		throw new IllegalArgumentException("Not a legal literal: " + literal);
	}

	/**
	 * Finds the end of the label in a literal string.
	 * 
	 * @return The index of the double quote ending the label.
	 */
	private static int findEndOfLabel(String literal) {
		// we just look for the last occurrence of a double quote
		return literal.lastIndexOf("\"");
	}

	private static String unescapeString(String s) {
		s = StringUtil.gsub("\\", "", s);
		return s;
	}
	
	public static Object literalToObject(Literal literal) {
		switch (literal.getDatatype().toString()) {
		case "http://www.w3.org/2001/XMLSchema#int":
			return literal.intValue();
		default:
			return null;
		}
	}
	
	public static Object stringToObject(String string) {
		Literal literal = parseLiteral(string);
		return literalToObject(literal);		
	}
	
}
