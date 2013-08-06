package wint.lang.utils;

import java.util.regex.Pattern;


/**
 * @author pister 2011-12-29 10:38:32
 */
public class TargetUtil {
	
	static final String SLASH_STRING = "/";
	
	static final char SLASH_CHAR = '/';
	
	static final String FIXED_CHARS = "_-";
	
	static final Pattern SLASHES_PATTERN = Pattern.compile("/+");
	
	/**
	 * 	 null		=> /
	 *   ""			=> /
	 *   //			=> /
	 *   \			=> /
	 *   /.ext		=> /
	 *   xxx		=> /xxx
	 *   xxx/		=> /xxx   
	 *   /xxx/		=> /xxx
	 *   \xxx		=> /xxx
	 *   /xxx/yyy 		=> /xxx/yyy
	 *   /xxx//yyy 		=> /xxx/yyy
	 *   /xxx/yyy_zzz	=> /xxx/yyyZzz
	 *   /xxx/yyy-zzz	=> /xxx/yyyZzz
	 *   /xxx_aaa/yyy_zzz => /xxxAaa/yyyZzz
	 *   /xxx/yyy.ext	=>	/xxx/yyy
	 *   /xxx-aaa/yyy.ext => /xxxAxx/yyy
	 * 
	 * @param target
	 * @return
	 */
	public static String normalizeTarget(String target) {
		target = normalizePath(target);
		
		return StringUtil.fixedCharToCamel(target, FIXED_CHARS);
	}
	
	/**
	 * 	 null		=> /
	 *   ""			=> /
	 *   //			=> /
	 *   \			=> /
	 *   /.ext		=> /
	 *   xxx		=> /xxx
	 *   xxx/		=> /xxx   
	 *   /xxx/		=> /xxx
	 *   \xxx		=> /xxx
	 *   /xxx/yyy 		=> /xxx/yyy
	 *   /xxx//yyy 		=> /xxx/yyy
	 *   /xxx/yyy_zzz	=> /xxx/yyy_zzz
	 *   /xxx/yyy-zzz	=> /xxx/yyy-zzz
	 *   /xxx_aaa/yyy_zzz => /xxxAaa/yyy_zzz
	 *   /xxx/yyy.ext	=>	/xxx/yyy
	 *   /xxx-aaa/yyy.ext => /xxxAxx/yyy
	 * 
	 * @param target
	 * @return
	 */
	public static String normalizePath(String target) {
		if (StringUtil.isEmpty(target)) {
			return SLASH_STRING;
		}
		
		target = target.replace('\\', SLASH_CHAR);
		
		int dotPos = target.indexOf('.');
		if (dotPos >= 0) {
			target = target.substring(0, dotPos);
		}
		
		while (target.endsWith(SLASH_STRING)) {
			target = target.substring(0, target.length() - 1);
		}
		
		if (StringUtil.isEmpty(target)) {
			return SLASH_STRING;
		}
		
		if (!target.startsWith(SLASH_STRING)) {
			target = SLASH_STRING + target;
		}
		
		return SLASHES_PATTERN.matcher(target).replaceAll(SLASH_STRING);
	}

}
