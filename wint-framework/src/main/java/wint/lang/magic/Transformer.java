package wint.lang.magic;

public interface Transformer<S, T> {
	
	T transform(S object);

}
