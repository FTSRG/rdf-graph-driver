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

		final String fourStoreShowCommands = System.getenv("FOURSTORE_SHOW_COMMANDS");
		if ("true".equals(fourStoreShowCommands)) {
			showCommands = true;
		}
		final String fourStoreShowCommandOutput = System.getenv("FOURSTORE_SHOW_COMMAND_OUTOUT");
		if ("true".equals(fourStoreShowCommandOutput)) {
			showCommandOutput = true;
		}

		environment = ImmutableMap.of("FOURSTORE_CLUSTER_NAME", clusterName);
	}

	public void start() throws FileNotFoundException, IOException {
		UnixUtils.execResourceScript("4s-start.sh", environment, showCommands, showCommandOutput);
	}

	public void stop() throws FileNotFoundException, IOException {
		UnixUtils.execResourceScript("4s-stop.sh", environment, showCommands, showCommandOutput);
	}

	public void load(final String modelPath) throws IOException, InterruptedException {
		final File modelFile = new File(modelPath);
		load(modelFile);
	}

	public void load(final File modelFile) throws IOException, InterruptedException {
		if (!modelFile.exists()) {
			throw new FileNotFoundException(modelFile.getAbsolutePath());
		}

		UnixUtils.execResourceScript("4s-import.sh", modelFile.getAbsolutePath(), environment, showCommands, showCommandOutput);
	}

	public void setShowCommandOutput(final boolean showCommandOutput) {
		this.showCommandOutput = showCommandOutput;
	}

}
