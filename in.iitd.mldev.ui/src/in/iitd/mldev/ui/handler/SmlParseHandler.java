package in.iitd.mldev.ui.handler;

import in.iitd.mldev.process.background.ISmlParsed;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.process.background.SmlProgramOutput;
import in.iitd.mldev.ui.editor.SmlEditor;

public class SmlParseHandler implements ISmlParsed {
	private static SmlProgramOutput output;
	
	@Override
	public void programParsed(SmlProgramOutput output) {
		SmlParseHandler.output = output;
		SmlEditor.editor.programChanged();
	}
	
	public static SmlModule getOutput() {
		return output == null ? null : output.getRoot().getModule(output.getRoot().getName());
	}
}
