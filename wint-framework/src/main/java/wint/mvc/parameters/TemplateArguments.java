package wint.mvc.parameters;

public class TemplateArguments {

	private Arguments arguments;

	public TemplateArguments(Arguments arguments) {
		super();
		this.arguments = arguments;
	}

	public double getDouble(int index) {
		return arguments.getDouble(index);
	}

	public float getFloat(int index) {
		return arguments.getFloat(index);
	}

	public int getInt(int index) {
		return arguments.getInt(index);
	}

	public long getLong(int index) {
		return arguments.getLong(index);
	}

	public String getString(int index) {
		return arguments.getString(index);
	}

	public int length() {
		return arguments.length();
	}
	
}
