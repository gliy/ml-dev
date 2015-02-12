package in.iitd.mldev.ui.repl;

import in.iitd.mldev.process.ISmlListener;
import in.iitd.mldev.process.ISmlProcess;
import in.iitd.mldev.process.SmlLauncher;
import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;
import in.iitd.mldev.ui.editor.ISmlEditor;
import in.iitd.mldev.ui.editor.SmlSourceViewerConfiguration;
import in.iitd.mldev.utils.DisplayUtil;
import in.iitd.mldev.utils.StringUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.SourceViewerDecorationSupport;

/**
 * Most of this code was gathered from Counterclockwise plugin for clojure in
 * eclipse
 *
 */
public class SmlReplView extends ViewPart implements IAdaptable {
	private StyledText logPanel;
	private SmlSourceViewer viewer;
	private SmlSourceViewerConfiguration viewerConfig;
	private StyledTextPlaceHolder placeholder;
	private IConsole console;
	private ILaunch launch;
	public StyledText viewerWidget;
	private SourceViewerDecorationSupport fSourceViewerDecorationSupport;
	public static final String VIEW_ID = "in.iitd.mldev.ui.smlREPLView";
	
	public static final AtomicReference<SmlReplView> activeREPL = new AtomicReference<SmlReplView>();

	private ISmlProcess program;

	public IConsole getConsole() {
		return console;
	}

	@Override
	public void dispose() {
		super.dispose();
		if (this.program != null) {
			this.program.terminate();

		}
		SmlReplView.activeREPL.set(null);

	}

	public static SmlReplView connect(IFile file, boolean makeActiveREPL)
			throws Exception {
		return connect(file, null, null, makeActiveREPL);
	}

	public static SmlReplView connect(final IFile file, IConsole console,
			ILaunch launch, final boolean makeActiveREPL) throws Exception {
		SmlReplView replView = SmlReplView.activeREPL.get();
		if (replView != null) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage()
					.showView(VIEW_ID, null, IWorkbenchPage.VIEW_ACTIVATE);
			if (replView.program != null) {
				replView.program.load(file);
			} else {
				replView.configure(file);
			}
		}
		final SmlReplView repl = (SmlReplView) PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.showView(VIEW_ID, null, IWorkbenchPage.VIEW_ACTIVATE);
		repl.console = console;
		// repl.showConsoleAction.setEnabled(console != null);
		repl.launch = launch;
		repl.configure(file);
		if (makeActiveREPL) {
			SmlReplView.activeREPL.set(repl);
		}
		return repl;

	}

	private void configure(IFile file) {
		this.program = SmlLauncher.launch(file, new ISmlListener() {

			@Override
			public void lineRead(IFile file, final String line) {
				DisplayUtil.asyncExec(new Runnable() {

					@Override
					public void run() {
						logPanel.append(line + "\n");
					}
				});

			}
		});

	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		activeREPL.set(SmlReplView.this);
	}

	@SuppressWarnings("restriction")
	@Override
	public void createPartControl(Composite parent) {
		final IPreferenceStore prefs = SmlUiPlugin.getDefault()
				.getPreferenceStore();
		SashForm split = new SashForm(parent, SWT.VERTICAL);
		split.setBackground(split.getDisplay().getSystemColor(
				SWT.COLOR_WIDGET_BACKGROUND));

		logPanel = new StyledText(split, SWT.V_SCROLL | SWT.WRAP);
		logPanel.setIndent(4);
		logPanel.setEditable(false);
		
		logPanel.setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));
		viewer = new SmlSourceViewer(split, null, null, false, SWT.V_SCROLL
				| SWT.H_SCROLL);

		viewerConfig = new SmlSourceViewerConfiguration(viewer);
		fSourceViewerDecorationSupport = new SourceViewerDecorationSupport(
				viewer, null, null, EditorsPlugin.getDefault()
						.getSharedTextColors());
		fSourceViewerDecorationSupport.install(prefs);
		viewer.configure(viewerConfig);
		viewerWidget = viewer.getTextWidget();
		
		getViewSite().setSelectionProvider(viewer);
		viewer.setDocument(new Document());
		final StyledText st = (StyledText) viewer.getControl();
		setPlaceHolder(st, "<type SML code here>");


		logPanel.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				logPanel.setTopIndex(logPanel.getLineCount() - 1);

			}
		});

		installAutoEvalExpressionOnEnter();

	}



	private void setPlaceHolder(final StyledText st, final String placeholder) {
		final IPropertyChangeListener replHintsListener = new IPropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(
						PreferenceConstants.SML_REPL_HINTS)) {
					updatePlaceHolder(st, placeholder);
				}
			}
		};
		getPreferences().addPropertyChangeListener(replHintsListener);
		st.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getPreferences()
						.removePropertyChangeListener(replHintsListener);
			}
		});
		updatePlaceHolder(st, placeholder);
	}

	private void updatePlaceHolder(final StyledText st, final String placeholder) {
		if (getPreferences().getBoolean(PreferenceConstants.SML_REPL_HINTS)) {
			if (this.placeholder == null) {
				this.placeholder = new StyledTextPlaceHolder(st, placeholder);
			}
			this.placeholder.setPlaceHolder(placeholder);
		} else {
			if (this.placeholder != null) {
				this.placeholder.setPlaceHolder(null);
			}
		}
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == ISmlEditor.class) {
			return viewer;
		} else {
			return super.getAdapter(adapter);
		}
	}

	@Override
	public void setFocus() {
		viewerWidget.setFocus();
	}

	private IPreferenceStore getPreferences() {
		return SmlUiPlugin.getDefault().getPreferenceStore();
	}

	private void createToolbar() {
		// IToolBarManager mgr =
		// getViewSite().getActionBars().getToolBarManager();
		// mgr.add(autoRepeatLastAction);
		// mgr.add(printErrorAction);
		// mgr.add(interruptAction);
		// mgr.add(reconnectAction);
		// mgr.add(clearLogAction);
		// mgr.add(newSessionAction);
		// mgr.add(showConsoleAction);
	}

	private void copyToLog(StyledText s) {
		// sadly, need to reset text on the ST in order to get formatting/style
		// // ranges...
		// s.setText(boostIndent.matcher(s.getText()).replaceAll("   ")
		// .replaceFirst("^\\s+", "=> "));
		int start = logPanel.getCharCount();
		try {
			for (StyleRange sr : s.getStyleRanges()) {
				sr.start += start;
				logPanel.setStyleRange(sr);
			}
		} catch (Exception e) {
			// should never happen
		}
	}

	private String removeTrailingSpaces(String s) {
		return s.trim();
	}


	private void evalExpression() {
		// We remove trailing spaces so that we do not embark extra spaces,
		// newlines, etc. for example when evaluating after having hit the
		// Enter key (which automatically adds a new line
		viewerWidget.setText(removeTrailingSpaces(viewerWidget.getText()));
		String s = viewerWidget.getText();
		if (s.trim().length() > 0) {
			try {
				program.send(s);
			} catch (Exception e) {
				SmlLog.logError(e);
			}
			
		}
		copyToLog(viewerWidget);
		viewerWidget.setText("");
	}

	private void installAutoEvalExpressionOnEnter() {
		viewerWidget.addVerifyKeyListener(new VerifyKeyListener() {
			private boolean enterAlonePressed(VerifyEvent e) {
				return (e.keyCode == SWT.LF || e.keyCode == SWT.CR)
						&& e.stateMask == SWT.NONE;
			}

			private boolean noSelection() {
				return viewerWidget.getSelectionCount() == 0;
			}

			private String textAfterCaret() {
				return viewerWidget.getText().substring(
						viewerWidget.getSelection().x);
			}

			@Override
			public void verifyKey(VerifyEvent e) {
				if (enterAlonePressed(e) && noSelection()
						&& textAfterCaret().trim().isEmpty()) {

					final String widgetText = viewerWidget.getText();

					// Executing evalExpression() via SWT's asyncExec mechanism,
					// we ensure all the normal behaviour is done by the Eclipse
					// framework on the Enter key, before sending the code.
					// For example, we are then able to get rid of a bug with
					// the content assistant which ensures the text is completed
					// with the selection before being sent for evaluation.
					DisplayUtil.asyncExec(new Runnable() {
						@Override
						public void run() {
							// we do not execute auto eval if some non-blank
							// text has
							// been added between the check and the execution
							final String text = viewerWidget.getText();
							int idx = text.indexOf(widgetText);
							if (idx == 0
									&& text.substring(widgetText.length())
											.trim().isEmpty()
											&& isValidExpression(text)) {
								evalExpression();
							}
						}
					});
				}
			}
		});
	}

	private static boolean isValidExpression(String text) {
		if(!text.trim().endsWith(";")) {
			return false;
		}
		int count = 0;
		for(char c : text.toCharArray()) {
			if(c == '(') {
				count++;
			} else if (c== ')') {
				count--;
			}
		}
		return count == 0;
	}
	
	private static class StyledTextPlaceHolder {
		private final StyledText st;
		private String placeholder;
		private boolean hasFocus;
		private boolean isPlaceholderDisplayed;

		public StyledTextPlaceHolder(final StyledText st, String p) {
			this.st = st;
			this.placeholder = p;
			this.hasFocus = st.isFocusControl();

			renderPlaceholder();

			st.addFocusListener(new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					hasFocus = false;
					renderPlaceholder();
				}

				@Override
				public void focusGained(FocusEvent e) {
					hasFocus = true;
					renderPlaceholder();
				}
			});
		}

		private void renderPlaceholder() {
			if (hasFocus) {
				if (isPlaceholderDisplayed) {
					isPlaceholderDisplayed = false;
					st.setText("");
					st.setStyleRange(null);
				}
			} else {
				if (isPlaceholderDisplayed || StringUtils.isEmpty(st.getText())) {
					isPlaceholderDisplayed = true;
					st.setText(StringUtils.safeString(placeholder));
					if (!StringUtils.isEmpty(placeholder)) {
						st.setStyleRange(new StyleRange(0,
								placeholder.length(), null, null, SWT.ITALIC));
					}
				}
			}
		}

		public void setPlaceHolder(final String placeholder) {
			this.placeholder = placeholder;
			renderPlaceholder();
		}

	}

}
