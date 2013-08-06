package wint.core.serivce.supports.demo;

import java.util.List;
import java.util.Map;

import wint.core.service.AbstractService;

public class FirstServiceImpl extends AbstractService implements FirstService {
	
	private String strValue;
	
	private int intValue;
	
	private List<Person> persons;
	
	private Map<String, String> prop;

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}


	@Override
	public String toString() {
		return "FirstServiceImpl [intValue=" + intValue + ", persons=" + persons + ", prop=" + prop + ", strValue=" + strValue + "]";
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	public Map<String, String> getProp() {
		return prop;
	}

	public void setProp(Map<String, String> prop) {
		this.prop = prop;
	}

}
