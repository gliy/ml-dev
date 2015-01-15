package in.iitd.mldev.launch.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmlFunction extends SmlObject {

	private String name;
	private List<String> args;

	public SmlFunction(String name, String... types) {
		super();
		this.name = name;
		this.args = new ArrayList<String>();
		if (types != null && types.length > 0) {
			this.args = Arrays.asList(types);
		}
	}

	public String getName() {
		return name;
	}

	public List<String> getArgs() {
		return args;
	}

	@Override
	public String toString() {
		return name + " : " + params();
	}

	public String params() {
		if (args.isEmpty()) {
			return "";
		}
		StringBuilder bu = new StringBuilder(args.get(0));
		for (int i = 1; i < args.size(); i++) {
			bu.append(" -> ").append(args.get(i));
		}
		return bu.toString();

	}

	@Override
	public SmlType getType() {
		return SmlType.FN_TYPE;
	}
}
