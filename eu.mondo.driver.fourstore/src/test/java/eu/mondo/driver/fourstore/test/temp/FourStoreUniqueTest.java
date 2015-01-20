package eu.mondo.driver.fourstore.test.temp;
///*******************************************************************************
// * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
// * All rights reserved. This program and the accompanying materials
// * are made available under the terms of the Eclipse Public License v1.0
// * which accompanies this distribution, and is available at
// * http://www.eclipse.org/legal/epl-v10.html
// *
// * Contributors:
// * Gabor Szarnyas - initial API and implementation
// *******************************************************************************/
//package hu.bme.mit.bigmodel.fourstore.test;
//
//import hu.bme.mit.bigmodel.fourstore.FourStoreDriverUnique;
//
//import java.io.IOException;
//import java.math.Long;
//import java.util.ArrayList;
//
//import org.junit.Before;
//import org.junit.Test;
//
//import com.google.common.collect.Multimap;
//
//public class FourStoreUniqueTest {
//
//	protected FourStoreDriverUnique driver;
//
//	@Before
//	public void init() {
//		driver = new FourStoreDriverUnique("trainbenchmark_cluster");
//	}
//
//	@Test
//	public void testUniques() throws IOException {
//		driver.generateUniques();
//		ArrayList<String> edgeTypes = driver.getEdgeTypes();
//		for (String edgeType : edgeTypes) {
//			Multimap<Long, Long> edges = driver.collectEdges(edgeType);
//		}
//	}
//
//}
