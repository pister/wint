package wint.tools.similar.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import wint.tools.util.IoUtil;

public class FileContent extends Content {
	
	private File file;
	
	private String charset = "gbk";

	public FileContent(File file) {
		super();
		this.id = file.getName();
		this.file = file;
	}

	@Override
	public String getValue() {
		try {
			Reader reader = new InputStreamReader(new FileInputStream(file), charset);
			return IoUtil.readToString(reader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
