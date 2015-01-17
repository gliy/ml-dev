package in.iitd.mldev.ui;

import org.eclipse.swt.graphics.RGB;

public class PreferenceConstants {

	public static final String SML_PLUGIN_ID = "in.iitd.mldev.ui";
	/** Boolean attribute for whether bracket matching is enabled.
	 * We set it to true in initializeDefaultPluginPreferences,
	 * and never change it, so bracket matching is always on. */
	public static final String SML_BRACKET_MATCHING_ENABLED = "in.iitd.mldev.smlBracketMatchingEnabled";
	/** Color attribute for the color of the bracket matching box. */
	public static final String SML_BRACKET_MATCHING_COLOR = "in.iitd.mldev.smlBracketMatchingColor";
	/** Color attribute for the color for keywords in syntax highlighting. */
	public static final String SML_KEYWORD_COLOR = "in.iitd.mldev.smlKeywordColor";
	/** Color attribute for the color for strings in syntax highlighting. */
	public static final String SML_STRING_COLOR = "in.iitd.mldev.smlStringColor";
	/** Color attribute for the color for comments in syntax highlighting. */
	public static final String SML_COMMENT_COLOR = "in.iitd.mldev.smlCommentColor";
	public static final String SML_INT_COLOR = "in.iitd.mldev.smlIntColor";
	public static final String SML_REAL_COLOR = "in.iitd.mldev.smlRealColor";
	/** Integer attribute for the tab width. */
	public static final String SML_TAB_WIDTH = "in.iitd.mldev.smlTabWidth";
	/** Boolean attribute for whether to mark syntax errors in the editor. */
	public static final String SML_MARK_ERRORS = "in.iitd.mldev.smlMarkErrors";
	public static final String SML_RAINBOW_PAREN = "in.iitd.mldev.rainbowParen";
	public static final int SML_RAINBOW_PAREN_COUNT = 8;
	public static final RGB[] SML_RAINBOW_PAREN_DEFAULTS = new RGB[]{
		new RGB(212, 23, 142),
		new RGB(130, 3, 3),
		new RGB(40,161,50),
		new RGB(227,174,14),
		new RGB(100,127,209),
		new RGB(100, 204, 209),
		new RGB(237,230,14),
		new RGB(219,77,207),
	};
	public static final String TEMPLATE_KEY = "in.iitd.mldev.templatestore";
	public static final String SML_REPL_HINTS = "in.iitd.mldev.replhints";

	private PreferenceConstants(){}
}
