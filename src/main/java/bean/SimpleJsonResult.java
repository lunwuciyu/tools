package bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleJsonResult {

	private String msg = "操作失败";

	private boolean success;

	private Map<Object, Object> result = new HashMap<Object, Object>();

	private List<?> rows;

	private Integer total = 0;

	public SimpleJsonResult() {

	}

	public SimpleJsonResult(Boolean success) {
		this.success = success;
	}

	public SimpleJsonResult(Boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public SimpleJsonResult(List<?> rows, Integer total) {
		this.success = true;
		this.rows = rows;
		this.total = total;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Map<Object, Object> getResult() {
		return result;
	}

	public void setResult(Map<Object, Object> result) {
		this.result = result;
	}

	public List<?> getRows() {
		return rows;
	}

	public void setRows(List<?> rows) {
		this.rows = rows;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

}
