package in.iitd.mldev.ui.editor;

import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.text.edits.TextEdit;

public class SmlCodeFormatter extends CodeFormatter {

	@Override
	public TextEdit format(int kind, String source, int offset, int length,
			int indentationLevel, String lineSeparator) {
		// TODO Auto-generated method stub
		System.out.println("FORMAT");
		return null;
	}

	@Override
	public TextEdit format(int kind, String source, IRegion[] regions,
			int indentationLevel, String lineSeparator) {
		// TODO Auto-generated method stub
		System.out.println("FORMAT");
		return null;
	}

}
