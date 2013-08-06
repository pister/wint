package wint.tools.similar.proccess.steps;

import java.io.StringReader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;

import wint.tools.similar.term.TermAnalyzer;

public class LuceneTermAnalyzer implements TermAnalyzer {

	private Analyzer analyzer;
	
	public LuceneTermAnalyzer(Analyzer analyzer) {
		super();
		this.analyzer = analyzer;
	}

	public Iterator<String> analyze(String content) {
		TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
		return new TokenStreamIterator(tokenStream);
	}

}
