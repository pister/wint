package wint.demo.app.biz.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class UserDO implements Serializable {
	
	private static final long serialVersionUID = 5689933234423804705L;

	private long userId;
	
	private String name;
	
	private String password;
	
	private List<String> favs;

    private int age;

	private Date gmtModified;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	@Override
	public String toString() {
		return "UserDO [name=" + name + ", password=" + password + "]";
	}

	public List<String> getFavs() {
		return favs;
	}

	public void setFavs(List<String> favs) {
		this.favs = favs;
	}

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
