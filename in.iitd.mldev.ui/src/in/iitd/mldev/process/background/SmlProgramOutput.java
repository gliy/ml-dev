package in.iitd.mldev.process.background;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.internal.C;

public class SmlProgramOutput {
	private ISmlModule root;

	public SmlProgramOutput(ISmlModule root) {
		super();
		this.root = root;
	}
	public ISmlModule getRoot() {
		return root;
	}
	
	
	
}
