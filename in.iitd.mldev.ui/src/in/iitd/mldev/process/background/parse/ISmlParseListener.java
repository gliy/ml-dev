package in.iitd.mldev.process.background.parse;

import in.iitd.mldev.process.background.ISmlModule;
import in.iitd.mldev.process.background.SmlDatatype;
import in.iitd.mldev.process.background.SmlException;
import in.iitd.mldev.process.background.SmlFunction;
import in.iitd.mldev.process.background.SmlList;
import in.iitd.mldev.process.background.SmlRecord;
import in.iitd.mldev.process.background.SmlTuple;
import in.iitd.mldev.process.background.SmlVal;
import in.iitd.mldev.process.background.SmlLineOutput.SmlErrorOutput;
import in.iitd.mldev.process.background.SmlLineOutput.SmlWarningOutput;

public interface ISmlParseListener {

	void parsedVal(ISmlModule module, SmlVal val);
	void parsedList(ISmlModule module, SmlList val);
	void parsedTuple(ISmlModule module, SmlTuple val);
	void parsedRecord(ISmlModule module, SmlRecord val);
	void parsedException(ISmlModule module, SmlException val);
	void parsedFunction(ISmlModule module, SmlFunction val);
	void parsedDatatype(ISmlModule module, SmlDatatype val);
	void parsedError(ISmlModule module, SmlErrorOutput val);
	void parsedWarning(ISmlModule module, SmlWarningOutput val);
	
	public static class DefaultSmlParseListener implements ISmlParseListener {

		@Override
		public void parsedVal(ISmlModule module, SmlVal val) {
		}

		@Override
		public void parsedList(ISmlModule module, SmlList val) {
		}

		@Override
		public void parsedTuple(ISmlModule module, SmlTuple val) {
		}

		@Override
		public void parsedRecord(ISmlModule module, SmlRecord val) {
		}

		@Override
		public void parsedException(ISmlModule module, SmlException val) {
		}

		@Override
		public void parsedFunction(ISmlModule module, SmlFunction val) {
		}

		@Override
		public void parsedDatatype(ISmlModule module, SmlDatatype val) {
		}

		@Override
		public void parsedError(ISmlModule module, SmlErrorOutput val) {
		}

		@Override
		public void parsedWarning(ISmlModule module, SmlWarningOutput val) {
		}
		
	}
}
