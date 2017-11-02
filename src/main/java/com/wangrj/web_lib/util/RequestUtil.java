package com.wangrj.web_lib.util;

import com.wangrj.java_lib.java_util.TextUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * by wangrongjun on 2017/10/16.
 */
public class RequestUtil {

    public static int getIntParameter(HttpServletRequest request, String name, int defaultValue) {
        Integer value = getIntParameter(request, name);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public static Integer getIntParameter(HttpServletRequest request, String name) {
        String parameter = request.getParameter(name);
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

    public static int checkIntParameter(HttpServletRequest request, String name) {
        String parameter = request.getParameter(name);
        if (TextUtil.isEmpty(parameter)) {
            throw new RuntimeException("parameter '" + parameter + "' is null");
        }
        try {
            return Integer.parseInt(parameter);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request, List<String> parameterNameList) {
        Map<String, String> map = new HashMap<>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (parameterNameList != null && parameterNameList.size() > 0) {
                boolean shouldPut = false;
                for (String s : parameterNameList) {
                    if (name.equals(s)) {
                        shouldPut = true;
                        break;
                    }
                }
                if (!shouldPut) {
                    continue;
                }
            }
            String parameter = request.getParameter(name);
            if (parameter != null) {
                map.put(name, parameter);
            }
        }
        return map;
    }

    public static String getRequestUrlWithParameter(HttpServletRequest request, List<String> ignore) {
        String url = request.getServletPath();// "/wrj/index.jsp"
        String parameterList = "";
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            if (ignore != null && ignore.size() > 0) {
                boolean shouldIgnore = false;
                for (String s : ignore) {
                    if (name.equals(s)) {
                        shouldIgnore = true;
                        break;
                    }
                }
                if (shouldIgnore) {
                    continue;
                }
            }
            String value = request.getParameter(name);
            parameterList += name + "=" + value + "&";
        }
        if (parameterList.length() > 0) {
            parameterList = parameterList.substring(0, parameterList.length() - 1);
            return url + "?" + parameterList;
        } else {
            return url;
        }
    }

}
