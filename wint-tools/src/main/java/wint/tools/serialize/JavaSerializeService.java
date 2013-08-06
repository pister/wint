package wint.tools.serialize;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class JavaSerializeService extends AbstractSerializeService implements SerializeService {

	public void serialize(Object o, OutputStream os) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(os);
			oos.writeObject(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public Object deserialize(InputStream is) {
		try {
			ObjectInputStream ois = new ObjectInputStream(is);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
