<%--@elvariable id="token" type="java.lang.String"--%>
<%--
  Created by IntelliJ IDEA.
  User: 孙建荣
  Date: 2017/3/5/005
  Time: 11:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    pageContext.setAttribute("basePath", basePath);
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>协会管理系统</title>
    <link rel="stylesheet" type="text/css" href="${basePath}/css/base.css">
    <link rel="stylesheet" type="text/css" href="${basePath}/css/login.css">
</head>

<body>
<!-- logo -->
<img id="logo" src="${basePath}/picture/logo.png" alt="">
<!-- 登录框 -->
<div id="loginBox">
    <div class="topPart">
        <h2>登陆入口</h2>
        <div id="toolbar">
            <a class="waves-effect waves-button" href="javascript:" onclick="logoutAction()"><i
                    class="zmdi zmdi-run"></i> 强制退出</a>
        </div>
        <%--<a id="goBack" href="javascript:;" onclick="logoutAction()"><i class="zmdi zmdi-run"></i> 强制退出</a></a>--%>
    </div>

    <input type="hidden" name="token" id="token" VALUE="${sessionScope.token}">

    <input type="text" id="username" name="LoginForm[username]" class="txt_input txt_input2" placeholder="请输入用户名"
           autocomplete="off">


    <input type="password" id="password" name="LoginForm[password]" class="txt_input" placeholder="请输入密码"
           autocomplete="new-password">


    <div class="codebox">
        <input type="text" id="code" placeholder="请输入验证码">

        <div style="width: 400px;">
            <div style="float:left;width:100px; margin-left: 13px; height:42px;">
                <img style="width:100px; height:42px; cursor: pointer;" title="点击刷新" id="codePicture"
                     onclick="refreshValidateCode()"
                     src="${basePath}/code/imageCode"/>
            </div>
        </div>

    </div>
    <!-- 用户名错误提示 -->
    <span id="usernameErrorElement"></span>
    <!-- 密码错误提示 -->
    <span id="passwordErrorElement"></span>
    <!-- 验证码提示 -->
    <span id="validateCodeErrorElement"></span>

    <button type="button" href="javascript:" id="loginBtn" onclick="checkForm()">登录</button>
    <a id="forget" href="#">忘记密码 ?</a>
    <a id="set_username" href="#">学号登录入口 ?</a>
</div>
<!-- 底部版权 -->
<p id="copyright">个人——赣ICP备13015608号-5
    <a target="_blank" href="#"
       style="color: #2f2f2f; padding-left:15px">
        <img src="${basePath}/picture/authicon.png"/>
        <span style="margin-left:10px;">赣 公网安备 31010702001486号</span>
    </a>
</p>


<script type="text/javascript" src="${basePath}/js/jquery.js"></script>
<script src="${basePath}/plugins/layer/layer.js"></script>
<script src="${basePath}/js/login.js"></script>

</body>
</html>