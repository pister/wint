package wint.tools.similar.proccess;

import wint.tools.similar.content.Content;

public interface ProccessStep {
	
	void execute(Content content, ProcessContext processContext);

}
