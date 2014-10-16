package wint.mvc.form.validator;

import java.util.HashMap;
import java.util.Map;

import wint.lang.exceptions.TypeNotMatchException;
import wint.lang.exceptions.ValidatorNotFound;
import wint.lang.magic.MagicClass;
import wint.lang.magic.MagicMap;
import wint.lang.magic.MagicObject;
import wint.lang.magic.Property;
import wint.lang.utils.StringUtil;

public class ValidatorUtil {
	
	private static final Map<String, MagicClass> innerValidators = new HashMap<String, MagicClass>();
	
	static {
		registerInner("required", RequiredValidator.class);
		registerInner("int", IntegerValidator.class);
		registerInner("integer", IntegerValidator.class);
		registerInner("long", LongValidator.class);
		registerInner("number", NumberValidator.class);
		registerInner("string", StringLengthValidator.class);
		registerInner("email", EmailValidator.class);
		registerInner("regex", RegexValidator.class);
		registerInner("date", DateValidator.class);
		registerInner("enums", EnumValuesValidator.class);
		registerInner("phone", PhoneValidator.class);
		registerInner("excludeChars", ExcludeCharValidator.class);
		registerInner("boolean", BooleanValidator.class);
		registerInner("count", CountValidator.class);
	}
	
	private static void registerInner(String name, Class<? extends Validator> clazz) {
		MagicClass validatorClass = MagicClass.wrap(clazz);
		innerValidators.put(name, validatorClass);
	}
	
	private static MagicObject find(String typeName) {
		MagicClass validatorClass = innerValidators.get(typeName);
		if (validatorClass != null) {
			return validatorClass.newInstance();
		}
		MagicClass clazz = MagicClass.forName(typeName);
		if (!clazz.isAssignableTo(Validator.class)) {
			throw new TypeNotMatchException(clazz.getTargetClass() + " is not a Validator type.");
		}
		return clazz.newInstance();
	}
	
	public static Validator lookFor(String typeName, MagicMap initParameters) {
		MagicObject validator = find(typeName);
		if (validator == null) {
			throw new ValidatorNotFound("validator not found: " + typeName);
		}
		Map<String, Property> properties =  validator.getMagicClass().getProperties();
		for (Map.Entry<String, Property> entry : properties.entrySet()) {
			if (!entry.getValue().isWritable()) {
				continue;
			}
			String name = entry.getKey();
			String value = initParameters.getString(name);
			if (StringUtil.isEmpty(value)) {
				continue;
			}
			validator.setPropertyValueExt(name, value);
		}
		Validator validatorObject = (Validator)validator.getObject();
		
		
		validatorObject.init();
		return validatorObject;
	}

}
