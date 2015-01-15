package in.iitd.mldev.ui.template;

import java.io.IOException;

import in.iitd.mldev.ui.SmlUiPlugin;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;
import org.eclipse.ui.editors.text.templates.ContributionTemplateStore;
import org.eclipse.ui.texteditor.templates.TemplatePreferencePage;

public class SMLTemplatePage extends TemplatePreferencePage {

	
	public SMLTemplatePage() {
		super();
		ContributionContextTypeRegistry registry = SmlUiPlugin.getDefault().getRegistry();
		IPreferenceStore prefStore = SmlUiPlugin.getDefault().getPreferenceStore();
		
		//registry.addContextType(contextType);
		setPreferenceStore(prefStore);
		setContextTypeRegistry(registry);
		
		ContributionTemplateStore store = SmlUiPlugin.getDefault().getTemplateStore();
		setTemplateStore(store);
		
	}
}
