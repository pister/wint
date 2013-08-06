package wint.lang.magic;

import wint.lang.exceptions.CanNotFindClassException;
import wint.lang.utils.ClassUtil;
import wint.lang.utils.StringUtil;

public class MagicPackage {
	
	private String packagePath;

	private Package pack;
	
	public MagicPackage(String name) {
		super();
		this.packagePath = name;
		pack = Package.getPackage(name);
	}
	
	public MagicPackage(MagicPackage basePackage, String name) {
		super();
		if (StringUtil.isEmpty(name)) {
			this.packagePath = basePackage.getName();
			pack = Package.getPackage(packagePath);
		} else {
			while (name.startsWith(".")) {
				name = name.substring(1);
			}
			this.packagePath = basePackage.getName() + "." + name;
			pack = Package.getPackage(packagePath);
		}
		
	}
	
	public Package getTargetPackage() {
		return pack;
	}
	
	public String getName() {
		return packagePath;
	}
	
	public boolean exist() {
		return (pack != null);
	}
	
	public String getFileName(){
		int pos = packagePath.lastIndexOf('.');
		if (pos < 0) {
			return StringUtil.EMPTY;
		}
		return packagePath.substring(pos + 1);
	}
	
	public String toClassName() {
		int pos = packagePath.lastIndexOf('.');
		if (pos < 0) {
			return StringUtil.uppercaseFirstLetter(packagePath);
		}
		String packagePart = packagePath.substring(0, pos);
		String classPart = packagePath.substring(pos + 1);
		return packagePart + "." + StringUtil.uppercaseFirstLetter(classPart);
	}
	
	public Class<?> getClass(String name){
		if (!exist()) {
			return null;
		}
		try {
			return ClassUtil.forName(packagePath + "." + name);
		} catch (CanNotFindClassException e) {
			return null;
		}
	}
	
	public Class<?> getClass(String name, ClassLoader classLoader){
		if (!exist()) {
			return null;
		}
		try {
			return ClassUtil.forName(packagePath + "." + name, classLoader);
		} catch (CanNotFindClassException e) {
			return null;
		}
	}
	
	public String toString() {
		return getName();
	}
	
	public MagicPackage getParent() {
		if (StringUtil.isEmpty(packagePath)) {
			return null;
		}
		int pos = packagePath.lastIndexOf('.');
		if (pos < 0) {
			return null;
		}
		
		String parentName = packagePath.substring(0, pos);
		return new MagicPackage(parentName);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((packagePath == null) ? 0 : packagePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MagicPackage other = (MagicPackage) obj;
		if (packagePath == null) {
			if (other.packagePath != null)
				return false;
		} else if (!packagePath.equals(other.packagePath))
			return false;
		return true;
	}
	
	public static void main(String[] args) {
		MagicPackage p = new MagicPackage("java.util");
		System.out.println(p.exist());
		 p = new MagicPackage("java.util.xxxx");
		 System.out.println(p.exist());
	}
	
}
