package wint.help.biz.query;

import wint.lang.magic.MagicClass;
import wint.lang.magic.Property;
import wint.mvc.url.UrlBroker;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BaseQuery implements Serializable {

    private static final long serialVersionUID = -8274758873447809336L;

    private int pageSize = 20;

    private int startRow = 0;

    private long totalResultCount;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        if (pageSize <= 1) {
            pageSize = 1;
        }
        int pageNo = getPageNo();
        this.pageSize = pageSize;
        setPageNo(pageNo);
    }

    public int getStartRow() {
        return startRow;
    }

    public void setPageNo(int pageNo) {
        if (pageNo <= 1) {
            pageNo = 1;
        }
        startRow = (pageNo - 1) * pageSize;
    }

    public int getMaxPage() {
        if (totalResultCount <= 1) {
            return 1;
        }
        if (pageSize <= 1) {
            return (int)totalResultCount;
        }
        return (int)(totalResultCount + pageSize - 1) / pageSize;
    }

    public int getPageCount() {
        return getMaxPage();
    }

    public boolean hasNextPage() {
        int currentPage = getCurrentPage();
        return currentPage < getMaxPage();
    }

    public boolean hasPrevPage() {
        int currentPage = getCurrentPage();
        return currentPage > 1;
    }

    public int getNextPage() {
        int currentPage = getCurrentPage();
        if (hasNextPage()) {
            return (currentPage + 1);
        }
        return currentPage;
    }

    public int getPrevPage() {
        int currentPage = getCurrentPage();
        if (hasPrevPage()) {
            return currentPage - 1;
        }
        return currentPage;
    }

    public void turnNext() {
        this.startRow += pageSize;
    }

    public void turnPrev() {
        this.startRow -= pageSize;
        if (this.startRow < 0) {
            this.startRow = 0;
        }
    }

    public int getCurrentPage() {
       return startRow / pageSize + 1;
    }

    public int getPageNo() {
        return getCurrentPage();
    }

    public long getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(long totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public UrlBroker appendTo(UrlBroker urlBroker) {
        MagicClass baseClass = MagicClass.wrap(BaseQuery.class);
        MagicClass selfClass = MagicClass.wrap(getClass());

        Map<String, Property> baseProperties = baseClass.getReadableProperties();
        Map<String, Property> selfProperties = selfClass.getReadableProperties();
        if (baseProperties.size() == selfProperties.size()) {
            return urlBroker;
        }
        Map<String, Property> propertiesToBeAppend = new HashMap<String, Property>(selfProperties.size() - baseProperties.size());
        for (Map.Entry<String, Property> entry : selfProperties.entrySet()) {
            if (!baseProperties.containsKey(entry.getKey())) {
                propertiesToBeAppend.put(entry.getKey(), entry.getValue());
            }
        }
        if (propertiesToBeAppend.isEmpty()) {
            return urlBroker;
        }

        for (Map.Entry<String, Property> entry : propertiesToBeAppend.entrySet()) {
            String name = entry.getKey();
            Property property = entry.getValue();
            Object value = property.getValue(this);
            if (value == null) {
                continue;
            }
            urlBroker.param(name, value);
        }

        return urlBroker;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }
}
