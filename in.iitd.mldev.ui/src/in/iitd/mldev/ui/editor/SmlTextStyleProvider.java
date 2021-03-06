package in.iitd.mldev.ui.editor;

import java.util.List;

import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/** A utility class to get colours and font styles for syntax highlighting. */
public class SmlTextStyleProvider {

	private TextAttribute defaultStyle, keywordStyle, stringStyle,
			commentStyle, integerStyle, realStyle, listStyle, recordStyle;
	private TextAttribute[] rainbowParensStyle;

	/**
	 * Creates a new text style provider. Currently, a new provider is created
	 * for each SML editor.
	 */
	/*
	 * Colour preferences are only read in the constructor, which is why editors
	 * need to be closed and reopened after changing the colours.
	 */
	public SmlTextStyleProvider() {
		IPreferenceStore store = SmlUiPlugin.getDefault().getPreferenceStore();
		RGB keywordColor = PreferenceConverter.getColor(store,
				PreferenceConstants.SML_KEYWORD_COLOR);
		RGB stringColor = PreferenceConverter.getColor(store,
				PreferenceConstants.SML_STRING_COLOR);
		RGB commentColor = PreferenceConverter.getColor(store,
				PreferenceConstants.SML_COMMENT_COLOR);
		RGB integerColor = PreferenceConverter.getColor(store,
				PreferenceConstants.SML_INT_COLOR);

		keywordStyle = new TextAttribute(new Color(null, keywordColor), null,
				SWT.BOLD);
		stringStyle = new TextAttribute(new Color(null, stringColor), null,
				SWT.NORMAL);
		commentStyle = new TextAttribute(new Color(null, commentColor), null,
				SWT.NORMAL);
		integerStyle = new TextAttribute(new Color(null, integerColor), null,
				SWT.NORMAL);
		realStyle = new TextAttribute(new Color(null, PreferenceConverter.getColor(store,
				PreferenceConstants.SML_REAL_COLOR)), null, SWT.NORMAL);
		listStyle = new TextAttribute(new Color(null, PreferenceConverter.getColor(store,
				PreferenceConstants.SML_LIST_COLOR)), null, SWT.NORMAL);
		recordStyle = new TextAttribute(new Color(null, PreferenceConverter.getColor(store,
				PreferenceConstants.SML_RECORD_COLOR)), null, SWT.NORMAL);

		List<String> keys = SmlUiPlugin.getRainbowParenStrings();
		rainbowParensStyle = new TextAttribute[keys.size()];
		for (int i = 0; i < keys.size(); i++) {
			rainbowParensStyle[i] = new TextAttribute(new Color(null,
					PreferenceConverter.getColor(store, keys.get(i))), null,
					SWT.BOLD);
		}
	}

	/** Disposes the colours allocated in the constructor. */
	/*
	 * "Applications must dispose of all colors which they allocate."
	 * Unfortunately, this method is never called.
	 */
	public void dispose() {
		TextAttribute[] styles = { defaultStyle, keywordStyle, stringStyle,
				commentStyle, integerStyle, realStyle, listStyle, recordStyle };
		for (int i = 0; i < styles.length; i++) {
			if (styles[i].getBackground() != null)
				styles[i].getBackground().dispose();
			if (styles[i].getForeground() != null)
				styles[i].getForeground().dispose();
		}
	}

	/** Returns the colour and font style for comments in the editor. */
	public TextAttribute getCommentStyle() {
		return commentStyle;
	}

	/** Returns the colour and font style for keywords in the editor. */
	public TextAttribute getKeywordStyle() {
		return keywordStyle;
	}

	/** Returns the colour and font style for strings in the editor. */
	public TextAttribute getStringStyle() {
		return stringStyle;
	}

	public TextAttribute getIntegerStyle() {
		return integerStyle;
	}
	
	public TextAttribute getRealStyle() {
		return realStyle;
	}

	public TextAttribute[] getRainbowParensStyle() {
		return rainbowParensStyle;
	}

	public TextAttribute getListStyle() {
		return listStyle;
	}

	public TextAttribute getRecordStyle() {
		return recordStyle;
	}
}
