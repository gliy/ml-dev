package in.iitd.mldev.ui.preferences;

import in.iitd.mldev.ui.PreferenceConstants;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SmlContentAssistPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		addField(new IntegerFieldEditor(PreferenceConstants.SML_AA_DELAY, "Auto activation delay:", getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.SML_AA_CHARS, "Auto activation characters:", getFieldEditorParent()));
	}

}
