package page;

import support.Page;
import support.PageBean;

import java.util.List;



public class PageUtil {
	/**
	 * 将Page对象转换为PageBean对象
	 * @param page
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static PageBean pageToPageBean(Page page){
		PageBean pb = new PageBean();
		
		pb.setAllRow((int)(page.getTotalCount()));
		pb.setTotalPage((int)(page.getTotalPageCount()));
		pb.setCurrentPage((int)(page.getCurrentPageNo()));
		pb.setList((List) page.getResult());
		
		return pb;
	}
}
