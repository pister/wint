package wint.tools.serialize;

import wint.tools.util.FastByteArrayInputStream;
import wint.tools.util.FastByteArrayOutputStream;

public abstract class AbstractSerializeService implements SerializeService {

	public byte[] serialize(Object o) {
        if (o == null) {
            return null;
        }
		FastByteArrayOutputStream bos = new FastByteArrayOutputStream(128);
		serialize(o, bos);
		return bos.toByteArray();
	}

	public Object deserialize(byte[] b) {
        if (b == null) {
            return null;
        }
		FastByteArrayInputStream bis = new FastByteArrayInputStream(b);
		return deserialize(bis);
	}

}
