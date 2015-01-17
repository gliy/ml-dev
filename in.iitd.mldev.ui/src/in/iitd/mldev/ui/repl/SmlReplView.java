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

import java.util.Set;
import java.util.TreeSet;
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
import org.eclipse.jface.text.source.ISourceViewer;
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
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
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
	private String sessionId;
	private String secondaryId;
	public StyledText viewerWidget;
	private SourceViewerDecorationSupport fSourceViewerDecorationSupport;
	private static final Pattern boostIndent = Pattern.compile("^",
			Pattern.MULTILINE);
	public static final String VIEW_ID = "in.iitd.mldev.ui.smlREPLView";
	public static final AtomicReference<SmlReplView> activeREPL = new AtomicReference<SmlReplView>();
	private static Set<String> SECONDARY_VIEW_IDS = new TreeSet<String>() {
		{
			// no one will create more than 1000 REPLs at a time, right? :-P
			for (int i = 0; i < 1000; i++)
				add(String.format("%03d", i));
		}
	};
	private ISmlProcess program;

	private static String getSecondaryId() {
		synchronized (SECONDARY_VIEW_IDS) {
			String id = SECONDARY_VIEW_IDS.iterator().next();
			SECONDARY_VIEW_IDS.remove(id);
			return id;
		}
	}

	private static void releaseSecondaryId(String id) {
		assert id != null;

		synchronized (SECONDARY_VIEW_IDS) {
			SECONDARY_VIEW_IDS.add(id);
		}
	}

	public IConsole getConsole() {
		return console;
	}

	public static SmlReplView connect(IFile file,
			boolean makeActiveREPL) throws Exception {
		return connect(file, null, null, makeActiveREPL);
	}

	public static SmlReplView connect(final IFile file,
			IConsole console, ILaunch launch, final boolean makeActiveREPL)
			throws Exception {
		String secondaryId;
		final SmlReplView repl = (SmlReplView) PlatformUI
				.getWorkbench()
				.getActiveWorkbenchWindow()
				.getActivePage()
				.showView(VIEW_ID, secondaryId = getSecondaryId(),
						IWorkbenchPage.VIEW_ACTIVATE);
		repl.secondaryId = secondaryId;
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
			public void lineRead(String file, final String line) {
				DisplayUtil.asyncExec(new Runnable() {
					
					@Override
					public void run() {
						logPanel.append(line + "\n");
					}
				});
				
			}
		});;
		
	}

	@Override
	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		activeREPL.set(SmlReplView.this);
	}

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
		viewer.configure(viewerConfig);

		viewerWidget = viewer.getTextWidget();

		getViewSite().setSelectionProvider(viewer);
		viewer.setDocument(new Document());
		final StyledText st = (StyledText) viewer.getControl();
		setPlaceHolder(st, "<type SML code here>");
		
		
		installMessageDisplayer(viewerWidget, new MessageProvider() {
			@Override
			public String getMessageText() {
				return getEvaluationHint();
			}
		});
		
		installAutoEvalExpressionOnEnter();
		
	}
	
	private interface MessageProvider {
    	String getMessageText();
    }
	
    private void installMessageDisplayer(final StyledText textViewer, final MessageProvider hintProvider) {
		textViewer.addListener(SWT.Paint, new Listener() {
		    private int getScrollbarAdjustment () {
		        if (!Platform.getOS().equals(Platform.OS_MACOSX)) return 0;

		        // You cannot reliably determine if a scrollbar is visible or not
                //   (http://stackoverflow.com/questions/5674207)
                // So, we need to determine if the vertical scrollbar is "needed" based
                // on the contents in the viewer
                Rectangle clientArea = textViewer.getClientArea();
                int textLength = textViewer.getText().length();
                if (textLength == 0 || textViewer.getTextBounds(0, Math.max(0, textLength - 1)).height < clientArea.height) {
                    return textViewer.getVerticalBar().getSize().x;
                } else {
                    return 0;
                }
		    }

			@Override
			public void handleEvent(Event event) {
				String message = hintProvider.getMessageText();
				if (message == null)
					return;

                // keep the 'tooltip' using the default font
                event.gc.setFont(JFaceResources.getFont(JFaceResources.DEFAULT_FONT));

				Point topRightPoint = topRightPoint(textViewer.getClientArea());
				int sWidth = textWidthPixels(message, event);
				int x = Math.max(topRightPoint.x - sWidth + getScrollbarAdjustment(), 0);
				int y = topRightPoint.y;

				// the text widget doesn't know we're painting the hint, so it won't necessarily
				// clear old presentations of it; this leads to streaking on Windows if we don't
				// clear the foreground explicitly
				Color fg = event.gc.getForeground();
				event.gc.setForeground(event.gc.getBackground());
				event.gc.drawRectangle(textViewer.getClientArea());
				event.gc.setForeground(fg);
				event.gc.setAlpha(200);
				event.gc.drawText(message, x, y, true);
			}

			private Point topRightPoint(Rectangle clipping) {
				return new Point(clipping.x + clipping.width, clipping.y);
			}

			private int textWidthPixels(String text, Event evt) {
				int width = 0;
				for (int i = 0; i < text.length(); i++) {
					width += evt.gc.getAdvanceWidth(text.charAt(i));
				}
				return width;
			}
		});
    }

    private String getEvaluationHint() {
    	if (!getPreferences().getBoolean(PreferenceConstants.SML_REPL_HINTS))
    		return null;
    	return "??";

//    	if (getPreferences().getBoolean(PreferenceConstants.REPL_VIEW_AUTO_EVAL_ON_ENTER_ACTIVE)) {
//    		return Messages.REPLView_autoEval_on_Enter_active;
//    	} else {
//    		return Messages.format(Messages.REPLView_autoEval_on_Enter_inactive,
//    				Platform.getOS().equals(Platform.OS_MACOSX)
//    					? "Cmd"
//    				    : "Ctrl");
//    	}
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
		// TODO Auto-generated method stub

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
		// ranges...
		s.setText(boostIndent.matcher(s.getText()).replaceAll("   ")
				.replaceFirst("^\\s+", "=> "));
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

	public void evalExpression(String s, boolean addToHistory,
			boolean printToLog, boolean repeatLastREPLEvalIfActive) {
		try {
			if (s.trim().length() > 0) {
				program.send(s);
				
				// if (printToLog) viewHelpers._("log", this, logPanel, s,
				// inputExprLogType);

			//	final Object ret = evalExpression.invoke(s, addToHistory);

				// if (repeatLastREPLEvalIfActive &&
				// autoRepeatLastAction.isChecked()) {
				// final String lastREPLExpr =
				// getLastExpressionSentFromREPL();
				// if (!StringUtils.isBlank(lastREPLExpr)) {
				// new Thread(new Runnable() {
				// @Override
				// public void run() {
				// if (hasEvalResponseException(ret))
				// return;
				// evalExpression(lastREPLExpr, false, false, false);
				// }
				// }).start();
				// }
				// }
			}

		} catch (Exception e) {

			SmlLog.logError(e);
		}
	}

	private void evalExpression() {
		// We remove trailing spaces so that we do not embark extra spaces,
		// newlines, etc. for example when evaluating after having hit the
		// Enter key (which automatically adds a new line
		viewerWidget.setText(removeTrailingSpaces(viewerWidget.getText()));
		evalExpression(viewerWidget.getText(), true, false, false);
		if (viewerWidget.getText().trim().length() > 0) {
			// lastExpressionSentFromREPL = viewerWidget.getText();
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
											.trim().isEmpty()) {
								evalExpression();
							}
						}
					});
				}
			}
		});
	}

//	protected SourceViewerDecorationSupport getSourceViewerDecorationSupport(ISourceViewer viewer) {
//		if (fSourceViewerDecorationSupport == null) {
//			fSourceViewerDecorationSupport= new SourceViewerDecorationSupport(
//					viewer,
//					null/*getOverviewRuler()*/,
//					null/*getAnnotationAccess()*/,
//					EditorsPlugin.getDefault().getSharedTextColors()/*getSharedColors()*/
//					);
//			editorSupport._("configureSourceViewerDecorationSupport",
//					fSourceViewerDecorationSupport, viewer);
//			fSourceViewerDecorationSupport.
//		}
//		return fSourceViewerDecorationSupport;
//	}
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
