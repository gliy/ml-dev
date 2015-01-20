package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.model.ISmlProgramListener;
import in.iitd.mldev.core.model.SmlBinding;
import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.process.ISmlProcess;
import in.iitd.mldev.process.SmlLauncher;
import in.iitd.mldev.process.background.ISmlModule;
import in.iitd.mldev.process.background.SmlLineOutput;
import in.iitd.mldev.process.background.SmlLineOutput.SmlErrorOutput;
import in.iitd.mldev.process.background.SmlListeningModule;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.process.background.parse.ISmlParseListener;
import in.iitd.mldev.process.background.parse.ISmlParseListener.DefaultSmlParseListener;
import in.iitd.mldev.process.background.parse.SmlOutputParser;
import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;
import in.iitd.mldev.ui.editor.outline.SmlContentOutlinePage;
import in.iitd.mldev.ui.handler.SmlParseHandler;
import in.iitd.mldev.ui.handler.SmlReplHandler;
import in.iitd.mldev.ui.ruler.SmlRuler;
import in.iitd.mldev.ui.text.SmlBracketMatcher;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.source.AnnotationRulerColumn;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.editors.text.EditorsUI;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.texteditor.ChainedPreferenceStore;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.MarkerUtilities;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

/** The SML editor. */
public class SmlEditor extends TextEditor implements ISmlProgramListener,
		ISmlEditor {
	public static SmlEditor editor;

	/** The content outline page shown in the Outline view. */
	private SmlContentOutlinePage outlinePage;
	/** The high-level model of the program in the editor. */
	private SmlProgram program;

	private ISmlProcess process;
	private IAnnotationModel annotationModel;

	private SmlModule root;

	/**
	 * Called by Eclipse to initialize the editor. Sets the editor to use the
	 * SML UI plug-in's preference store, and configures its syntax
	 * highlighting, tab width, etc. See SmlSourceViewerConfiguration.
	 */
	protected void initializeEditor() {
		this.editor = this;
		super.initializeEditor();
		setPreferenceStore(new ChainedPreferenceStore(new IPreferenceStore[] {
				SmlUiPlugin.getDefault().getPreferenceStore(),
				EditorsUI.getPreferenceStore() }));
		setSourceViewerConfiguration(new SmlSourceViewerConfiguration(this));
		setRangeIndicator(new DefaultRangeIndicator());
		showOverviewRuler();
		setKeyBindingScopes(new String[] { "in.iitd.mldev.ui.editor.context" });
		this.annotationModel = new SmlRuler();
	}

	@Override
	protected CompositeRuler createCompositeRuler() {
		CompositeRuler ruler = super.createCompositeRuler();
		ruler.setModel(annotationModel);
		ruler.addDecorator(1, new AnnotationRulerColumn(5));
		return ruler;
	}

	/**
	 * Returns this editor's outline page if requested. (This is how Eclipse
	 * gets the outline from the editor.) If the request is for something else,
	 * passes it on to the superclass.
	 */
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter.equals(IContentOutlinePage.class)) {
			if (outlinePage == null)
				outlinePage = new SmlContentOutlinePage(this);
			return outlinePage;
		}
		if (adapter.equals(IGotoMarker.class)) {
			return super.getAdapter(adapter);
		}
		return super.getAdapter(adapter);
	}

	/** Called by Eclipse when creating the editor. Adds bracket matching. */
	protected void configureSourceViewerDecorationSupport(
			SourceViewerDecorationSupport support) {
		super.configureSourceViewerDecorationSupport(support);
		support.setCharacterPairMatcher(new SmlBracketMatcher());
		support.setMatchingCharacterPainterPreferenceKeys(
				PreferenceConstants.SML_BRACKET_MATCHING_ENABLED,
				PreferenceConstants.SML_BRACKET_MATCHING_COLOR);
	}

	/** Returns the SmlProgram representing the document in this editor. */
	public SmlProgram getProgram() {
		if (program == null) {
			IDocument document = getDocumentProvider().getDocument(
					getEditorInput());
			program = new SmlProgram(document);
			program.addListener(this);
		}
		return program;
	}

	/**
	 * Called when the SmlProgram is updated. Removes existing error markers,
	 * and optionally adds markers for the errors in the updated program.
	 */
	/*
	 * We always remove all markers to avoid any markers getting left over after
	 * the user disables error marking.
	 */
	public void programChanged(SmlProgram program) {
		if (SmlUiPlugin.getDefault().getPreferenceStore()
				.getBoolean(PreferenceConstants.SML_MARK_ERRORS))
			addErrorMarkers(program);
	}

	public void programChanged() {
		programChanged(program);
	}

	/** Removes all error markers from the editor's document. */
	private void removeErrorMarkers() {
		if (!(getEditorInput() instanceof IFileEditorInput))
			return;
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		try {
			file.deleteMarkers(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/** Adds markers for all errors in the given program. */
	private void addErrorMarkers(SmlProgram program) {
		if (!(getEditorInput() instanceof IFileEditorInput))
			return;
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		IRegion[] problems = program.getProblems();
		try {
			for (int i = 0; i < problems.length; i++) {
				// IDocument document =
				// getDocumentProvider().getDocument(getEditorInput());
				Map<String, Object> attributes = new HashMap<String, Object>();
				attributes.put(IMarker.MESSAGE, "Syntax error");
				attributes.put(IMarker.CHAR_START,
						new Integer(problems[i].getOffset()));
				attributes.put(
						IMarker.CHAR_END,
						new Integer(problems[i].getOffset()
								+ problems[i].getLength()));
				attributes.put(IMarker.SEVERITY, new Integer(
						IMarker.SEVERITY_ERROR));
				attributes.put(IMarker.TRANSIENT, Boolean.TRUE);
				MarkerUtilities.createMarker(file, attributes, IMarker.PROBLEM);
			}

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public SmlModule getRoot() {
		return root;
	}
	@Override
	public void doSave(IProgressMonitor progressMonitor) {
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		removeErrorMarkers();
		this.root = new SmlModule(file.getName());
		this.process = SmlLauncher.launch(file, new SmlOutputParser(
				new SmlListeningModule(root,
						errorListener(file))));

		super.doSave(progressMonitor);
	}

	private void createMarker(IFile file, SmlLineOutput err) {
		try {
			int startCol = err.getStartCol() - 1;
			int line = program.getDocument().getLineOffset(
					err.getStartLine() - 1);
			int endLine = err.getEndLine() == null ? line : program
					.getDocument().getLineOffset(err.getEndLine() - 1);
			int endCol = err.getEndCol() == null ? endLine
					+ program.getDocument().getLineLength(
							err.getStartLine() - 1) - startCol : endLine
					+ err.getEndCol();
			int sev = err instanceof SmlErrorOutput ? IMarker.SEVERITY_ERROR
					: IMarker.SEVERITY_WARNING;
			Map<String, Object> attributes = new HashMap<String, Object>();
			attributes.put(IMarker.MESSAGE, err.getErrorMessage());
			attributes.put(IMarker.CHAR_START, new Integer(line + startCol));
			attributes.put(IMarker.CHAR_END, new Integer(endCol));
			attributes.put(IMarker.SEVERITY, new Integer(sev));
			attributes.put(IMarker.LINE_NUMBER, err.getStartLine());
			attributes.put(IMarker.TRANSIENT, Boolean.TRUE);
			MarkerUtilities.createMarker(file, attributes, IMarker.PROBLEM);
		} catch (BadLocationException ex) {

		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called when the cursor position changes. Selects the binding at that
	 * position in the outline page.
	 */
	protected void handleCursorPositionChanged() {
		super.handleCursorPositionChanged();
		selectInOutline();
	}

	private ISmlParseListener errorListener(final IFile file) {
		return new DefaultSmlParseListener() {

			public void parsedWarning(ISmlModule module,
					SmlLineOutput.SmlWarningOutput val) {
				if (module.getName().equals(file.getName())) {
					createMarker(file, val);
				}
			}

			public void parsedError(ISmlModule module, SmlErrorOutput val) {
				System.out.println("<< Adding error marker");
				if (module.getName().equals(file.getName())) {
					createMarker(file, val);
				}
			}
		};
	}

	private void selectInOutline() {

		if (outlinePage == null || program == null) {
			// nothing to select
			return;
		}

		ISourceViewer sourceViewer = getSourceViewer();
		int offset = widgetOffset2ModelOffset(sourceViewer, sourceViewer
				.getTextWidget().getCaretOffset());
		SmlBinding binding = getProgram().getBinding(offset);
		if (binding != null) {
			outlinePage.select(binding);
		} else {
			outlinePage.deselect();
		}
	}

}
