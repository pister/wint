package wint.tools.redis;

import java.io.Serializable;

public class RedisHost implements Serializable {

	private static final long serialVersionUID = -5517721164555369909L;

	private String hostname;
	
	private int port;

	public RedisHost() {
		super();
	}

	public RedisHost(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "RedisHost [hostname=" + hostname + ", port=" + port + "]";
	}
	
}
