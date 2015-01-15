package in.iitd.mldev.ui.ruler;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.rulers.AbstractContributedRulerColumn;

public class SmlRuler extends AbstractMarkerAnnotationModel{

	@Override
	protected IMarker[] retrieveMarkers() throws CoreException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void deleteMarkers(IMarker[] markers) throws CoreException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void listenToMarkerChanges(boolean listen) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isAcceptable(IMarker marker) {
		// TODO Auto-generated method stub
		return false;
	}



}
