package eu.mondo.driver.file;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdExtractor {

	public static Long extractId(final String string) {
	    final Matcher matcher = IdExtractor.PATTERN.matcher(string);
	    if (matcher.matches()) {
	        return new Long(matcher.group(1));
	    } else {
	        throw new IllegalStateException("No match found for ID pattern "+IdExtractor.PATTERN+" in URL "+string);
	    }
	}

	private static final String ID_PREFIX = "_";
	private static final String REGEX = ".*" + ID_PREFIX + "(\\d+)";
	private static final Pattern PATTERN = Pattern.compile(REGEX);

}
