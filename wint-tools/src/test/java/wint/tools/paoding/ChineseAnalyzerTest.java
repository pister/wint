package wint.tools.paoding;

import java.io.IOException;
import java.io.StringReader;

import junit.framework.TestCase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.TermAttribute;

import wint.tools.ikanalyzer.lucene.IKAnalyzer;

public class ChineseAnalyzerTest extends TestCase {
	
	public void testPaodingAnalyzer() throws IOException {
		Analyzer analyzer = new IKAnalyzer(); //new StandardAnalyzer(Version.LUCENE_30, StopWords.getStopWords());;
		String test = "你好，这就是搜索引擎，还是数学之美？";
		TokenStream tokenStream = analyzer.tokenStream("xxx", new StringReader(test));
		TermAttribute termAttribute = tokenStream.getAttribute(TermAttribute.class);
		while (tokenStream.incrementToken()) {
			String term = termAttribute.term();
			System.out.println(term);
		}
	}

}
