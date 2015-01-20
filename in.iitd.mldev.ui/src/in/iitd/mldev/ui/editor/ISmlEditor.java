package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.process.background.SmlModule;

public interface ISmlEditor {

	SmlProgram getProgram();
	SmlModule getRoot();
}
