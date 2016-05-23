package wint.mvc.servlet.domains;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * User: huangsongli
 * Date: 16/5/23
 * Time: 下午3:54
 */
public class DomainSuffixes {

    public static final Collection<String> SUFFIXES = Collections.unmodifiableCollection(Arrays.asList(
            ".com.cn", ".net.cn", "org.cn", "edu.cn",
            ".com", ".net", ".cn", ".org", ".info", ".me", ".biz", ".cc", ".asia"));

}
