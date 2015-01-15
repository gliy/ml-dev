package in.iitd.mldev.launch.background;

import in.iitd.mldev.launch.background.SmlObject.SmlType;

public class SmlVal extends SmlObject {
	private String name;
	private String value;
	private String type;
	
	
	public SmlVal(String name, String value, String type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}


	@Override
	public SmlType getType() {
		return SmlType.VAL_TYPE;
	}


	public String getName() {
		return name;
	}

	public String getValType() {
		return type;
	}

	public String getValue() {
		return value;
	}
}
