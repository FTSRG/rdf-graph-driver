package eu.mondo.driver.fourstore;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResultHandlerException;
import org.openrdf.query.resultio.QueryResultParseException;
import org.openrdf.query.resultio.text.tsv.SPARQLResultsTSVParser;

public class Main {

	public static void main(final String[] args) throws TupleQueryResultHandlerException, QueryResultParseException, IOException {
		final SPARQLResultsTSVParser parser = new SPARQLResultsTSVParser();
		final InputStream is = new FileInputStream("/home/szarnyasg/results.tsv");
		final BindingSetCollector resultHandler = new BindingSetCollector();
		parser.setQueryResultHandler(resultHandler);
		parser.parse(is);

		final Collection<BindingSet> bindingSets = resultHandler.getBindingSets();
		for (final BindingSet bindingSet : bindingSets) {
			System.out.println(bindingSet);
		}
	}
}
