<%@ page import="com.wangrj.web_lib.util.ImageCode" %><%--
  Created by IntelliJ IDEA.
  User: wangrongjun
  Date: 2017/9/25
  Time: 14:43
  To change this template use File | Settings | File Templates.
--%>
<%
    new ImageCode().out(request, response);
    // 在Action中验证时要这样：ImageCode.validateCode(request,userCode);
%>