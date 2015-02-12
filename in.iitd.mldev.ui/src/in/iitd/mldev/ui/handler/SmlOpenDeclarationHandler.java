package in.iitd.mldev.ui.handler;

import in.iitd.mldev.core.model.SmlBinding;
import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.ui.editor.SmlEditor;

import java.util.Arrays;
import java.util.List;

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

public class SmlOpenDeclarationHandler extends AbstractHandler {
	private static final String TERMINATING_CHARACTERS = "=\n\r ;),(|{}+-/><[]~\"'";
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
				int left;
				ITextSelection textSelection = (ITextSelection) selection;
				String selectionText = textSelection.getText();
				if (selectionText.isEmpty()) {

					int offset = textSelection.getOffset(); // etc.
					IDocument document = smlEditor.getDocumentProvider()
							.getDocument(editor.getEditorInput());
					int backOffset = searchBackwards(offset - 1, document);
					int forwardOffset = searchForwards(offset, document);
					selectionText = document.get(backOffset, forwardOffset
							- backOffset);
					System.out.println(">>> "
							+ document.get(backOffset, forwardOffset
									- backOffset));

					left = gotoDeclaration(backOffset, selectionText.trim(), smlEditor.getProgram(), selectionProvider);
				} else {
					int backOffset = textSelection.getOffset() - textSelection.getLength();
					left = gotoDeclaration(backOffset, selectionText.trim(), smlEditor.getProgram(), selectionProvider);
				}
				if(left >= 0) {
					selectionProvider.setSelection(new TextSelection(left, 0));
				}

			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int gotoDeclaration(int from, 
			String selectionText, SmlProgram program, ISelectionProvider selectionProvider) {
		int maxLeft = -1;
		// if a function/val is defined twice, always choose the last one(in scope)
		for (SmlBinding binding : program.getBindings()) {
			if (binding.getLeft() > from) {
				
			}else if (binding.getIdent().name.equals(selectionText)) {
				maxLeft = Math.max(binding.getLeft(), maxLeft);
			}
			
		}
		return maxLeft;

	}

	private int searchBackwards(int start, IDocument document)
			throws BadLocationException {
		int i = start;
		char c = document.getChar(start);
		while (i > 0 && !TERMINATING_CHARACTERS.contains("" + c)) {
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
		while (i < length && !TERMINATING_CHARACTERS.contains("" + c)) {
			i++;
			c = document.getChar(i);
		}
		return i;
	}

}
