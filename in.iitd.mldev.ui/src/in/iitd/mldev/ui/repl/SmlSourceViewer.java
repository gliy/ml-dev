package in.iitd.mldev.ui.repl;

import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.ui.editor.ISmlEditor;
import in.iitd.mldev.ui.editor.SmlEditor;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SmlSourceViewer extends ProjectionViewer implements
		IPropertyChangeListener, ISmlEditor {

	public SmlSourceViewer(Composite parent, IVerticalRuler ruler,
			IOverviewRuler overviewRuler, boolean showsAnnotationOverview,
			int styles) {
		super(parent, ruler, overviewRuler, showsAnnotationOverview, styles);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
	}

	@Override
	public SmlProgram getProgram() {
		return SmlEditor.editor.getProgram();
	}

	@Override
	public SmlModule getRoot() {
		return SmlEditor.editor.getRoot();
	}

	@Override
	public void configure(SourceViewerConfiguration configuration) {
		super.configure(configuration);
	}

	static public Color createColor(IPreferenceStore store, String key,
			Display display) {
		RGB rgb = getRGBColor(store, key);
		return (rgb != null) ? new Color(display, rgb) : null;
	}

	static public RGB getRGBColor(IPreferenceStore store, String key) {
		RGB rgb = null;

		if (store.contains(key)) {
			if (store.isDefault(key))
				rgb = PreferenceConverter.getDefaultColor(store, key);
			else
				rgb = PreferenceConverter.getColor(store, key);
		}

		return rgb;
	}

}
