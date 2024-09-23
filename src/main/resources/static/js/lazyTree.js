var requestParams = new RequestParams();

function RequestParams() {
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

function onDrawNode(e) {
    var node = e.node;
    if (orgnTree.isExpandedNode(node)) {
        e.iconCls = "icon-folderopen";
    } else {
        e.iconCls = "icon-folder";
    }
}

function onBeforeTreeLoad(e) {
    var node = e.node;      //当前节点
    var params = e.params;  //参数对象

    if (node.orgnId) {
        params.orgId = node.orgnId;
    }
}

function onbeforeexpand(e) {
    var nodes = orgnTree.getChildNodes(e.node);
    if (!nodes) {
        orgnTree.loadNode(e.node);
    }
}