package wint.tools.similar.proccess.steps;

import java.io.IOException;
import java.util.Iterator;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

public class TokenStreamIterator implements Iterator<String> {

	private TermAttribute termAttribute;
	
	private TokenStream tokenStream;
	
	public TokenStreamIterator(TokenStream tokenStream) {
		super();
		this.tokenStream = tokenStream;
		this.termAttribute = tokenStream.getAttribute(TermAttribute.class);
	}

	public boolean hasNext() {
		try {
			return tokenStream.incrementToken();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String next() {
		return termAttribute.term();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

}
