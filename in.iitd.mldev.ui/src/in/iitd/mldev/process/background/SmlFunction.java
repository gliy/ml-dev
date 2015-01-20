package in.iitd.mldev.process.background;

import java.util.ArrayList;
import java.util.List;

public class SmlFunction extends SmlObject {

	private List<SmlType> args;

	public SmlFunction(String name, String... types) {
		super(name);
		this.name = name;
		this.args = new ArrayList<SmlType>();
		if (types != null && types.length > 0) {
			for (String type : types) {
				args.add(SmlType.parseType(type));
			}
		}
	}


	public List<SmlType> getArgs() {
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
		StringBuilder bu = new StringBuilder(args.get(0).toString());
		for (int i = 1; i < args.size(); i++) {
			bu.append(" -> ").append(args.get(i));
		}
		return bu.toString();

	}

}
