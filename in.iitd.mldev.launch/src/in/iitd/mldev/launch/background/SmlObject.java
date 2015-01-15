package in.iitd.mldev.launch.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SmlObject {

	public abstract SmlType getType();

	public final void visit(SmlVistor vistor) {

	}

	public static class SmlVistor {

	}

	public static class SmlType {

		private String name;
		private List<String> qualifier;

		public SmlType(String name, String... qualifier) {
			this.name = name;
			this.qualifier = new ArrayList<String>();
			if (qualifier != null && qualifier.length > 0) {
				this.qualifier = Arrays.asList(qualifier);
			}
		}

		public static final SmlType EXCEPTION_TYPE = new SmlType("exception");
		public static final SmlType FN_TYPE = new SmlType("function");
		public static final SmlType RECORD_TYPE = new SmlType("record");
		public static final SmlType TUPLE_TYPE = new SmlType("tuple");
		public static final SmlType CON_TYPE = new SmlType("con");
		public static final SmlType VAL_TYPE = new SmlType("val");
	}
	
	public static SmlObject parseObject(String obj) {
		return null;
	}

}
