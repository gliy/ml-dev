package in.iitd.mldev.core.scan;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/** A rule that matches an SML character literal. Used by SmlTokenScanner. */
public class SmlCharRule implements IRule {
	private IToken token;
	public SmlCharRule (IToken successToken) {token = successToken;}
	public IToken evaluate (ICharacterScanner scanner) {
		if (scanner.read() != '#') {return unread(scanner, 1);}
		if (scanner.read() != '"') {return unread(scanner, 2);}
		
		int r = scanner.read();
		int unreadCount = 3;
		if (r == '"') {
			return unread(scanner, unreadCount);
		} else if(r == ICharacterScanner.EOF) {
			return unread(scanner, unreadCount);
		} else if (r == '\\') {
			unreadCount += readEscSeq(scanner);
		} 
		r = scanner.read();
		unreadCount++;
		if (r != '"') {
			return unread(scanner, unreadCount);
		}
		return token;
	}
	private int readEscSeq (ICharacterScanner scanner) {
		int readCount = 1;
		if (isWhitespace(scanner.read())) {
			while (isWhitespace(scanner.read())) {readCount++;};
			scanner.unread();
		}
		return readCount;
	}
	
	private IToken unread(ICharacterScanner scanner, int count) {
		for (int i = 0; i < count; i++) {
			scanner.unread();
		}
		return Token.UNDEFINED;
	}
	private boolean isWhitespace (int c) {return (c==' ' || c=='\t' || c=='\f' || c=='\r' || c=='\n');}
}