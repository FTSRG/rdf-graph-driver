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
package hu.bme.mit.bigmodel.rdf.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.IOUtils;

public class UnixUtils {
	
	public static String createTempFileFromScript(final String script) throws IOException, FileNotFoundException {
		return createTempFileFromResource(script, "sh", true);
	}
	
	public static String createTempFileFromResource(final String script, final String extension, final boolean executable) throws IOException, FileNotFoundException {
		final InputStream scriptInputStream = UnixUtils.class.getResourceAsStream("/" + script);
		
		// create a temporary file
		final File scriptTempFile = File.createTempFile("rdf-graph-driver-", extension);
        scriptTempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(scriptTempFile)) {
            IOUtils.copy(scriptInputStream, out);
        }
        scriptTempFile.setExecutable(executable);
		
		final String command = scriptTempFile.getAbsolutePath();
		return command;
	}
	
	public static void execResourceScript(final String command, final Map<String, String> environmentVariables) throws FileNotFoundException, IOException {
		String tempScript = UnixUtils.createTempFileFromScript(command);
		exec(tempScript, environmentVariables);
	}
	
	public static void execResourceScript(final String command, final String arguments, final Map<String, String> environmentVariables) throws FileNotFoundException, IOException {
		String tempScript = UnixUtils.createTempFileFromScript(command);
		exec(tempScript + " " + arguments, environmentVariables);		
	}
	
	public static void exec(final String command, final Map<String, String> environmentVariables) throws FileNotFoundException, IOException {
		final Map<?, ?> executionEnvironment = EnvironmentUtils.getProcEnvironment();
		for (Entry<String, String> environmentVariable : environmentVariables.entrySet()) {
			String keyAndValue = environmentVariable.getKey() + "=" + environmentVariable.getValue();
			EnvironmentUtils.addVariableToEnvironment(executionEnvironment, keyAndValue);
		}
		
		CommandLine cmdLine = CommandLine.parse(command);
		DefaultExecutor executor = new DefaultExecutor();
		// throws exception if exitcode != 0
		executor.execute(cmdLine, executionEnvironment);
	}
	
	public static Process run(final String command, final boolean showOutput, final Map<String, String> environment) throws IOException {
		return run(new String[] { command }, showOutput, environment);
	}

	public static Process run(final String[] command, final boolean showOutput, final Map<String, String> environment) throws IOException {
		final Process process = runProcess(command, showOutput, environment);
		final BufferedReader reader = UnixUtils.readProcessOutput(process);

		String line;
		while ((line = reader.readLine()) != null) {
			if (showOutput) {
				System.out.println(line);
			}
		}
		
		if (showOutput) {
			System.out.println("Command finished.");
		}
		
		return process;
	}

	public static Process runProcess(final String[] command, final boolean showOutput, final Map<String, String> environment) throws IOException {
		if (showOutput) {
			System.out.println("Invoking command:");
			for (final String string : command) {
				System.out.print(string + " ");
			}
			System.out.println();
			System.out.println("Command output:");
		}

		// passing command name and arguments as an array to the ProcessBuilder
		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.environment().putAll(environment);
		final Process process = builder.start();

		return process;
	}

	public static BufferedReader readProcessOutput(final Process process) {
		// hooking on the process' output stream
		final InputStream stdout = process.getInputStream();
		final InputStream stderr = process.getErrorStream();
		
		final InputStream std = new SequenceInputStream(stdout, stderr);

		final BufferedReader reader = new BufferedReader(new InputStreamReader(std));
		return reader;
	}
}
