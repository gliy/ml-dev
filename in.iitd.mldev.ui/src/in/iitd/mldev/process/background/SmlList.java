package in.iitd.mldev.process.background;

import in.iitd.mldev.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmlList extends SmlObject {

	private SmlType listType;
	private List<String> listValues;

	public SmlList(String name, String type, String... values) {
		super(name, SmlType.LIST_TYPE);
		this.listType = SmlType.parseType(type.replaceAll(" list$", ""));
		if (values == null) {
			this.listValues = new ArrayList<String>();
		} else {
			this.listValues = Arrays.asList(values);
		}
	}

	public SmlType getListType() {
		return listType;
	}

	public List<String> getListValues() {
		return listValues;
	}

	@Override
	public String toString() {
		return String.format("[%s] : %s list",
				StringUtils.join(listValues, ", "), listType);
	}
}
