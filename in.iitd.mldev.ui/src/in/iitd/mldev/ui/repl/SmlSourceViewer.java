package in.iitd.mldev.ui.repl;

import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.ui.editor.ISmlEditor;
import in.iitd.mldev.ui.editor.SmlEditor;

import org.eclipse.jface.text.source.IOverviewRuler;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Composite;

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
	
}
