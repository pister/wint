package wint.demo.app.web.action;

import java.io.Serializable;

public class TheObject implements Serializable {

	private static final long serialVersionUID = 3017786252591581703L;
	
	private int age;
	
	private long id;
	
	private String name;
	
	private TheOtherObject theOtherObject;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TheOtherObject getTheOtherObject() {
		return theOtherObject;
	}

	public void setTheOtherObject(TheOtherObject theOtherObject) {
		this.theOtherObject = theOtherObject;
	}

	@Override
	public String toString() {
		return "TheObject [age=" + age + ", id=" + id + ", name=" + name + "]";
	}
	

}
