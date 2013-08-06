package wint.help.tools.ibatis.gen;

public class DefaultResultRender implements ResultRender {

	public String render(IbatisResult result) {
		StringBuilder sb = new StringBuilder();
		sb.append("<result");
		sb.append(" property=");
		sb.append("\"");
		sb.append(result.getProperty());
		sb.append("\"");
		sb.append(" column=");
		sb.append("\"");
		sb.append(result.getColumn());
		sb.append("\"");
		sb.append("/>");
		return sb.toString();
	}

}
