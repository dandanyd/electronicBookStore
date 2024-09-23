/**
 * Created by n72636 on 2018/10/25.
 */
function onCompanyChanged(e) {
    mini.confirm("切换公司会重新打开页面，请先保存已操作的数据，是否现在就切换公司？", "提示",
        function (action) {
            if (action == "ok") {
                mini.Cookie.set('company', e.value, 100);//100天过期的话，可以保持皮肤切换
                //  window.location.reload();
                var tab = tabs.getActiveTab();
                tabs.removeAll(tab);
            } else {
                e.cancel = true;
            }
        }
    );
}

function confirm() {
    var userCd = mini.get("userCd").value;
    var oldPwd = mini.get("oldPwd").value;
    var newPwd = mini.get("newPwd").value;
    var newPwdConfirm = mini.get("newPwdConfirm").value;

    if (oldPwd == null || oldPwd == "") {
        alert("原始密码不能为空！");
        return;
    }
    if (newPwd == null || newPwd == "" || newPwd.length < 5) {
        alert("新密码不能为空且长度至少5位！");
        return;
    }
    if (newPwdConfirm == null || newPwdConfirm == "") {
        alert("确认新密码不能为空！");
        return;
    }
    if (newPwd != newPwdConfirm) {
        alert("两次输入的新密码不一样！");
        return;
    }

    $.ajax({
        url: "modPwd",
        data: {userCd: userCd, oldPwd: oldPwd, newPwd: newPwd},
        type: "post",
        success: function (text) {
            var data = mini.decode(text);
            if (data.flag == -1) {
                alert(data.msg);
            } else if (data.flag == 1) {
                alert("密码重置成功，请重新登录。");
                window.top.location.href = "logout";
            } else {
                alert("系统异常");
            }

        }
    });
}

function cancel() {
    window.top.location.href = "logout";
}
