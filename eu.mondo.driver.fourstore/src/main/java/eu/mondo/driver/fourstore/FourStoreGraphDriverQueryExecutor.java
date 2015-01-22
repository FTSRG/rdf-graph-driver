package eu.mondo.driver.fourstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import eu.mondo.utils.UnixUtils;

public class FourStoreGraphDriverQueryExecutor extends FourStoreGraphDriverLoader {
	
	public FourStoreGraphDriverQueryExecutor(String connectionString) {
		super(connectionString);
	}

	public BufferedReader runQuery(final String query) throws IOException {
		final String command = String.format("4s-query $FOURSTORE_CLUSTER_NAME -f text -s -1 '%s'", query);
		if (showCommands) {
			System.out.println(command);
		}
			
		final BufferedReader reader = UnixUtils.exec(command, environment);
		return reader;
	}

	public List<Long> queryIds(final String query) throws IOException {
		final BufferedReader reader = runQuery(query);

		// example: <http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#x87947>
		final String regex = "<.*x(\\d+)>";
		final Pattern pattern = Pattern.compile(regex);

		final List<Long> results = new ArrayList<>();

		String line;
		while ((line = reader.readLine()) != null) {
			final Matcher matcher = pattern.matcher(line);
			if (matcher.matches()) {
				final Long id = new Long(matcher.group(1));
				results.add(id);
			}
		}
		return results;
	}
	
}
