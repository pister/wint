package wint.sessionx.cookie;

import javax.servlet.http.Cookie;

public class WintCookie extends Cookie {

	 private boolean httpOnly;
	
	public WintCookie(String name, String value) {
		super(name, value);
	}

	public boolean isHttpOnly() {
		return httpOnly;
	}

	public void setHttpOnly(boolean httpOnly) {
		this.httpOnly = httpOnly;
	}
	
   public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDomain() == null) ? 0 : getDomain().hashCode());
        result = prime * result + (httpOnly ? 1231 : 1237);
        result = prime * result + getMaxAge();
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
        return result;
    }
    
    public String toString(){
    	StringBuffer tmep =new StringBuffer();
    	tmep.append("cookie[domain:").append((getDomain() == null) ? "" : getDomain()).
    	     append("[httpOnly:").append((isHttpOnly()) ? "true" : "false").
    	     append("[name:").append((getName()== null) ? "" : getName()).
    	     append("[path:").append((getPath()== null) ? "" : getPath()).
    	     append("[value:").append((getValue()== null) ? "" : getValue()).
    	     append("[MaxAge:").append(String.valueOf(getMaxAge()));
    	
    	return tmep.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WintCookie other = (WintCookie) obj;
        if (getDomain() == null) {
            if (other.getDomain() != null)
                return false;
        } else if (!getDomain().equals(other.getDomain()))
            return false;
        if (httpOnly != other.httpOnly)
            return false;
        if (getMaxAge() != other.getMaxAge())
            return false;
        if (getName() == null) {
            if (other.getName() != null)
                return false;
        } else if (!getName().equals(other.getName()))
            return false;
        if (getPath() == null) {
            if (other.getPath() != null)
                return false;
        } else if (!getPath().equals(other.getPath()))
            return false;
        if (getValue() == null) {
            if (other.getValue() != null)
                return false;
        } else if (!getValue().equals(other.getValue()))
            return false;
        return true;
    }
	
}
