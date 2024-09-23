/**
 * Created by n72636 on 2018/10/25.
 */
$(document).ready(function () {
    if (requestParams.urlCallback) {
        $("input[name='urlCallback']").val(requestParams.urlCallback);
    }
    if (requestParams.errorCode) {
        var errorMessage;
        switch (requestParams.errorCode) {
            case "1":
                errorMessage = "您已经被强制退出，请重新登录";
                break;
            case "2":
                errorMessage = "会话超时，请重新登录";
                break;
            case "3":
                errorMessage = "请输入用户名。";
                break;
            case "4":
                errorMessage = "请输入密码。";
                break;
            case "5":
                errorMessage = "用户名或密码错误。";
                break;
            default:
                errorMessage = "";
        }
        if (errorMessage != "") {
            $("div.errors").html(errorMessage);
            $("div.errors").css("display", "");
        }
    }

    $.get("loginInfo",
        function (data, status) {
            if (status == "success" && data) {
                $("#title").html(data.sysName);
            }
        }
    );
});