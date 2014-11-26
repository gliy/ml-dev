package in.iitd.mldev.ui.handler;

import java.util.List;

import in.iitd.mldev.core.model.SmlBinding;
import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.core.parse.ast.Dec;
import in.iitd.mldev.core.parse.ast.FunDec;
import in.iitd.mldev.ui.editor.SmlEditor;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

public class SMLOpenDeclarationHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor.getClass() != SmlEditor.class) {
			return null;
		}

		SmlEditor smlEditor = (SmlEditor) editor;
		ISelectionProvider selectionProvider = smlEditor.getSelectionProvider();
		ISelection selection = selectionProvider.getSelection();
		try {
			if (selection instanceof ITextSelection) {
				ITextSelection textSelection = (ITextSelection) selection;
				String selectionText = textSelection.getText();
				if (selectionText.isEmpty()) {

					int offset = textSelection.getOffset(); // etc.
					IDocument document = smlEditor.getDocumentProvider()
							.getDocument(editor.getEditorInput());
					int backOffset = searchBackwards(offset, document);
					int forwardOffset = searchForwards(offset, document);
					selectionText = document.get(backOffset, forwardOffset
							- backOffset);
					System.out.println(">>> "
							+ document.get(backOffset, forwardOffset
									- backOffset));

					gotoDeclaration(backOffset, selectionText.trim(), smlEditor.getProgram(), selectionProvider);
				}

			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void gotoDeclaration(int from, 
			String selectionText, SmlProgram program, ISelectionProvider selectionProvider) {
		// List<Dec> decs = program.getParseTree().decs;
		for (SmlBinding binding : program.getBindings()) {
			if (binding.getLeft() > from) {
				return;
			}
			if (binding.getIdent().name.equals(selectionText)) {
				selectionProvider.setSelection(new TextSelection(binding.getLeft(), 0));
				return;
			}
			
		}
		// for (Dec dec : decs) {
		// if(dec instanceof FunDec) {
		// System.out.println("DEC");
		// }
		// }

	}

	private int searchBackwards(int start, IDocument document)
			throws BadLocationException {
		int i = start;
		char c = document.getChar(start);
		while (i > 0 && c != '.' && c != '\n') {
			i--;
			c = document.getChar(i);
		}
		return i;
	}

	private int searchForwards(int start, IDocument document)
			throws BadLocationException {
		int i = start;
		char c = document.getChar(start);
		int length = document.getLength();
		while (i < length && c != '.' && c != '\n' && c != ' ') {
			i++;
			c = document.getChar(i);
		}
		return i;
	}

}
