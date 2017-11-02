package com.wangrj.web_lib.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * by wangrongjun on 2017/8/24.
 * <p>
 * 在所有的请求与响应中设置编码为UTF-8
 * <p>
 * <filter>
 * <filter-name>CharsetFilter</filter-name>
 * <filter-class>com.wangrj.web_lib.filter.CharsetFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>CharsetFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * <dispatcher>REQUEST</dispatcher>
 * <dispatcher>FORWARD</dispatcher>
 * <dispatcher>INCLUDE</dispatcher>
 * <dispatcher>ERROR</dispatcher>
 * </filter-mapping>
 */
public class CharsetFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    //    @Override
    public void init1(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
