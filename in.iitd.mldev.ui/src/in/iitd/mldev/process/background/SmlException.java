package in.iitd.mldev.process.background;

import java.util.List;

public class SmlException extends SmlObject {

	private List<SmlType> types;
	public SmlException(String name, String... types) {
		super(name, SmlType.EXCEPTION_TYPE);
		this.types = SmlType.parseTypes(types);
	}
	
	public List<SmlType> getTypes() {
		return types;
	}
	

	
}
