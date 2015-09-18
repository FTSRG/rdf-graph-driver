package eu.mondo.driver.fourstore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.QueryResultParseException;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVParser;

import eu.mondo.utils.UnixUtils;

public class FourStoreGraphDriverQueryExecutor extends FourStoreGraphDriverLoader {

	protected final String ID_PREFIX = "_";

	protected final SPARQLResultsTSVParser parser = new SPARQLResultsTSVParser();

	public FourStoreGraphDriverQueryExecutor(final String connectionString) {
		super(connectionString);
	}

	public Collection<BindingSet> runQuery(final String queryDefinition) throws IOException {
		final String command = String.format("4s-query $FOURSTORE_CLUSTER_NAME -f text -s -1 '%s'", queryDefinition);
		if (showCommands) {
			System.out.println(command);
		}

		final BindingSetCollector bindingSetCollector = new BindingSetCollector();
		parser.setQueryResultHandler(bindingSetCollector);

		final InputStream is = UnixUtils.execToStream(command, environment);
		try {
			parser.parse(is);
		} catch (TupleQueryResultHandlerException | QueryResultParseException e) {
			throw new IOException(e);
		}
		final Collection<BindingSet> bindingSets = bindingSetCollector.getBindingSets();

		return bindingSets;
	}

	// public List<Long> queryIds(final String query) throws IOException {
	// final Collection<BindingSet> bindingSets = runQuery(query);

	// System.out.println(bindingSets);
	// // example: <http://www.semanticweb.org/ontologies/2011/1/TrainRequirementOntology.owl#_87947>
	// final String regex = "<.*#" + ID_PREFIX + "(\\d+)>";
	// final Pattern pattern = Pattern.compile(regex);
	//
	// final List<Long> results = new ArrayList<>();
	//
	// String line;
	// while ((line = reader.readLine()) != null) {
	// final Matcher matcher = pattern.matcher(line);
	// if (matcher.matches()) {
	// final Long id = new Long(matcher.group(1));
	// results.add(id);
	// }
	// }
	// return null;
	// }

}
