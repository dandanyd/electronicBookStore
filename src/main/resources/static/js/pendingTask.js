/**
 * Created by WIN7 on 2015-7-28.
 */

var interval_task_count = -1;
var updater = {
    poll: function () {
        var task_url = url + "user/task/count?user=loginid=" + Base64.encode(user) + "&t=" + new Date().getTime();
        $.ajax({
            url: task_url,
            type: "get",
            dataType: "jsonp",
            success: updater.onSuccess,
            error: updater.onError
        });
        if (interval_task_count != -1) {
            window.clearInterval(interval_task_count);
        }
        interval_task_count = window.setInterval(updater.poll, 30000);
    },
    onSuccess: function (data, dataStatus) {
        $('#count').text(data);
    }
};

var user, url;
$.ajax({
    url: "indexInfo", async: false, success: function (data) {
        user = data.userCd;
    }
});
$.ajax({
    url: "bpmService/ExaminationAndApprovalNumber", async: false, success: function (data) {
        url = data;
    }
});

$(document).ready(function () {
    updater.poll();
});

var Base64 = {
    _keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",
    encode: function (input) {
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = Base64._utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);

            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;

            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);
        }
        return output;
    },

    _utf8_encode: function (string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";

        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);

            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }
        }
        return utftext;
    }
}

