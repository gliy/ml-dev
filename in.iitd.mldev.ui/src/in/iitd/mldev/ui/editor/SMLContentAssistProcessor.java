package in.iitd.mldev.ui.editor;

import in.iitd.mldev.core.model.SmlBinding;
import in.iitd.mldev.core.model.SmlProgram;
import in.iitd.mldev.core.parse.ast.ASTRoot;
import in.iitd.mldev.core.parse.ast.CaseExp;
import in.iitd.mldev.core.parse.ast.Clause;
import in.iitd.mldev.core.parse.ast.Dec;
import in.iitd.mldev.core.parse.ast.Exp;
import in.iitd.mldev.core.parse.ast.FlatAppExp;
import in.iitd.mldev.core.parse.ast.FlatConPat;
import in.iitd.mldev.core.parse.ast.FunDec;
import in.iitd.mldev.core.parse.ast.LetExp;
import in.iitd.mldev.core.parse.ast.Pat;
import in.iitd.mldev.core.parse.ast.StringPat;
import in.iitd.mldev.core.parse.ast.ValDec;
import in.iitd.mldev.core.parse.ast.VarPat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class SMLContentAssistProcessor implements IContentAssistProcessor {

	private SmlEditor editor;
	public SMLContentAssistProcessor(SmlEditor editor) {
		super();
		this.editor = editor;
	}

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int offset) {
		//viewer.getDocument().g
		return computeProposals(offset, findPrefix(viewer, offset));
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;//new char[]{'a','b','c'};
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
		
		List<Dec> decs = program.getParseTree().decs;
		SMLProposalGenerator generator = new SMLProposalGenerator(offset, prefix);
		for (int i = 0; i < decs.size() && decs.get(i).getLeft() < offset; i++) {
			Dec dec = decs.get(i);
			List<ICompletionProposal> proposals = generator.parseDec(dec);
			rtn.addAll(proposals);
		}
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
	
	private static class SMLProposalGenerator {
		private int offset;
		private String prefix;
		
		public SMLProposalGenerator(int offset, String prefix) {
			super();
			this.offset = offset;
			this.prefix = prefix;
		}

		private List<ICompletionProposal> parseDec( Dec dec) {
			if(dec instanceof FunDec) {
				return parseFunDec((FunDec)dec);
			} else if (dec instanceof ValDec) {
				return parseValDec((ValDec)dec);
			}
			return list();
		}
		
		private  List<ICompletionProposal> parseFunDec( FunDec dec) {
			Clause c = dec.bindings.get(0).clauses.get(0);
			
			String funcName = ((VarPat)c.pats.get(0)).ident.name;
			List<ICompletionProposal> rtn = list();
			rtn.add(createProposal(funcName));
			rtn.addAll(parseExp(c.exp));
			return rtn;
		}

		private List<ICompletionProposal> parseExp(Exp exp) {
			if (exp instanceof LetExp) {
				return parseLetExp(((LetExp) exp));
			} else if (exp instanceof CaseExp) {

			} else if (exp instanceof FlatAppExp) {
				List<ICompletionProposal> rtn = list();
				for (Exp childExp : ((FlatAppExp) exp).exps) {
					rtn.addAll(parseExp(childExp));
				}
				return rtn;
			}
			return list();
		}

		private List<ICompletionProposal> parseLetExp(
				LetExp letExp) {
			if(letExp.getLeft() < offset && letExp.getRight() > offset) {
				List<ICompletionProposal> rtn = list();
				for (Dec dec : letExp.decs) {
					rtn.addAll(parseDec(dec));
				}
				return rtn;
			}
			return list();
		}

		private List<ICompletionProposal> parseValDec(ValDec dec) {
			return list(createProposal(((VarPat) ((FlatConPat) dec.bindings
					.get(0).pat).pats.get(0)).ident.name));
		}
		
		private ICompletionProposal createProposal( String name) {
			return createProposal(name, null);
		}
		private ICompletionProposal createProposal( String name, String additional) {
			String append = additional != null ? ":" + additional : "";
			if (!name.startsWith(prefix)) {
				return null;
			}
			return new CompletionProposal(name + " ", offset - prefix.length(), prefix.length(), name.length() + 1,
					null, name + append, null, null);
		}
		private static List<ICompletionProposal> list(ICompletionProposal ... proposals) {
			ArrayList<ICompletionProposal> rtn = new ArrayList<ICompletionProposal>();
			if(proposals != null) {
				for (ICompletionProposal proposal : proposals) {
					if(proposal != null) {
						rtn.add(proposal);
					}
				}
			}
			return rtn;
		}
	}

	
}
