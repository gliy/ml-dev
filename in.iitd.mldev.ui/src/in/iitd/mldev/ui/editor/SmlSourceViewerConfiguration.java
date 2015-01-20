package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.SmlCorePlugin;
import in.iitd.mldev.core.scan.SmlPartitionScanner;
import in.iitd.mldev.core.scan.SmlTokenTypes;
import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;
import in.iitd.mldev.ui.text.SingleTokenScanner;
import in.iitd.mldev.ui.text.SmlTextStyleScanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.DefaultAnnotationHover;
import org.eclipse.jface.text.source.IAnnotationHover;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;

/** Bundles the configuration of an SmlEditor. Sets up its default document
 * partitioning, syntax highlighting, tab width, and the reconciler that
 * updates the program model. Except for the constructor, all the methods are
 * called only by Eclipse to determine the configuration of the editor. */
public class SmlSourceViewerConfiguration extends TextSourceViewerConfiguration {

	/** The editor that this instance configures. */
	private ISmlEditor editor;

	/** Creates a source viewer configuration to configure the given editor.
	 * It should only be used with this editor. */
	public SmlSourceViewerConfiguration (ISmlEditor editor) {
		this.editor = editor;
	}
	
	/** Returns the content types to be configured in an SML document.
	 * See SmlPartitionScanner. */
	public String[] getConfiguredContentTypes (ISourceViewer sourceViewer) {
		return SmlPartitionScanner.CONTENT_TYPES;
	}

	/** Returns the default partitioning for an SML document.
	 * See SmlPartitionScanner. */
	public String getConfiguredDocumentPartitioning (ISourceViewer sourceViewer) {
		return SmlCorePlugin.SML_PARTITIONING;
	}
	
	@Override
	public IAnnotationHover getAnnotationHover(ISourceViewer sourceViewer) {
		return new DefaultAnnotationHover();
	}
	
	
	/** Returns a presentation reconciler for performing syntax highlighting. */
	public IPresentationReconciler getPresentationReconciler (ISourceViewer viewer) {
		PresentationReconciler reconciler = new PresentationReconciler();
		reconciler.setDocumentPartitioning(getConfiguredDocumentPartitioning(viewer));
		
		SmlTextStyleProvider styleProvider = new SmlTextStyleProvider();
        
		DefaultDamagerRepairer damageRepairer;
        
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getCommentStyle()));
		reconciler.setDamager(damageRepairer, SmlTokenTypes.COMMENT);
		reconciler.setRepairer(damageRepairer, SmlTokenTypes.COMMENT);
        
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getStringStyle()));
		reconciler.setDamager(damageRepairer, SmlTokenTypes.STRING);
		reconciler.setRepairer(damageRepairer, SmlTokenTypes.STRING);
        
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getStringStyle()));
		reconciler.setDamager(damageRepairer, SmlTokenTypes.CHAR);
		reconciler.setRepairer(damageRepairer, SmlTokenTypes.CHAR);
		
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getIntegerStyle()));
		reconciler.setDamager(damageRepairer, SmlTokenTypes.INT);
		reconciler.setRepairer(damageRepairer, SmlTokenTypes.INT);
		
		damageRepairer = new DefaultDamagerRepairer(new SingleTokenScanner(styleProvider.getRealStyle()));
		reconciler.setDamager(damageRepairer, SmlTokenTypes.REAL);
		reconciler.setRepairer(damageRepairer, SmlTokenTypes.REAL);
        
		damageRepairer = new DefaultDamagerRepairer(new SmlTextStyleScanner(styleProvider.getKeywordStyle(), styleProvider.getRainbowParensStyle()));
		reconciler.setDamager(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(damageRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		
		return reconciler;
    }
    
	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new SmlContentAssistProcessor(editor), IDocument.DEFAULT_CONTENT_TYPE);
		assistant.enableAutoActivation(true);
		return assistant;
	}
	
	
	/** Returns the width that a tab character should be displayed with. */
    public int getTabWidth (ISourceViewer viewer) {
    	return SmlUiPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.SML_TAB_WIDTH);
    }
    
    /** Returns the reconciler that will updates the program model
     * in response to changes in the editor's document. */
    public IReconciler getReconciler (ISourceViewer viewer) {
    	MonoReconciler reconciler = new MonoReconciler(new SmlReconcilingStrategy(editor), false);
    	return reconciler;
    }

    
    @Override
    public IQuickAssistAssistant getQuickAssistAssistant(
    		ISourceViewer sourceViewer) {
    	return super.getQuickAssistAssistant(sourceViewer);
    }
}
