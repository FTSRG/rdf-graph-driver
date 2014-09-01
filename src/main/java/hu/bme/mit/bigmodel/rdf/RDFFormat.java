/*******************************************************************************
 * Copyright (c) 2010-2014, Daniel Stein, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Gabor Szarnyas
 *   Daniel Stein
 *******************************************************************************/

package hu.bme.mit.bigmodel.rdf;

public enum RDFFormat {
    RDFXML, TURTLE;

    public String getRdfFormatString() {
        return this.toString().toLowerCase();
    }
}
