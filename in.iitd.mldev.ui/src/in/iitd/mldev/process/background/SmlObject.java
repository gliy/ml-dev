package in.iitd.mldev.process.background;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class SmlObject {

	protected String name;
	protected SmlType type;

	public SmlObject(String name) {
		this(name, SmlType.UNKNOWN_TYPE);
	}

	public SmlObject(String name, String type) {
		this(name, SmlType.parseType(type));
	}

	public SmlObject(String name, SmlType type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public SmlType getType() {
		return type;
	}

	public final void visit(SmlVistor vistor) {

	}

	public static class SmlVistor {

	}

	public static class SmlType {
		public static final SmlType EXCEPTION_TYPE = new SmlType("exception");
		public static final SmlType FN_TYPE = new SmlType("function");
		public static final SmlType RECORD_TYPE = new SmlType("record");
		public static final SmlType TUPLE_TYPE = new SmlType("tuple");
		public static final SmlType CON_TYPE = new SmlType("con");
		public static final SmlType INT_TYPE = new SmlType("int");
		public static final SmlType REAL_TYPE = new SmlType("real");
		public static final SmlType STRING_TYPE = new SmlType("string");
		public static final SmlType UNKNOWN_TYPE = new SmlType("__unknown");

		private String name;
		private List<String> qualifier;
		private static final Set<SmlType> TYPE_REGISTRY = new HashSet<SmlObject.SmlType>(
				Arrays.asList(EXCEPTION_TYPE, FN_TYPE, RECORD_TYPE, TUPLE_TYPE,
						CON_TYPE, INT_TYPE, REAL_TYPE, STRING_TYPE));

		public SmlType(String name, String... qualifier) {
			this.name = name;
			this.qualifier = new ArrayList<String>();
			if (qualifier != null && qualifier.length > 0) {
				this.qualifier = Arrays.asList(qualifier);
			}
		}

		public static SmlType parseType(String newType) {
			for (SmlType type : TYPE_REGISTRY) {
				if (type.name.equals(newType)) {
					return type;
				}
			}
			SmlType type = new SmlType(newType);
			TYPE_REGISTRY.add(type);
			return type;
		}

		public static List<SmlType> parseTypes(Iterable<String> types) {
			List<SmlType> rtn = new ArrayList<SmlObject.SmlType>();
			if (types == null) {
				return rtn;
			}
			for (String smlType : types) {
				rtn.add(parseType(smlType.trim()));
			}
			return rtn;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result
					+ ((qualifier == null) ? 0 : qualifier.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null || obj.getClass() != SmlType.class) {
				return false;

			}
			SmlType type = (SmlType) obj;
			return type.name.equals(name);// TODO:include qualifier?
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static SmlObject parseObject(String obj) {
		return null;
	}

}
