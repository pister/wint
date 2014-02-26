package wint.sessionx.serialize;


public interface SerializeService {
	
	Object serialize(Object input);
	
	Object unserialize(Object src); 

}
