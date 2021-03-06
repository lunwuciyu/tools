package support;

import java.io.Serializable;
import java.util.List;
/**
 * start从0开始
 * page从1开始
 * <p></p>
 * @version V1.0   
 * @modificationHistory=========================逻辑或功能性重大变更记录
 * @modify by user: {修改人} 2012-11-12
 * @modify by reason:{方法名}:{原因}
 */
public class PaginationSupport implements Serializable{
	/** 序列化ID*/
	private static final long	serialVersionUID	= 1L;
	@SuppressWarnings("rawtypes")
	private List items; // 要返回的某一页的记录列表

	private int totalCount; // 总记录数
	private int totalPage; // 总页数
	private int currentPage; // 当前页
	private int pageSize = 10; // 每页记录数

	private int startIndex;
	private int previousPage;
	private int nextPage;

	public PaginationSupport() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public PaginationSupport(List items, int totalCount, int pageSize, int startIndex) {
		if (pageSize <= 0) {
			pageSize = 10;
		}
		this.items = items;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.startIndex = (startIndex < 0 ? 0 : startIndex);
		this.totalPage = this.totalCount % pageSize == 0 ? this.totalCount / pageSize : this.totalCount / pageSize + 1;
		this.currentPage = this.startIndex / pageSize + 1;
	}

	@SuppressWarnings("rawtypes")
	public List getItems() {
		return items;
	}

	@SuppressWarnings("rawtypes")
	public void setItems(List items) {
		this.items = items;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageCount() {
		return (this.totalCount % pageSize == 0 ? this.totalCount / pageSize : this.totalCount / pageSize + 1);
	}

	public void setPageCount(int pageCount) {
		this.totalPage = pageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getPreviousPage() {
		previousPage = (this.startIndex % pageSize == 0 ? this.startIndex / pageSize : this.startIndex / pageSize + 1) - 1;
		if (previousPage < 0)
			previousPage = 0;
		return previousPage;
	}

	public void setPreviousPage(int previousPage) {
		this.previousPage = previousPage;
	}

	public int getNextPage() {
		nextPage = (this.startIndex % pageSize == 0 ? this.startIndex / pageSize : this.startIndex / pageSize + 1) + 1;
		if (nextPage > getPageCount() - 1)
			nextPage = getPageCount() - 1;
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}
	
	/**
	* @Description:pageNo和start之间的转换
	* @param pageNo
	* @param limit
	* @return  Integer
	 */
	public static Integer changePageNOToStart(Integer pageNo,Integer limit) {
		if (pageNo == 0) {
			pageNo = 1;
		}
		Integer start = (pageNo-1)*limit;
		return start;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
