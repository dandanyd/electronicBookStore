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
                errorMessage = "You have been forced to exit,please log in again";
                break;
            case "2":
                errorMessage = "Session timeout,Please login again";
                break;
            case "3":
                errorMessage = "Please enter a user name.";
                break;
            case "4":
                errorMessage = "Please enter password.";
                break;
            case "5":
                errorMessage = "user name or password error.";
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