package wint.help.http;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author songlihuang
 * @date 2024/3/7 09:35
 */
public enum MimeTypeEnum {
    HTML("text/html", "html", "htm"),
    TXT("text/plain", "txt"),
    XML("application/xml", "xml"),
    CSS("text/css", "css"),
    JSON("application/json", "json"),
    JS("application/javascript", "js"),
    JPEG("image/jpeg", "jpeg", "jpg"),
    PNG("image/png", "png"),
    GIF("image/gif", "gif"),
    BMP("image/bmp", "bmp"),
    ICO("image/ico", "ico", "icon"),
    SWF("application/x-shockwave-flash", "swf"),
    DOC("application/msword", "doc"),
    DOCX("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "docx"),
    XLS("application/vnd.ms-excel", "xls"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "xlsx"),
    PPT("application/vnd.ms-powerpoint", "ppt"),
    PPTX("application/vnd.openxmlformats-officedocument.presentationml.presentation", "pptx"),
    WOFF("application/font-woff", "woff"),
    TTF("font/ttf", "ttf"),
    MP3("audio/mpeg", "mp3"),
    MP4("video/mp4", "mp4"),

    ;

    private String mimeType;

    private List<String> suffixList;


    private MimeTypeEnum(String mimeType, String ...suffix) {
        this.mimeType = mimeType;
        this.suffixList = Collections.unmodifiableList(Arrays.asList(suffix));
    }

    public String getMimeType() {
        return mimeType;
    }

    public List<String> getSuffixList() {
        return suffixList;
    }
}
