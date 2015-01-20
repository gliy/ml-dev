package in.iitd.mldev.process;

import org.eclipse.core.resources.IFile;

public interface ISmlListener {

	void lineRead(IFile file, String line);
	
}
