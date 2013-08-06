package wint.tools.similar.term;

import java.util.Iterator;

public interface TermAnalyzer {
	
	Iterator<String> analyze(String content);

}
