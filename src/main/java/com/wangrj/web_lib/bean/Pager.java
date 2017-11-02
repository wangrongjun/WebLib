package com.wangrj.web_lib.bean;

import com.wangrj.java_lib.java_util.TextUtil;
import com.wangrj.web_lib.util.RequestUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/10/10.
 * <p>
 * 使用：
 * 当前页：${pager.pageIndex+1}
 * 总页数：${pager.pageCount}
 * 首页：xxx.jsp?pageIndex=0
 * 上一页：xxx.jsp?pageIndex=${pager.pageIndex-1}
 * 下一页：xxx.jsp?pageIndex=${pager.pageIndex-1}
 * 尾页：xxx.jsp?pageIndex=${pager.pageCount-1}
 * 跳转：
 * var value = $("input[name=textfield3]").val();
 * var pageIndex = parseInt(value) - 1;
 * if (isNaN(pageIndex))
 * alert("请输入整数");
 * else
 * location.href = "jobselec.jsp?pageIndex=" + pageIndex;
 * 删除后刷新：xxx.jsp?pageIndex=${pager.pageIndex}&refreshCount=true
 */
public class Pager<T> {

    private String requestUrl;// 用于标识不同的页面，避免后面的页面使用了前面页面的pager造成混乱。
    private Map<String, String> parameterMap;// 参数（除了分页相关外的其他参数）
    private List<T> list;
    private int pageSize;// 每页记录数
    private int pageIndex;// 当前页码，从0开始
    private int totalCount;// 总记录数

    public int getPageCount() { // 获取总页数
        return totalCount / pageSize + (totalCount % pageSize == 0 ? 0 : 1);// 剩余的放在尾页
    }

    public interface OnQueryListener<T> {
        List<T> queryList(Map<String, String> parameterMap, int offset, int rowCount);

        int queryTotalCount(Map<String, String> parameterMap);

        default Integer getIntParameter(Map<String, String> parameterMap, String name) {
            String parameter = parameterMap.get(name);
            if (TextUtil.isEmpty(parameter)) {
                return null;
            }
            try {
                return Integer.parseInt(parameter);
            } catch (NumberFormatException e) {
                e.printStackTrace(System.out);
                return null;
            }
        }
    }

    /**
     * 分页解决方案
     * <p>
     * 调用前url传入的参数：
     * 1.pageIndex 如果不为空，则跳转到pageIndex，否则跳转到第0页
     * 2.refreshCount 如果为true，则调用queryTotalCount来更新totalCount，否则不更新。该参数常用于删除记录后。
     * <p>
     * 调用后设置到session的参数：pager
     *
     * @param pageSize 每页记录数
     */
    public static <T> void setPager(int pageSize,
                                    HttpServletRequest request,
                                    List<String> queryParameterNameList,
                                    OnQueryListener<T> listener) {
        // 初始化参数
        int pageIndex = RequestUtil.getIntParameter(request, "pageIndex", 0);
        String refreshCount = request.getParameter("refreshCount");

        // 获取原有的pager
        HttpSession session = request.getSession();
        Pager<T> pager = (Pager<T>) session.getAttribute("pager");

        if (pager == null) {
            // pager为空，就新建一个pager
            Map<String, String> parameterMap = RequestUtil.getParameterMap(request, queryParameterNameList);
            int count = listener.queryTotalCount(parameterMap);
            pager = new Pager<>(request.getServletPath(), parameterMap, null, pageSize, pageIndex, count);
        } else {
            // pager不为空，遍历queryParameterNameList看看当前request是否包含查询参数，
            //     如果包含，就比较当前request的每一个查询参数是否都与pager中parameterMap相同。
            //         如果不同，就新建一个pager。
            //     如果不包含，就判断requestUrl是否变化并且pageIndex是否存在。
            //         如果requestUrl有变化，就代表访问了新的页面，就新建一个pager。
            //         如果requestUrl没有变化，但pageIndex不存在，说明由条件查询换为无条件查询，就新建一个pager。
            boolean contain = false;
            boolean equal = true;
            if (queryParameterNameList != null) {
                for (String name : queryParameterNameList) {
                    String newParameter = request.getParameter(name);
                    if (newParameter != null) {
                        contain = true;
                        if (!newParameter.equals(pager.getParameterMap().get(name))) {
                            equal = false;
                        }
                    }
                }
            }
            if (contain) {
                if (!equal) {
                    Map<String, String> parameterMap = RequestUtil.getParameterMap(request, queryParameterNameList);
                    int count = listener.queryTotalCount(parameterMap);
                    pager = new Pager<>(request.getServletPath(), parameterMap, null, pageSize, pageIndex, count);
                }
            } else if (!request.getServletPath().equals(pager.getRequestUrl()) || request.getParameter("pageIndex") == null) {
                Map<String, String> parameterMap = RequestUtil.getParameterMap(request, queryParameterNameList);
                int count = listener.queryTotalCount(parameterMap);
                pager = new Pager<>(request.getServletPath(), parameterMap, null, pageSize, pageIndex, count);
            } else if ("true".equals(refreshCount)) {// 如果页面和查询条件都没变化，且需要更新记录总数（如删除后）
                Map<String, String> parameterMap = RequestUtil.getParameterMap(request, queryParameterNameList);
                pager.setTotalCount(listener.queryTotalCount(parameterMap));
            }
        }

        // 纠正需要跳转的页编号，使其在合理范围
        if (pageIndex < 0) {
            pageIndex = 0;
        } else if (pageIndex >= pager.getPageCount()) {
            pageIndex = pager.getPageCount() - 1;
        }
        pager.setPageIndex(pageIndex);

        // 根据pager获取数据
        List<T> list = listener.queryList(pager.getParameterMap(), pageIndex * pager.getPageSize(), pager.getPageSize());
        pager.setList(list);

        // 把pager设置进session
        session.setAttribute("pager", pager);
    }

    public Pager() {
    }

    public Pager(String requestUrl, Map<String, String> parameterMap,
                 List<T> list, int pageSize, int pageIndex, int totalCount) {
        this.requestUrl = requestUrl;
        this.parameterMap = parameterMap;
        this.list = list;
        this.pageSize = pageSize;
        this.pageIndex = pageIndex;
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

}
