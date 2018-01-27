var usernameErrorElement = $('#usernameErrorElement');
var passwordErrorElement = $('#passwordErrorElement');
var validateCodeErrorElement = $('#validateCodeErrorElement');

var usernameElement = $("#username");
var passwordElement = $("#password");
var validateCodeElement = $("#code");
var validateCodePicture = $("#codePicture");
var tokenElement = $("#token");
var loginBtn = $("#loginBtn");

// 页面初始化时执行的一些操作
$(function () {
    //输入用户名
    usernameElement.on('focus', function () {
        hiddenBorderWarn(usernameElement);
        hiddenBorderWarn(passwordElement);
        hiddenBorderWarn(validateCodeElement);
        cleanInputValue(usernameErrorElement);
        cleanInputValue(passwordErrorElement);
        cleanInputValue(validateCodeErrorElement);
    });
    //输入密码
    passwordElement.on('focus', function () {
        console.log("passwordElement.onfocus");
        hiddenBorderWarn(usernameElement);
        hiddenBorderWarn(passwordElement);
        hiddenBorderWarn(validateCodeElement);
        cleanInputValue(usernameErrorElement);
        cleanInputValue(passwordErrorElement);
        cleanInputValue(validateCodeErrorElement);
    });
    //输入验证码
    validateCodeElement.on('focus', function () {
        console.log("validateCodeElement.onfocus");
        hiddenBorderWarn(usernameElement);
        hiddenBorderWarn(passwordElement);
        hiddenBorderWarn(validateCodeElement);
        cleanInputValue(usernameErrorElement);
        cleanInputValue(passwordErrorElement);
        cleanInputValue(validateCodeErrorElement);
    });

    //回车登录
    document.onkeydown = function (e) {
        var ev = document.all ? window.event : e;
        if (ev.keyCode === 13) {
            checkForm();
        }
    };

});

function getRootPath() {
    var curWwwPath = window.document.location.href;   // 获取当前的网址 example http://localhost:8080/manager/index.html
    var pathName = window.document.location.pathname;   // 获取主机地址后的目录  example  /manager/index.html
    var position = curWwwPath.indexOf(pathName);         // 获取项目资源的url开始地址  example  21
    var localhostPath = curWwwPath.substring(0, position);   // 获取主机的地址  example    http://localhost:8080
    // 计算主机url资源地址后第一个斜杠到第二个斜杠之间的内容就是项目名字
    var projectName = pathName.substring(0, pathName.substr(1).indexOf("/") + 1);
    return (localhostPath + projectName);
}


function checkForm() {
    var usernameValue = usernameElement.val();
    var passwordValue = passwordElement.val();
    var codeValue = validateCodeElement.val();

    if (usernameValue === '') {
        layer.msg('请填写用户名', {icon: 5});
        usernameErrorElement.text('请填写用户名');
        usernameElement.focus();
        showBorderWarn(usernameElement);
        return false;
    }

    if (passwordValue === '') {
        layer.msg('请填写密码', {icon: 5});
        passwordErrorElement.text('请填写密码');
        passwordElement.focus();
        showBorderWarn(passwordElement);
        return false;
    }
    if (codeValue === '' || codeValue.length !== 4) {
        layer.msg('请填写正确验证码', {icon: 5});
        validateCodeElement.focus();
        showBorderWarn(validateCodeElement);
        return false;
    }
    sendLoginInfo();
}

// 提交表单操作
function sendLoginInfo() {
    var param = {
        username: usernameElement.val(),
        password: passwordElement.val(),
        formCode: validateCodeElement.val(),
        token: tokenElement.val()
    };
    var flag = 1;
    console.log("向服务器发送的token" + tokenElement.val());
    if (flag) {
        $.ajax({
            type: "post",
            url: getRootPath() + "/login.action",
            data: param,
            dataType: "json",
            beforeSend: function () {
                flag = 0;
                disableBtn(loginBtn);
            },
            success: function (result) {
                var statusCode = result.status;
                if (statusCode === 995) {
                    flag = 0;
                    //登录成功
                    layer.msg('登陆成功了', function () {
                        window.parent.location.href = getRootPath() + "/userCenter.html";
                    });
                } else if (statusCode === 996 || statusCode === 1) {
                    layer.msg('用户名或者密码错误。。', {icon: 5});
                    cleanInputValue(validateCodeElement);
                    showBorderWarn(usernameElement);
                    showBorderWarn(passwordElement);
                    refresh();
                } else if (statusCode === 998) {
                    layer.alert('您已经登录过了，请不要重复登录,点击强制退出清除您的会话');
                } else if (statusCode === 988) {
                    layer.msg('重复提交表单，速度太快了。。。。。请再次再尝试登陆', {icon: 5});
                    refreshValidateCode();
                } else if (statusCode === 991) {
                    layer.msg('验证码错误', {icon: 5});
                    cleanInputValue(validateCodeElement);
                    showBorderWarn(validateCodeElement);
                    refreshValidateCode();
                }
                else {
                    layer.alert('检查下你的用户名跟密码尝试重新登录');
                }
                flag = 1;
                enableBtn(loginBtn);
            },
            error: function () {
                flag = 1;
                enableBtn(loginBtn);
                layer.msg('服务器开小差了', {icon: 5});
            }
        });

    }
}

// 禁用按钮
function disableBtn(buttonElement) {
    buttonElement.attr("disabled", "disabled");
    buttonElement.text("登录验证中");
}

// 启用按钮
function enableBtn(buttonElement) {
    buttonElement.removeAttr("disabled");
    buttonElement.text("登录");
}


//切换验证码
function refreshValidateCode() {
    validateCodePicture.attr("src", getRootPath() + '/code/generateCode?rand=' + Math.random());
    validateCodeElement.val('');
}

/**
 * 消除输入框的红色边框警示效果消除
 * @param inputElement
 */
function hiddenBorderWarn(inputElement) {
    inputElement.css('border', '1px solid #d3d3d3');
}

/**
 *显示红框警告
 */
function showBorderWarn(inputElement) {
    inputElement.css('border', '2px solid red');
}

/**
 * 把输入框边的警示文字消除
 * @param inputElement
 */
function cleanInputValue(inputElement) {
    inputElement.text('');
}

// 注销登录操作
function logoutAction() {
    $.ajax({
        type: "Get",
        url: getRootPath() + "/logout.action",
        success: function (result) {
            if (result.status !== 993) {
                layer.msg('服务器出错', {icon: 5});
            }
            layer.alert('已经强行退出，请尝试重新登录', {icon: 6});
        },
        error: function () {
            layer.msg('服务器出错', {icon: 5});
        }
    })
}

function getToken() {
    $.ajax({
        type: "get",
        url: getRootPath() + "/code/getToken",
        success: function (token) {
            if (token !== '') {
                tokenElement.val(token);
            }
        }
    })
}
