package wint.mvc.module;


public interface Module {
	
	/**
	 * action or widget
	 * @return
	 */
	String getType();

	/**
	 * method start with do.., like doXyz ect.
	 * @return
	 */
	boolean isDoAction();
	
	/**
	 * the <em>@Action</em> annotaion's defaultTarget() value.
	 * @return
	 */
	String getDefaultTarget();
	
	ModuleInfo getModuleInfo();
	
}
