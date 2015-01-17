package in.iitd.mldev.ui.handler;

import in.iitd.mldev.process.SmlLauncher;
import in.iitd.mldev.ui.editor.SmlEditor;
import in.iitd.mldev.ui.repl.SmlReplView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class SmlReplHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		System.out.println(page);
		if(page.getActiveEditor().getClass() == SmlEditor.class) {
			IEditorInput editor = page.getActiveEditor().getEditorInput();
			if(editor instanceof FileEditorInput){
				FileEditorInput fileInput = (FileEditorInput)editor;
				//SmlLauncher.launch(fileInput.getFile());
				try {
					SmlReplView.connect(fileInput.getFile(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
