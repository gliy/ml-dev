package in.iitd.mldev.process.background;

import in.iitd.mldev.utils.StringUtils;

import java.util.List;

public class SmlTuple extends SmlObject{

	private List<String> values;
	private List<SmlType> types;
	
	public SmlTuple(String name, List<String> values, List<String> types) {
		super(name, SmlType.TUPLE_TYPE);
		this.values = values;
		this.types = SmlType.parseTypes(types);
	}
	
	public List<String> getValues() {
		return values;
	}
	@Override
	public SmlType getType() {
		return super.getType();
	}
	
	@Override
	public String toString() {
		return String.format("%s : (%s) : %s", name, StringUtils.join(values, ","),
				StringUtils.join(types, " * "));
	}

}
