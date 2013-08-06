package wint.lang.magic.config;

import java.util.HashMap;
import java.util.Map;


public class MagicConfig {
	
	private static final MagicConfig magicConfig = new MagicConfig();
	
	private MagicType magicType;
	
	private Map<MagicType, MagicFactory> magicFactories = new HashMap<MagicType, MagicFactory>();
	
	private MagicConfig() {
		magicType = MagicType.AUTO;
		
		magicFactories.put(MagicType.JAVA, new ReflectMagicFactory());
		magicFactories.put(MagicType.AUTO, new AutoMagicFactory());
		magicFactories.put(MagicType.CGLIB, new CglibMagicFactory());
		
	}

	public static MagicConfig getMagicConfig() {
		return magicConfig;
	}

	public MagicType getMagicType() {
		return magicType;
	}
	
	public synchronized void register(MagicType magicType , MagicFactory magicFactory) {
		magicFactories.put(magicType, magicFactory);
	}

	public void setMagicType(MagicType magicType) {
		this.magicType = magicType;
	}
	
	public MagicFactory getMagicFactory() {
		return magicFactories.get(magicType);
	}

}
