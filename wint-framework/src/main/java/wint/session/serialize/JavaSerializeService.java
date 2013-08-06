package wint.session.serialize;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import wint.lang.WintException;
import wint.lang.io.FastByteArrayInputStream;
import wint.lang.io.FastByteArrayOutputStream;

public class JavaSerializeService implements SerializeService {

	public Object serialize(Object input) {
		try {
			FastByteArrayOutputStream bos = new FastByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(input);
			return bos.toByteArray();
		} catch (Exception e) {
			throw new WintException(e);
		}
	}

	public Object unserialize(Object src) {
		try {
			FastByteArrayInputStream bis = new FastByteArrayInputStream((byte[])src);
			ObjectInputStream ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (Exception e) {
			throw new WintException(e);
		}
	}

}
