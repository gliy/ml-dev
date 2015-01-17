package in.iitd.mldev.process.background.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public abstract class SmlParseAction {

	private List<Pattern> patterns;

	protected SmlParseAction(String... patterns) {
		this.patterns = new ArrayList<Pattern>();
		for (String pattern : patterns) {
			this.patterns.add(Pattern.compile(pattern));
		}
	}

	public List<Pattern> getPatterns() {
		return patterns;
	}

	public void parseAction(SmlOutputParser parser, String ... data) {
		parse(parser, data);
		
	}
	protected abstract void parse(SmlOutputParser parser, String... data);
}
