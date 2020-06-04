package wint.sessionx.serialize;

import com.github.pister.tson.Tsons;

/**
 * Created by songlihuang on 2020/6/4.
 */
public class TSonSerializeService implements SerializeService {
    @Override
    public Object serialize(Object input) {
        return Tsons.encode(input);
    }

    @Override
    public Object unserialize(Object src) {
        if (src == null) {
            return null;
        }
        return Tsons.decode(src.toString());
    }
}
