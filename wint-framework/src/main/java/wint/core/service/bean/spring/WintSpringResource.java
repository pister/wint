package wint.core.service.bean.spring;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;

/**
 * @author pister 2012-3-4 11:42:22
 */
public class WintSpringResource extends AbstractResource {

	private wint.core.io.resource.Resource wintResource;
	
	public WintSpringResource(wint.core.io.resource.Resource wintResource) {
		super();
		this.wintResource = wintResource;
	}

	public InputStream getInputStream() throws IOException {
		return wintResource.getInputStream();
	}

	public String getDescription() {
		return wintResource.getDescription();
	}

	@Override
	public boolean exists() {
		return wintResource.exist();
	}

	@Override
	public Resource createRelative(String relativePath) throws IOException {
		wint.core.io.resource.Resource newWintResource =  wintResource.createRelative(relativePath);
		Resource ret = new WintSpringResource(newWintResource);
		return ret;
	}

}
