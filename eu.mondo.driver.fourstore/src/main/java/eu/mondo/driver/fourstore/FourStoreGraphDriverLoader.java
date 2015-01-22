/*******************************************************************************
 * Copyright (c) 2010-2014, Gabor Szarnyas, Istvan Rath and Daniel Varro
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Gabor Szarnyas - initial API and implementation
 *******************************************************************************/
package eu.mondo.driver.fourstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import eu.mondo.utils.UnixUtils;

public class FourStoreGraphDriverLoader {

	protected boolean showCommands = false;
	protected boolean showCommandOutput = false;
	protected final Map<String, String> environment;
	
	public FourStoreGraphDriverLoader(final String connectionString) {
		final String clusterName = connectionString;
		environment = ImmutableMap.of("FOURSTORE_CLUSTER_NAME", clusterName);
	}
	
	public void start() throws FileNotFoundException, IOException {
		UnixUtils.execResourceScript("4s-start.sh", environment, showCommandOutput);
	}
	
	public void stop() throws FileNotFoundException, IOException {
		UnixUtils.execResourceScript("4s-stop.sh", environment, showCommandOutput);
	}
	
	public void load(final String modelPath) throws IOException, InterruptedException {
		final File modelFile = new File(modelPath);
		if (!modelFile.exists()) {
			throw new FileNotFoundException(modelPath);
		}

		UnixUtils.execResourceScript("4s-import.sh", modelFile.getAbsolutePath(), environment, showCommandOutput);
	}
	
	public void setShowCommandOutput(final boolean showCommandOutput) {
		this.showCommandOutput = showCommandOutput;
	}
	
}
