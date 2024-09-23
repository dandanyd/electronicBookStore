/**
 * Created by n72636 on 2018/10/25.
 */
function onCompanyChanged(e) {
    mini.MessageBox.buttonText = {
        ok: "Confirm",
        cancel: "Cancel"
    }

    mini.confirm("The switching company will open the page again,Please save the data that has been operated first,Will you switch the company now?", "Prompt",
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
        alert("The original password cannot be empty！");
        return;
    }
    if (newPwd == null || newPwd == "" || newPwd.length < 5) {
        alert("The new password cannot be empty and at least 5 bits long！");
        return;
    }
    if (newPwdConfirm == null || newPwdConfirm == "") {
        alert("Verify that the new password cannot be empty！");
        return;
    }
    if (newPwd != newPwdConfirm) {
        alert("The new password entered twice is different！");
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
                alert("Password reset successfully,Please login again.");
                window.top.location.href = "logout";
            } else {
                alert("System exception");
            }

        }
    });
}
