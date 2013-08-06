package wint.mvc.url.config;

public abstract class AbstractUrlConfig {
	
	private String name;
	
	public AbstractUrlConfig(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public abstract String getPath();
	
	public abstract void buildPath();
	
	@Override
	public String toString() {
		return "UrlConfig: " + getName() + "==>" + getPath();
	}

}
