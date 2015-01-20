package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.core.parse.ast.Dec;
import in.iitd.mldev.process.background.ISmlModule;
import in.iitd.mldev.process.background.SmlFunction;
import in.iitd.mldev.process.background.SmlModule;
import in.iitd.mldev.process.background.SmlObject;
import in.iitd.mldev.process.background.SmlObject.SmlType;
import in.iitd.mldev.process.background.SmlVal;
import in.iitd.mldev.process.background.parse.ISmlParseListener;
import in.iitd.mldev.ui.SmlUiPlugin;
import in.iitd.mldev.ui.handler.SmlParseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import org.eclipse.swt.graphics.Image;

public class SmlContentAssistProcessor extends TemplateCompletionProcessor {

	private ISmlEditor editor;

	public SmlContentAssistProcessor(ISmlEditor editor) {
		super();
		this.editor = editor;

	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		// viewer.getDocument().g

		List<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();
		String prefix = findPrefix(viewer, offset);
		rtn.addAll(Arrays.asList(computeProposals(offset, prefix)));
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
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return "abcdefg".toCharArray();
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
		// for (int i = 0; i < decs.size() && decs.get(i).getLeft() < offset;
		// i++) {
		// Dec dec = decs.get(i);
		// List<ICompletionProposal> proposals = generator.parseDec(dec);
		// for (ICompletionProposal proposal : proposals) {
		// boolean exists = false;
		// for (ICompletionProposal existing : proposals) {
		// if (existing.getDisplayString().equals(
		// proposal.getDisplayString())) {
		// exists = true;
		// break;
		// }
		// }
		// if (!exists) {
		// rtn.add(proposal);
		// }
		// }
		// }
		return rtn.toArray(new ICompletionProposal[0]);
	}

	private static String findPrefix(ITextViewer viewer, int offset) {
		IDocument doc = viewer.getDocument();
		StringBuilder rtn = new StringBuilder();

		for (int i = offset; i > 0; i--) {
			try {
				char c = doc.getChar(i);

				if (Character.isAlphabetic(c)) {
					rtn.append(c);
				} else {
					break;
				}
			} catch (BadLocationException e) {
			}
		}

		return rtn.reverse().toString();
	}

	private class SMLProposalGenerator {
		private int offset;
		private Prefix prefix;
		private boolean isFunction;

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

		// private List<ICompletionProposal> parseDec(Dec dec) {
		// if (dec instanceof FunDec) {
		// return parseFunDec((FunDec) dec);
		// } else if (dec instanceof ValDec) {
		// return parseValDec((ValDec) dec);
		// }
		// return list();
		// }
		//
		// private List<ICompletionProposal> parseFunDec(FunDec dec) {
		// List<ICompletionProposal> rtn = list();
		// for (FunBind bind : dec.bindings) {
		// rtn.addAll(parseFunBinding(bind));
		// }
		//
		// return rtn;
		// }
		//
		// private List<ICompletionProposal> parseFunBinding(FunBind bind) {
		// List<ICompletionProposal> rtn = list();
		// rtn.addAll(parseClause(bind.clauses.get(0)));
		//
		// return rtn;
		// }
		//
		// private List<ICompletionProposal> parseClause(Clause clause) {
		// List<ICompletionProposal> rtn = list();
		//
		// // if inside of the function add parameter names
		// if (clause.getLeft() <= offset && offset <= clause.getRight()) {
		// for (int i = 1; i < clause.pats.size(); i++) {
		// rtn.addAll(parsePat(clause.pats.get(i)));
		// }
		// // otherwise only add function name
		// } else {
		// rtn.addAll(parsePat(clause.pats.get(0)));
		// }
		//
		// rtn.addAll(parseExp(clause.exp));
		// return rtn;
		// }
		//
		// private List<ICompletionProposal> parsePat(Pat p) {
		// if (p instanceof VarPat)
		// return parseVarPat((VarPat) p);
		// return list();
		// }
		//
		// private List<ICompletionProposal> parseVarPat(VarPat pat) {
		// return list(createProposal(pat.ident.name));
		// }
		//
		// private List<ICompletionProposal> parseExp(Exp exp) {
		// if (exp instanceof LetExp) {
		// return parseLetExp(((LetExp) exp));
		// } else if (exp instanceof CaseExp) {
		//
		// } else if (exp instanceof FlatAppExp) {
		// List<ICompletionProposal> rtn = list();
		// for (Exp childExp : ((FlatAppExp) exp).exps) {
		// rtn.addAll(parseExp(childExp));
		// }
		// return rtn;
		// }
		// return list();
		// }
		//
		// private List<ICompletionProposal> parseLetExp(LetExp letExp) {
		// if (letExp.getLeft() < offset && letExp.getRight() > offset) {
		// List<ICompletionProposal> rtn = list();
		// for (Dec dec : letExp.decs) {
		// rtn.addAll(parseDec(dec));
		// }
		// return rtn;
		// }
		// return list();
		// }
		//
		// private List<ICompletionProposal> parseValDec(ValDec dec) {
		// return list(createProposal(((VarPat) ((FlatConPat) dec.bindings
		// .get(0).pat).pats.get(0)).ident.name));
		// }

		private ICompletionProposal createProposal(String name) {
			return createProposal(name, null);
		}

		private ICompletionProposal createProposal(String name,
				String additional) {
			String append = additional != null ? " : " + additional : "";
			if (!name.startsWith(prefix.toString())) {
				return null;
			}
			return new CompletionProposal(name + " ", offset
					- prefix.toString().length(), prefix.toString().length(),
					name.length() + 1, null, name + append, null, null);
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
			SmlModule root = editor.getRoot();
			if (root == null) {
				return list();
			}
			return parseOutput(root, true);

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

		private List<SmlObject> filterObjects(List<SmlObject> objs) {
			List<SmlObject> rtn = new ArrayList<SmlObject>();
			for (SmlObject obj : objs) {
				if (obj.getType() != SmlType.EXCEPTION_TYPE) {
					if (isFunction && obj.getType() == SmlType.FN_TYPE) {
						rtn.add(obj);
					} else if (!isFunction && obj.getType() != SmlType.FN_TYPE) {
						rtn.add(obj);
					}
				}
			}
			return rtn;
		}

		private ICompletionProposal createProposal(SmlObject obj) {
			if (obj.getClass() == SmlFunction.class) {
				SmlFunction fn = (SmlFunction) obj;
				return createProposal(fn.getName(), fn.params());
			} else {
				return createProposal(obj.getName(), obj.toString());

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
