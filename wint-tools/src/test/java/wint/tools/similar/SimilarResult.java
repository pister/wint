package wint.tools.similar;

import java.io.File;
import java.util.List;

public class SimilarResult {
	
	private List<File> file1;
	
	private List<File> file2;
	
	private double similar;

	public List<File> getFile1() {
		return file1;
	}

	public void setFile1(List<File> file1) {
		this.file1 = file1;
	}

	public List<File> getFile2() {
		return file2;
	}

	public void setFile2(List<File> file2) {
		this.file2 = file2;
	}

	public double getSimilar() {
		return similar;
	}

	public void setSimilar(double similar) {
		this.similar = similar;
	}

}
