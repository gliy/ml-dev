package in.iitd.mldev.ui.handler;

import in.iitd.mldev.process.SmlLauncher;
import in.iitd.mldev.ui.editor.SmlEditor;
import in.iitd.mldev.ui.repl.SmlReplView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class SmlReplHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IFile file = getActiveFile(page);
		if(file != null){
				try {
					SmlReplView.connect(file, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	public static IFile getActiveFile(IWorkbenchPage page) {
		if(page.getActiveEditor().getClass() == SmlEditor.class) {
			IEditorInput editor = page.getActiveEditor().getEditorInput();
			if(editor instanceof FileEditorInput){
				FileEditorInput fileInput = (FileEditorInput)editor;
				return fileInput.getFile();
			}
		}
		return null;
	}

}
