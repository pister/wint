package wint.help.seo;

import wint.lang.utils.CollectionUtil;
import wint.lang.utils.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Set;

/**
 * @author songlihuang
 * @date 2024/10/12 10:31
 */
public class SeoStringUtil {

    private static final String SYMBOL_CHARS = "~`!@#$%^&*()_+-=[]{};:'\",.<>/?\\|\t \n\r";

    private static final String SEPARATOR = "-";

    private static final int MAX_ASCII_VALUE = 127;

    private static final Set<Character> SYMBOL_SET;

    static {
        Set<Character> newSymbolSet = CollectionUtil.newHashSet();
        for (char c : SYMBOL_CHARS.toCharArray()) {
            newSymbolSet.add(c);
        }
        SYMBOL_SET = Collections.unmodifiableSet(newSymbolSet);
    }

    /**
     * 转成一个seo url友好的字符串
     * @param input 原始输入
     * @return 在url中seo友好的字符串
     */
    public static String toFriendlyInUrl(String input) {
        if (StringUtil.isEmpty(input)) {
            return input;
        }
        StringBuilder output = new StringBuilder(128);

        boolean prevCharIsSymbol = false;
        for (char c : input.toCharArray()) {
            if (SYMBOL_SET.contains(c)) {
                if (prevCharIsSymbol) {
                    continue;
                }
                prevCharIsSymbol = true;
                output.append(SEPARATOR);
            } else {
                prevCharIsSymbol = false;
                if (c > MAX_ASCII_VALUE) {
                    try {
                        String encodeChar = URLEncoder.encode(String.valueOf(c), "utf-8");
                        output.append(encodeChar);
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    output.append(Character.toLowerCase(c));
                }
            }
        }
        if (prevCharIsSymbol) {
            if (output.length() > 0) {
                // 删除最后一个字符
                output.deleteCharAt(output.length() - 1);
            }
        }
        return output.toString();
    }

}
