package wint.mvc.url;

import wint.mvc.view.Render;

public interface UrlModule extends Render {
	
	UrlBroker setTarget(String target);

}
