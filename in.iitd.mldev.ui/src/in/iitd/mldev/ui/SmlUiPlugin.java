package in.iitd.mldev.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/** The main plugin class. Generated by Eclipse and not modified much.
 * Contains "global" string constants used as identifiers by many classes. */
/* TODO: write more TODOs */
public class SmlUiPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static SmlUiPlugin plugin;
	
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

	/** Integer attribute for the tab width. */
	public static final String SML_TAB_WIDTH = "in.iitd.mldev.smlTabWidth";
	/** Boolean attribute for whether to mark syntax errors in the editor. */
	public static final String SML_MARK_ERRORS = "in.iitd.mldev.smlMarkErrors";

	//Resource bundle.
	private ResourceBundle resourceBundle;


	/**
	 * The constructor.
	 */
	public SmlUiPlugin () {
		super();
		plugin = this;
		try {
			resourceBundle = ResourceBundle.getBundle("in.iitd.mldev.ui.SmlUiPluginResources");
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start (BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop (BundleContext context) throws Exception {
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static SmlUiPlugin getDefault () {
		return plugin;
	}

	/** Initializes default values for this plug-in's preferences. */
	/* TODO: This is deprecated. See the deprecated comment in
	 * Plugin.initializeDefaultPluginPreferences. */
	protected void initializeDefaultPluginPreferences () {
		IPreferenceStore store = getPreferenceStore();
		store.setDefault(SML_BRACKET_MATCHING_ENABLED, true);
		PreferenceConverter.setDefault(store, SML_BRACKET_MATCHING_COLOR, new RGB(192, 192, 192));
		PreferenceConverter.setDefault(store, SML_KEYWORD_COLOR, new RGB(128,0,128));
		PreferenceConverter.setDefault(store, SML_STRING_COLOR, new RGB(0,0,255));
		PreferenceConverter.setDefault(store, SML_COMMENT_COLOR, new RGB(0,128,0));
		store.setDefault(SML_TAB_WIDTH, 2);
		store.setDefault(SML_MARK_ERRORS, true);
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString (String key) {
		ResourceBundle bundle = SmlUiPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle () {
		return resourceBundle;
	}

}