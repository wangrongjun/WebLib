package com.wangrj.web_lib.framework;

import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * by wangrongjun on 2017/8/24.
 * <p>
 * Action控制器。使用时需要在web.xml中配置actionPackageName。
 * <p>
 * <servlet>
 * <servlet-name>Controller</servlet-name>
 * <servlet-class>com.wangrj.web_lib.framework.Controller</servlet-class>
 * <init-param>
 * <param-name>actionPackageName</param-name>
 * <param-value>com.learn_spring.yunpan.action</param-value>
 * </init-param>
 * </servlet>
 * <servlet-mapping>
 * <servlet-name>Controller</servlet-name>
 * <url-pattern>*.do</url-pattern>
 * </servlet-mapping>
 */
public class Controller extends HttpServlet {

    private String actionPackageName;

    @Override
    public void init() throws ServletException {
        actionPackageName = getInitParameter("actionPackageName");
        LogUtil.print("actionPackageName: " + actionPackageName);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // "/wrj/deleteJob.action" -> "com.oa.wrj.action.DeleteJobAction"
        String path = request.getServletPath();// "/wrj/deleteJob.action"
        int begin = path.lastIndexOf("/") + 1;
        int end = path.lastIndexOf(".");
        path = path.substring(begin, begin + 1).toUpperCase() + path.substring(begin + 1, end);
        String packageName = actionPackageName + "." + path + "Action";
        LogUtil.print(packageName);
        try {
            Class actionClass = Class.forName(packageName);
            Action action = (Action) actionClass.newInstance();
            String newPath = action.execute(request, response);
            if (!TextUtil.isEmpty(newPath)) {
                if (newPath.startsWith("-")) {
                    response.sendRedirect(newPath.substring(1));
                } else {
                    request.getRequestDispatcher(newPath).forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.print(e.toString());
            response.sendRedirect("error_404.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
