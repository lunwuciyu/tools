package util;

public class SortInfo {

	public static int SORT_NONE = 0;
	public static int SORT_ASC = 1;
	public static int SORT_DESC = 2;

	// 排序区域
	private String sortField;
	// 排序方向
	private int sortDir;

	public SortInfo() {
		this.sortField = "";
		this.sortDir = SORT_NONE;
	}

	public SortInfo(String sortField, int sortDir) {
		this.sortField = sortField;
		this.sortDir = sortDir;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public int getSortDir() {
		return sortDir;
	}

	public void setSortDir(int sortDir) {
		this.sortDir = sortDir;
	}

}
