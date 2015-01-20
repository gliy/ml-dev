package in.iitd.mldev.process.background.parse;

import in.iitd.mldev.process.background.SmlException;
import in.iitd.mldev.process.background.SmlFunction;
import in.iitd.mldev.process.background.SmlLineOutput;
import in.iitd.mldev.process.background.SmlLineOutput.SmlErrorOutput;
import in.iitd.mldev.process.background.SmlLineOutput.SmlWarningOutput;
import in.iitd.mldev.process.background.SmlList;
import in.iitd.mldev.process.background.SmlRecord;
import in.iitd.mldev.process.background.SmlTuple;
import in.iitd.mldev.process.background.SmlVal;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ParseActions {

	private ParseActions() {

	}

	public static List<SmlParseAction> getActions() {
		return Arrays.asList(IGNORE_ACTION, MODULE_ACTION, IT_ACTION,
				FUNCTION_ACTION, TUPLE_ACTION, LIST_ACTION, RECORD_ACTION, VAL_ACTION, DATA_TYPE_ACTION,
				EXCEPTION_ACTION, WARNING_ACTION, ERROR_ACTION);
	}

	private static final SmlParseAction MODULE_ACTION = new SmlParseAction(
			"\\[opening\\s*([^\\]]*).*") {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			System.out.println("Adding module " + data[0]);
			parser.setCurrent(parser.getCurrent().addModule(data[0]));
		}
	};

	private static final SmlParseAction EXCEPTION_ACTION = new SmlParseAction(
			trim("exception ([^\\s]+) of (.*)"), trim("exception ([^\\s]+)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
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
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlWarningOutput(SmlLineOutput.createLine(data[0]),
							data[1]));
		}
	};
	private static final SmlParseAction ERROR_ACTION = new SmlParseAction(
			trim("^.*:([^\\s]+) Error:(.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlErrorOutput(SmlLineOutput.createLine(data[0]),
							data[1]));
		}
	};

	private static final SmlParseAction DATA_TYPE_ACTION = new SmlParseAction(
			trim("datatype ([^\\s]+) = (.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {

		}
	};
	private static final SmlParseAction VAL_ACTION = new SmlParseAction(
			trim("val ([^=]+) = ([^:]+) : (.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(new SmlVal(data[0], data[1], data[2]));
		}
	};
	private static final SmlParseAction FUNCTION_ACTION = new SmlParseAction(
			trim("val ([^=]+) = fn : (.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlFunction(data[0], data[1].split("->")));

		}
	};
	
	private static final SmlParseAction TUPLE_ACTION = new SmlParseAction(
			trim("val ([^=]+) = \\(([^\\)]+)\\) : (.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlTuple(data[0], Arrays.asList(data[1].split(",")),
							Arrays.asList(data[2].split("\\*"))));

		}
	};
	private static final SmlParseAction LIST_ACTION = new SmlParseAction(
			trim("val ([^=]+) = \\[([^\\]]+)\\] : (.*)")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.getCurrent().add(
					new SmlList(data[0], data[2], data[1].split(",")));

		}
	};
	
	private static final SmlParseAction RECORD_ACTION = new SmlParseAction(
			trim("val ([^=]+) = \\{([^\\}]+)\\} : \\{([^\\}]+)\\}.*")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			Map<String, String> values = new LinkedHashMap<String, String>();
			Map<String, String> types = new LinkedHashMap<String, String>();
			for (String value : data[1].split(",")) {
				String[] nameValue = value.split("=");
				values.put(nameValue[0], nameValue[1]);
			}
			for (String value : data[2].split(",")) {
				String[] nameValue = value.split(":");
				types.put(nameValue[0], nameValue[1]);
			}
			
			parser.getCurrent().add(new SmlRecord(data[0], values, types));

		}
	};
	
	private static final SmlParseAction IT_ACTION = new SmlParseAction(
			trim("val it = \\(\\).*")) {

		@Override
		protected void parse(SmlOutputParser parser, String... data) {
			parser.unsetCurrent();
		}
	};

	private static final SmlParseAction IGNORE_ACTION = ignore(
			trim("\\[library .*\\]"), trim("\\[autoloading.*"));

	private static SmlParseAction ignore(String... ignoreRegex) {
		return new SmlParseAction(ignoreRegex) {

			@Override
			protected void parse(SmlOutputParser parser, String... data) {

			}
		};
	}

	private static String trim(String regex) {
		return "\\s*" + regex;
	}
}
