package in.iitd.mldev.process.background;

import in.iitd.mldev.process.background.SmlObject.SmlType;

public class SmlVal extends SmlObject {
	private String value;
	
	public SmlVal(String name, String value, String type) {
		super(name, type);
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return String.format("%s : %s", getValue(), getType());
	}
}
