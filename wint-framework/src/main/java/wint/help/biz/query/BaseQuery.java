package wint.help.biz.query;

import java.io.Serializable;

public class BaseQuery implements Serializable {

	private static final long serialVersionUID = -8274758873447809336L;
	
	private int pageSize = 20;
	
	private int startRow = 0;
	
	private int pageNo = 1;
	
	private int totalPageCount;

	private int totalResultCount;
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		if (pageSize <= 1) {
			pageSize = 1;
		}
		startRow = (pageNo - 1) * pageSize;
		this.pageSize = pageSize;
	}

	public int getStartRow() {
		return startRow;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if (pageNo <= 1) {
			pageNo = 1;
		}
		startRow = (pageNo - 1) * pageSize;
		this.pageNo = pageNo;
	}
	

	public int getMaxPage() {
		if (totalResultCount <= 1) {
			return 1;
		}
		if (pageSize <= 1) {
			return totalResultCount;
		}
		return (totalResultCount + pageSize - 1) / pageSize;
	}
	
	public int getPagesCount() {
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
	
	public boolean turnNext() {
		if (hasNextPage()) {
			pageNo = getNextPage();
			return true;
		}
		return false;
	}
	
	public boolean turnPrev() {
		if (hasPrevPage()) {
			pageNo = getPrevPage();
			return true;
		}
		return false;
	}
	
	public boolean turn(int page) {
		int maxPage = getMaxPage();
		if (page > maxPage) {
			pageNo = maxPage;
			return false;
		}
		if (page < 1) {
			pageNo = 1;
			return false;
		}
		pageNo = page;
		return true;
	}
	
	public int getCurrentPage() {
		int maxPage = getMaxPage();
		if (pageNo > maxPage) {
			return maxPage;
		}
		if (maxPage < 1) {
			return 1;
		}
		return pageNo;
	}
	
	public int getTotalPageCount() {
		return totalPageCount;
	}

	public int getTotalResultCount() {
		return totalResultCount;
	}

	public void setTotalResultCount(int totalResultCount) {
		this.totalResultCount = totalResultCount;
		totalPageCount = (totalResultCount + pageSize - 1) / pageSize + 1;
		if (pageNo > totalPageCount) {
			pageNo = totalPageCount;
		}
	}

}
