package wint.lang.utils;


public class LibUtil {
	
	public static boolean isSpringExist() {
		try {
			Class.forName("org.springframework.context.ApplicationContext");
			return true;
		} catch(ClassNotFoundException e) {
			return false;
		}
	}
	
	public static boolean isCglibExist() {
		try {
			Class.forName("net.sf.cglib.reflect.FastClass");
			Class.forName("org.objectweb.asm.ClassVisitor");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isFreeMarkerExist() {
		try {
			Class.forName("freemarker.template.Configuration");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
	
	public static boolean isVelocityExist() {
		try {
			Class.forName("org.apache.velocity.app.VelocityEngine");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public static boolean isCommonsUploadFileExist() {
		try {
			// commons-uploadfile
			Class.forName("org.apache.commons.fileupload.FileItemFactory");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
