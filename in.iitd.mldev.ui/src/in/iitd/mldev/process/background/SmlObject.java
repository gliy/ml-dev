package in.iitd.mldev.process.background;

import in.iitd.mldev.core.model.SmlBinding;
import in.iitd.mldev.ui.SmlUiPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

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

	public final SmlType getType() {
		return type;
	}

	public final void visit(SmlVistor vistor) {

	}

	public static class SmlVistor {

	}

	@Override
	public boolean equals(Object o) {
		if(o == null || !(o instanceof SmlObject)) {
			return false;
		}
		SmlObject so = (SmlObject)o;
		return name.equals(so.name);
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
		public static final SmlType LIST_TYPE = new SmlType("list");

		private String name;
		private List<String> qualifier;
		private static final Set<SmlType> TYPE_REGISTRY = new HashSet<SmlObject.SmlType>(
				Arrays.asList(EXCEPTION_TYPE, FN_TYPE, RECORD_TYPE, TUPLE_TYPE,
						CON_TYPE, INT_TYPE, REAL_TYPE, STRING_TYPE, LIST_TYPE));
		private static final Map<String, Image> typeToImage = new HashMap<String, Image>();

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

		public static List<SmlType> parseTypes(String... types) {
			if (types == null) {
				return new ArrayList<SmlObject.SmlType>();

			}
			return parseTypes(Arrays.asList(types));
		}

		public Image getImage() {
			return getImage(this);
		}
		
		private Image getImage(SmlType element) {
			//TODO:FIX
			if(1==1)return null;
			Image image = (Image) typeToImage.get(element.name);
			if (image == null) {
				String url = "/icons/contentassist/" + element.name + ".gif";
				ImageDescriptor descriptor = ImageDescriptor
						.createFromURL(SmlUiPlugin.getDefault().getBundle()
								.getEntry(url));
				image = descriptor.createImage();
				typeToImage.put(element.name, image);
			}
			return image;
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

}
