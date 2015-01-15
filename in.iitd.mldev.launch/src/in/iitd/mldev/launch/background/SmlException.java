package in.iitd.mldev.launch.background;

public class SmlException extends SmlFunction {

	public SmlException(String name, String... types) {
		super(name, types);
	}
	
	@Override
	public SmlType getType() {
		return SmlType.EXCEPTION_TYPE;
	}

	
}
