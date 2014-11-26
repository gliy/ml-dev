package in.iitd.mldev.ui.text;

import in.iitd.mldev.core.scan.SmlKeywords;
import in.iitd.mldev.ui.editor.SmlTextStyleProvider;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.BufferedRuleBasedScanner;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;

/** A scanner which scans for SML keywords. It should be run only on
 * document partitions which do not contain comments or strings. */
public class SmlTextStyleScanner extends BufferedRuleBasedScanner {

	private static final IToken OPEN_PAREN_TOKEN = new Token("(");
	private static final IToken CLOSE_PAREN_TOKEN = new Token(")");
	private Object[] rainbowParensData;
	private int currentParenLevel = 0;

	SmlTextStyleProvider styleProvider = new SmlTextStyleProvider();
	/** Creates a scanner that returns a token with the given data for
	 * SML keywords. */
	public SmlTextStyleScanner (Object data, Object[] rainbowParensData) {
		super();
		this.rainbowParensData = rainbowParensData;
		IToken keywordToken = new Token(data);
		WordRule alphaWords = new WordRule(new AlphaWordDetector(), new Token(null));
		
		for (String keyword : SmlKeywords.getKeywords()) {
			alphaWords.addWord(keyword, keywordToken);
		}
		setRules(new IRule[]{new ParenRule(), alphaWords});
	}
	@Override
	public IToken nextToken() {
		IToken nextToken = super.nextToken();
		if(nextToken == OPEN_PAREN_TOKEN) {
			Object data = rainbowParensData[currentParenLevel % rainbowParensData.length];
			currentParenLevel++;
			return new Token(data);
		} else if(nextToken == CLOSE_PAREN_TOKEN) {
			currentParenLevel--;
			Object data = rainbowParensData[currentParenLevel % rainbowParensData.length];
			return new Token(data);
		}
		return nextToken;
	}


	private static class ParenRule implements IRule {

		@Override
		public IToken evaluate(ICharacterScanner scanner) {
			int read = scanner.read();
			if(read == 40) {
				return OPEN_PAREN_TOKEN;
			} else if(read == 41) {
				return CLOSE_PAREN_TOKEN;
			}
			scanner.unread();
			return Token.UNDEFINED;
		}
		
	}
	/** A word detector which detects SML-style alphanumeric words. */
	private class AlphaWordDetector implements IWordDetector {
		public boolean isWordStart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z'));}
		public boolean isWordPart (char c) {return ((c>='a' && c<='z') || (c>='A' && c<='Z') || (c>='0' && c<='9') || c=='_' || c=='\'');}
	}

}
