package com.wangrj.web_lib.framework;

import com.wangrj.java_lib.java_util.LogUtil;
import com.wangrj.java_lib.java_util.ReflectUtil;
import com.wangrj.java_lib.java_util.TextUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * by wangrongjun on 2017/8/24.
 */
public abstract class ActionSupport implements Action {

    protected HttpServletRequest request;
    protected HttpServletResponse response;

    protected abstract String execute() throws ServletException, IOException,
            ParamErrorException, UnLoginException;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.request = request;
        this.response = response;
        try {
            return execute();
        } catch (ParamErrorException e) {
            LogUtil.printError(e.toString());
            showAlertMsg(e.toString());
        } catch (UnLoginException e) {
            showAlertMsg("请登录");
        }
        return null;
    }

    protected String getRealPath() {
        return request.getSession().getServletContext().getRealPath("\\");
    }

    protected int getIntegerParameter(String parameterName, int defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(parameterName));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    protected int checkIntegerParameter(String parameterName) throws ParamErrorException {
        String parameter = request.getParameter(parameterName);
        if (parameter == null) {
            throw new ParamErrorException("parameter not exists: " + parameterName);
        }
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            throw new ParamErrorException("parseInt error: parameter='" + parameterName +
                    "'  value='" + parameter + "'");
        }
    }

    protected String getStringParameter(String parameterName, String defaultValue) {
        String parameter = request.getParameter(parameterName);
        if (TextUtil.isEmpty(parameter)) {
            parameter = defaultValue;
        }
        return parameter;
    }

    protected String checkStringParameter(String parameterName) throws ParamErrorException {
        String parameter = request.getParameter(parameterName);
        if (TextUtil.isEmpty(parameter)) {
            throw new ParamErrorException("parameter not exists: " + parameterName);
        }
        return parameter;
    }

    protected <T> T getEntityParameter(Class<T> entityClass) {
        return getEntityParameter(entityClass, "");
    }

    /**
     * 把参数包装成entity对象
     *
     * @param prefix      参数的前缀，用于区分不同entity下同名的变量。request.getParameter(prefix + field.getName())
     * @param entityClass 如果POJO里又有POJO对象，例如job下有一个dept，会从request中读取dept.id参数并创建dept并赋值
     */
    protected <T> T getEntityParameter(Class<T> entityClass, String prefix) {
        return ReflectUtil.setObjectValue(entityClass, true, new ReflectUtil.GetValue() {
            @Override
            public Object get(Field field, boolean isBasicType) {
                // 如果是基本数据类型（数值，字符串，日期），直接获取并返回
                if (isBasicType) {
                    return request.getParameter(prefix + field.getName());
                }

                // 如果是自定义类型（一般是POJO类型），使用相同的步骤进行赋值
                Object innerObject;
                try {
                    innerObject = field.getType().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

                ReflectUtil.setObjectValue(innerObject, true, new ReflectUtil.GetValue() {
                    @Override
                    public Object get(Field field, boolean isBasicType) {
                        return request.getParameter(prefix + field.getName() + "." + field.getName());
                    }
                });

                return innerObject;
            }
        });
    }

    protected void showAlertMsg(String msg) throws IOException {
        response.getWriter().write("<script>alert('" + msg + "')</script>");
    }

    protected class ParamErrorException extends Exception {
        public ParamErrorException(String message) {
            super(message);
        }
    }

    protected class UnLoginException extends Exception {
    }

}
