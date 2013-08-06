package wint.tools.search;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import wint.tools.util.IoUtil;
import wint.tools.util.ResourceUtil;

public class StopWords {
	
	final static List<String> stopWordsEn = Arrays.asList(
		      "a", "an", "and", "are", "as", "at", "be", "but", "by",
		      "for", "if", "in", "into", "is", "it",
		      "no", "not", "of", "on", "or", "such",
		      "that", "the", "their", "then", "there", "these",
		      "they", "this", "to", "was", "will", "with"
		    );
	
	public static Set<String> getStopWords() {
		InputStream is = ResourceUtil.getResourceStream("stopwords/stopwords-cn.txt");
		if (is == null) {
			return new HashSet<String>();
		}
		String content = IoUtil.readToString(new InputStreamReader(is));
		String[] words = content.split(",");
		Set<String> ret = new HashSet<String>(words.length);
		for (String word : words) {
			ret.add(word.trim());
		}
		ret.addAll(stopWordsEn);
		return ret;
	}
	
	public static void main(String[] args) {
		Set<String> s =  getStopWords();
		System.out.println(s);
	}

}
