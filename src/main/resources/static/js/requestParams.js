/**
 * Created by Administrator on 2017/11/10.
 */
var requestParams = new urlSearch();

function urlSearch() {
    var name, value;
    var str = location.href;
    var index = str.indexOf("?");
    str = str.substr(index + 1);

    var arr = str.split("&");
    for (var i = 0; i < arr.length; i++) {
        index = arr[i].indexOf("=");
        if (index > 0) {
            name = arr[i].substring(0, index);
            value = arr[i].substring(index + 1);
            this[name] = value;
        }
    }
}