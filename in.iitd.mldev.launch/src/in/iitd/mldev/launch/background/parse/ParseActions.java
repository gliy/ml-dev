package in.iitd.mldev.launch.background.parse;

import in.iitd.mldev.launch.background.SmlException;
import in.iitd.mldev.launch.background.SmlFunction;
import in.iitd.mldev.launch.background.SmlLineOutput;
import in.iitd.mldev.launch.background.SmlVal;
import in.iitd.mldev.launch.background.SmlLineOutput.SmlErrorOutput;
import in.iitd.mldev.launch.background.SmlLineOutput.SmlWarningOutput;

import java.util.Arrays;
import java.util.List;

public class ParseActions {

	private ParseActions() {

	}

	public static List<SmlParseAction> getActions() {
		return Arrays.asList(IGNORE_ACTION, MODULE_ACTION, IT_ACTION,
				FUNCTION_ACTION,VAL_ACTION, DATA_TYPE_ACTION, EXCEPTION_ACTION, WARNING_ACTION,ERROR_ACTION);
	}

	private static final SmlParseAction MODULE_ACTION = new SmlParseAction(
			"\\[opening\\s*([^\\]]*).*") {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			System.out.println("Adding module " + data[0]);
			parser.setCurrent(parser.getCurrent().addModule(data[0]));
		}
	};

	private static final SmlParseAction EXCEPTION_ACTION = new SmlParseAction(
			trim("exception ([^\\s]+) of (.*)"), trim("exception ([^\\s]+)")) {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			SmlException ex;
			if (data.length > 1) {
				ex = new SmlException(data[0], data[1]);
			} else {
				ex = new SmlException(data[0]);
			}
			parser.getCurrent().add(ex);
		}
	};
	private static final SmlParseAction WARNING_ACTION = new SmlParseAction(
			trim("^.*:([^\\s]+) Warning:(.*)")) {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlWarningOutput(SmlLineOutput
							.createLine(data[0]), data[1]));
		}
	};
	private static final SmlParseAction ERROR_ACTION = new SmlParseAction(
			trim("^.*:([^\\s]+) Error:(.*)")) {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlErrorOutput(SmlLineOutput
							.createLine(data[0]), data[1]));
		}
	};
	
	private static final SmlParseAction DATA_TYPE_ACTION = new SmlParseAction(
			trim("datatype ([^\\s]+) = (.*)")) {
		
		@Override
		public void parse(SmlOutputParser parser, String... data) {
			
		}
	};
	private static final SmlParseAction VAL_ACTION = new SmlParseAction(
			 trim("val ([^=]+) = ([^:]+) : (.*)")) {
		
		@Override
		public void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(new SmlVal(data[0], data[1],data[2]));
		}
	};
	private static final SmlParseAction FUNCTION_ACTION = new SmlParseAction(
			trim("val ([^=]+) = fn : (.*)")) {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlFunction(data[0], data[1].split("->")));

		}
	};
	private static final SmlParseAction IT_ACTION = new SmlParseAction(
			trim("val it = \\(\\).*")) {

		@Override
		public void parse(SmlOutputParser parser, String... data) {
			parser.unsetCurrent();
		}
	};

	private static final SmlParseAction IGNORE_ACTION = ignore(
			trim("\\[library .*\\]"), trim("\\[autoloading.*"));

	private static SmlParseAction ignore(String... ignoreRegex) {
		return new SmlParseAction(ignoreRegex) {

			@Override
			public void parse(SmlOutputParser parser, String... data) {

			}
		};
	}

	private static String trim(String regex) {
		return "\\s*" + regex;
	}
}
