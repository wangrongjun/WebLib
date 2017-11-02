package com.wangrj.web_lib.filter;

import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * by wangrongjun on 2017/8/25.
 * <p>
 * 设置禁止直接访问的Jsp页面，只能交由xxxAction处理，即改为访问*.do。获取到显示需要的数据后再跳转到该页面。
 * <p>
 * <filter>
 * <filter-name>ActionInitJspFilter</filter-name>
 * <filter-class>com.wangrj.web_lib.filter.ActionInitJspFilter</filter-class>
 * <init-param>
 * <param-name>/file.jsp</param-name>
 * <param-value>showFileList.do</param-value>
 * </init-param>
 * <init-param>
 * <param-name>/index.jsp</param-name>
 * <param-value>showIndex.do</param-value>
 * </init-param>
 * </filter>
 * <filter-mapping>
 * <filter-name>ActionInitJspFilter</filter-name>
 * <url-pattern>*.jsp</url-pattern>
 * <dispatcher>REQUEST</dispatcher>
 * </filter-mapping>
 */
public class ActionInitJspFilter implements Filter {

    //以下jsp文件都禁止直接访问，只能交由xxxAction处理，即改为访问*.do
    private Map<String, String> map = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> parameterNames = filterConfig.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = filterConfig.getInitParameter(name);
            LogUtil.print(name + " - " + value);
            map.put(name, value);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String servletPath = request.getServletPath();//得到访问的servlet或者jsp的路径
        String doPath = map.get(servletPath);
        LogUtil.print(servletPath + " --> " + doPath);
        if (!TextUtil.isEmpty(doPath)) {
            request.getRequestDispatcher(doPath).forward(request, response);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
