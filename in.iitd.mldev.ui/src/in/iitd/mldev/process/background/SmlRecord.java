package in.iitd.mldev.process.background;

import in.iitd.mldev.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SmlRecord extends SmlObject {

	private Map<String, String> values;
	private Map<String, SmlType> types;

	public SmlRecord(String name, Map<String, String> values,
			Map<String, String> types) {
		super(name, SmlType.RECORD_TYPE);
		this.values = values;
		this.types = new LinkedHashMap<String, SmlObject.SmlType>();
		for (Entry<String, String> type : types.entrySet()) {
			this.types.put(type.getKey().trim(), SmlType.parseType(type.getValue().trim()));
		}
	}

	public Map<String, String> getValues() {
		return values;
	}

	public Map<String, SmlType> getTypes() {
		return types;
	}

	@Override
	public String toString() {
		return String.format("{%s} : {%s}",
				StringUtils.join(values.entrySet(), ","),
				StringUtils.join(types.entrySet(), ", "));
	}

}
