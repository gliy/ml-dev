package in.iitd.mldev.process.background;

import in.iitd.mldev.utils.StringUtils;

import java.util.List;

public class SmlTuple extends SmlObject{

	private List<String> values;
	private List<SmlType> tupleTypes;
	
	public SmlTuple(String name, List<String> values, List<String> types) {
		super(name, SmlType.TUPLE_TYPE);
		this.values = values;
		this.tupleTypes = SmlType.parseTypes(types);
	}
	
	public List<String> getValues() {
		return values;
	}
	
	public List<SmlType> getTupleTypes() {
		return tupleTypes;
	}
	@Override
	public String toString() {
		return String.format("(%s) : %s", StringUtils.join(values, ","),
				StringUtils.join(tupleTypes, " * "));
	}

}
