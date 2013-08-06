package wint.tools.similar.proccess.steps;

import org.apache.lucene.analysis.Analyzer;

import wint.tools.similar.term.TermAnalyzer;

public class LuceneAnalyzeStep extends AnalyzeTermStep {

	private LuceneTermAnalyzer termAnalyzer;
	
	public LuceneAnalyzeStep(Analyzer analyzer) {
		super();
		this.termAnalyzer = new LuceneTermAnalyzer(analyzer);
	}

	@Override
	protected TermAnalyzer getTermAnalyzer() {
		return termAnalyzer;
	}

}
