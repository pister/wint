package wint.tools.serialize;

import java.io.InputStream;
import java.io.OutputStream;

public interface SerializeService {
	
	byte[] serialize(Object o);
	
	Object deserialize(byte[] b);

	void serialize(Object o, OutputStream os);
	
	public Object deserialize(InputStream is);
	
}
