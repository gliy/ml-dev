package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.process.background.ISmlModule;
import in.iitd.mldev.process.background.SmlFunction;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.process.background.SmlObject;
import in.iitd.mldev.process.background.SmlObject.SmlType;
import in.iitd.mldev.ui.PreferenceConstants;
import in.iitd.mldev.ui.SmlUiPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.jface.text.templates.Template;
import org.eclipse.jface.text.templates.TemplateCompletionProcessor;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.graphics.Image;

public class SmlContentAssistProcessor extends TemplateCompletionProcessor {

	private ISmlEditor editor;
	private char[] autoActivateChars;

	public SmlContentAssistProcessor(ISmlEditor editor) {
		super();
		this.editor = editor;
		IPreferenceStore store = SmlUiPlugin.getDefault().getPreferenceStore();
		autoActivateChars = store.getString(PreferenceConstants.SML_AA_CHARS)
				.toCharArray();
		store.addPropertyChangeListener(new IPropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty()
						.equals(PreferenceConstants.SML_AA_CHARS)) {
					autoActivateChars = event.getNewValue().toString()
							.toCharArray();
				}
			}
		});

	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		// viewer.getDocument().g
		List<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();
		String prefix = findPrefix(viewer, offset);
		System.out.println("Computing proposals " + prefix);
		rtn.addAll(Arrays.asList(computeProposals(offset, prefix)));

		// adds templates
		rtn.addAll(Arrays.asList(filterByPrefix(
				super.computeCompletionProposals(viewer, offset), prefix)));
		List<ICompletionProposal> rtnWithoutNulls = new ArrayList<ICompletionProposal>();
		for (ICompletionProposal cp : rtn) {
			if (cp != null) {
				rtnWithoutNulls.add(cp);
			}
		}
		return rtnWithoutNulls.toArray(new ICompletionProposal[0]);
	}

	private ICompletionProposal[] filterByPrefix(
			ICompletionProposal[] proposals, String prefix) {
		List<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();
		for (ICompletionProposal proposal : proposals) {
			if (proposal.getDisplayString().startsWith(prefix)) {
				rtn.add(proposal);
			}
		}
		return rtn.toArray(new ICompletionProposal[0]);
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		System.out.println("Computing context");
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return autoActivateChars;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

	private ICompletionProposal[] computeProposals(int offset, String prefix) {
		SmlProgram program = editor.getProgram();
		List<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();

		// List<Dec> decs = program.getParseTree().decs;
		SMLProposalGenerator generator = new SMLProposalGenerator(offset,
				Prefix.create(prefix), program.getDocument());
		rtn.addAll(generator.computeProgramProposals());
		return rtn.toArray(new ICompletionProposal[0]);
	}

	private static String findPrefix(ITextViewer viewer, int offset) {
		IDocument doc = viewer.getDocument();
		StringBuilder rtn = new StringBuilder();
		try {
			int lineLength = doc.getLineLength(doc.getLineOfOffset(offset));
			if (lineLength > 0) {
				offset = offset == doc.getLength() ? offset - 1 : offset;
				for (int i = offset; i > offset - lineLength; i--) {

					char c = doc.getChar(i);

					if (Character.isAlphabetic(c) || "-".indexOf(c) >= 0) {
						rtn.append(c);
					} else if ("\r\n".indexOf(c) < 0) {
						break;
					}

				}
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return rtn.reverse().toString();
	}

	private class SMLProposalGenerator {
		private int offset;
		private Prefix prefix;
		private boolean isFunction;

		private final List<String> DEFAULT_PROPOSALS = Arrays.asList("val",
				"fun", "@", "!", ":=", "ref", "fn", "case", "if", "in", "infix","raise",
				"then","else", "let");
		public SMLProposalGenerator(int offset, Prefix prefix,
				IDocument document) {
			super();
			this.offset = offset;
			this.prefix = prefix;

			try {
				isFunction = document.getChar(offset
						- prefix.toString().length() - 1) == '(';
			} catch (BadLocationException ex) {
			}
		}

		private ICompletionProposal createProposal(String name) {
			return createProposal(name, null, null);
		}

		private ICompletionProposal createProposal(String name,
				String additional, Image image) {
			String append = additional != null ? " : " + additional : "";
			if (!name.startsWith(prefix.toString())) {
				return null;
			}
			return new CompletionProposal(name + " ", offset
					- prefix.toString().length(), prefix.toString().length(),
					name.length() + 1, image, name + append, null, null);
		}

		private List<ICompletionProposal> list(ICompletionProposal... proposals) {
			ArrayList<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();
			if (proposals != null) {
				for (ICompletionProposal proposal : proposals) {
					if (proposal != null) {
						rtn.add(proposal);
					}
				}
			}
			return rtn;
		}

		private List<ICompletionProposal> computeProgramProposals() {
			List<ICompletionProposal> defaults = new ArrayList<ICompletionProposal>(
					createDefaultProposals());
			SmlModule root = editor.getRoot();
			if (root == null) {
				return defaults;
			}
			defaults.addAll(parseOutput(root, true));
			return defaults;

		}

		private List<ICompletionProposal> createDefaultProposals() {
			List<ICompletionProposal> rtn = list();
			for (String key : DEFAULT_PROPOSALS) {
				//rtn.add(createProposal(key));
			}
			return rtn;
		}

		private List<ICompletionProposal> parseOutput(ISmlModule module,
				boolean root) {
			List<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();

			for (SmlObject obj : filterObjects(module.getDeclaredObjects())) {
				rtn.add(createProposal(obj));
			}
			for (ISmlModule child : module.getModules()) {
				rtn.addAll(parseOutput(child, false));
			}

			return rtn;
		}

		private List<SmlObject> filterObjects(Iterable<SmlObject> objs) {
			List<SmlObject> rtn = new ArrayList<SmlObject>();
			for (SmlObject obj : objs) {
				if (isFunction && obj.getType() == SmlType.FN_TYPE) {
					rtn.add(obj);
				} else if (!isFunction && obj.getType() != SmlType.FN_TYPE) {
					rtn.add(obj);
				}
			}
			return rtn;
		}

		private ICompletionProposal createProposal(SmlObject obj) {
			Image img = obj.getType().getImage();
			if (obj.getClass() == SmlFunction.class) {
				SmlFunction fn = (SmlFunction) obj;
				return createProposal(fn.getName(), fn.params(), img);
			} else {
				return createProposal(obj.getName(), obj.toString(), img);

			}
		}
	}

	@Override
	protected Template[] getTemplates(String contextTypeId) {
		return SmlUiPlugin.getDefault().getTemplateStore()
				.getTemplates(contextTypeId);
	}

	@Override
	protected TemplateContextType getContextType(ITextViewer viewer,
			IRegion region) {
		// TODO Auto-generated method stub
		return SmlUiPlugin.getDefault().getRegistry()
				.getContextType("in.iitd.mldev.ui.SMLContextType");
	}

	@Override
	protected Image getImage(Template template) {
		// TODO Auto-generated method stub
		return null;
	}

	private static class Prefix {
		private Prefix next;
		private String value;

		private Prefix(String value) {
			this.value = value;
		}

		private Prefix append(String value) {
			this.next = new Prefix(value);
			return this.next;
		}

		public static Prefix create(String value) {
			return create(value.split("\\."));
		}

		public static Prefix create(String[] values) {
			Prefix start = new Prefix("");
			if (values.length > 0) {
				start = new Prefix(values[0]);
				Prefix next = start;
				for (int i = 1; i < values.length; i++) {
					next = next.append(values[i]);
				}
			}
			return start;
		}

		@Override
		public int hashCode() {
			return value.hashCode() * (next == null ? 1 : next.hashCode());
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj == null || !(obj instanceof Prefix)) {
				return false;
			}

			Prefix o = (Prefix) obj;

			return o.value.equals(value)
					&& ((o.next == null && next == null) || (o.next != null && o.next
							.equals(next)));

		}

		@Override
		public String toString() {
			if (next != null) {
				return value + "." + next.toString();
			}
			return value;
		}

	}

}
