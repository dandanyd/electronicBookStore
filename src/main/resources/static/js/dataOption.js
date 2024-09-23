/**
 * Created by gln on 2018/2/1.
 */
var pageEquStatus =
    [{id: 0, text: '首次FAT'},
        {id: 1, text: '外检'},
        {id: 2, text: '开箱'},
        {id: 3, text: '安装'}
    ];

//意见类型
var pageOpType = [{id: 1, text: '开箱前关闭'}, {id: 2, text: '动车前关闭'}];

//申请状态
var pageAppStatus =
    [{id: -1, text: 'ALL'},
        {id: 0, text: '待申请'},
        {id: 1, text: '待检验'},
        {id: 2, text: '退检'},
        {id: 3, text: '意见'},
        {id: 4, text: '合格'},
        {id: 5, text: '取消'}
    ];

//检验结果 0表示未登记结果
var pageFatResult =
    [
        {id: 0, text: 'ALL'},
        {id: 1, text: '合格Acc'},
        {id: 2, text: '不合格REJ'},
        {id: 3, text: '带意见接受AWC'},
        {id: 4, text: '意见延续'},
        {id: 5, text: '免检'},
        {id: 6, text: '取消'}
    ];

//登记结果界面 所用的结果下拉框（外捡）(主任级别)
var pageFatResult2 =
    [
        {id: 1, text: '合格Acc'},
        {id: 2, text: '不合格REJ'},
        {id: 3, text: '带意见接受AWC'},
        {id: 5, text: '免检'},
        {id: 6, text: '取消'}
    ];
//登记结果界面 所用的结果下拉框（外捡）
var pageFatResult3 =
    [
        {id: 1, text: '合格Acc'},
        {id: 2, text: '不合格REJ'},
        {id: 3, text: '带意见接受AWC'},
        {id: 6, text: '取消'}
    ];

var pageOpenResult =
    [
        {id: 1, text: '合格Acc'},
        {id: 2, text: '不合格REJ'},
        {id: 4, text: '意见延续'}
    ];
var pageAssmResult =
    [{id: 1, text: '合格Acc'},
        {id: 2, text: '不合格REJ'}
    ];
//意见回复状态
var pageOpStatus =
    [{id: 0, text: 'ALL'},
        {id: 1, text: '待回复'},
        {id: 2, text: '待关闭'},
        {id: 3, text: '已关闭'}
    ];

//数据来源
var pageSrcType =
    [
        {id: 1, text: '供方'},
        {id: 2, text: '配套'}
    ]
//外捡状态
var pageFatStatus =
    [
        {id: 0, text: '未开始'},
        {id: 1, text: '检验中'},
        {id: 2, text: '完成'}
    ]
//开箱状态
var pageOpenStatus =
    [
        {id: -1, text: '无需开箱'},
        {id: 0, text: '未开始'},
        {id: 1, text: '无需开箱'},
        {id: -1, text: '无需开箱'}
    ]
//意见状态
var pageOpinionStatus =
    [
        {id: 0, text: '无'},
        {id: 1, text: '待关闭'},
        {id: 2, text: '关闭'}
    ]
//总体完成状态
var pageEndStatus =
    [
        {id: 0, text: 'N'},
        {id: 1, text: 'Y'}
    ]

//添加行
function addRow(grid, editcolumn) {
    var newRow = {id: ""};
    grid.addRow(newRow, grid.data.length);
    grid.beginEditCell(newRow, editcolumn);
}

function deleteObjects(grid, url) {
    var rows = grid.getSelecteds();
    if (rows.length > 0) {
        mini.confirm("确定删除选中记录?", "提示信息", function (action) {
            if (action == "ok") {
                //要发给后台处理的删除
                var dataRows = new Array();
                var j = 0;
                if (rows == null) {
                    return;
                }
                for (var i = 0; i < rows.length; i++) {
                    //新增的记录
                    if (rows[i]['_state'] == "added") {
                        continue;
                    } else {
                        rows[i]['alive'] = 0;
                        dataRows[j] = rows[i];
                        j++;
                    }
                }
                if (dataRows.length == 0) {
                    grid.removeRows(rows, false);
                    return;
                }
                var json = mini.encode(dataRows);
                grid.loading("保存中，请稍后......");
                $.ajax({
                    url: url,
                    data: json,
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    type: "POST",
                    success: function (json) {
                        if (json.code !== null && json.code !== 0 && json.msg !== null && json.msg !== undefined && json.msg !== '') {
                            mini.alert(json.msg, "", function () {
                                return;
                            });
                        }
                        grid.reload();
                        grid.unmask();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        mini.alert(jqXHR.responseText);
                    }
                });
            } else {
                return;
            }
        });
    } else {
        mini.alert("请选中一条记录");
        return;
    }
}

function updateObjects(grid, url) {
    grid.commitEdit();
    var data = grid.getChanges();
    var json = mini.encode(data);
    grid.loading("保存中，请稍后......");
    $.ajax({
        url: url,
        data: json,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "post",
        success: function (json) {
            var isReload = true;
            if (json.code !== null && json.code !== 0 && json.msg !== null && json.msg !== undefined && json.msg !== '') {

                if (json.code !== 0) {
                    isReload = false;
                }
            }
            if (isReload) {
                grid.reload();
            } else {
                grid.unmask();
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText);
        }
    });
}

function beforeload(e, grid) {
    if (grid.getChanges().length > 0) {
        e.cancel = true;
        mini.confirm("有增删改的数据未保存，是否取消本次操作？", "提示信息", function (action) {
            if (action == "ok") {
                return;
            } else {
                grid.reload();
            }
        });
    }
}

function celleditenter(grid, e) {
    var index = grid.indexOf(e.record);
    if (index == grid.getData().length - 1) {
        var row = {};
        grid.addRow(row);
    }
}

//意见类型
function onOpTypeRender(e) {
    for (var i = 0; i < pageOpType.length; i++) {
        var str = pageOpType[i];
        if (str.id == e.value)
            return str.text;
    }
    return "";
}

//意见状态
function onOpStatusRender(e) {
    for (var i = 0; i < pageOpStatus.length; i++) {
        var str = pageOpStatus[i];
        if (str.id == e.value)
            return str.text;
    }
    return "";
}

function openFile(fileUrl) {
    window.location.href = fileUrl;// '<c:url value="/static/templete/ua_function_overview.ppt"/>';
}

function previewDocOnline(fileId, filePath, isReadOnly, title) {
    var dialogWidth = window.document.body.clientWidth - 50;
    var dialogHeight = window.document.body.clientHeight - 50;
    dialogWidth = dialogWidth > 1000 ? dialogWidth : 1000;
    dialogHeight = dialogHeight > 600 ? dialogHeight : 600;
    mini.open({
        url: 'fat/file/onlineFile?fileId=' + fileId + "&filePath=" + filePath + "&isReadonly=" + isReadOnly,
        showMaxButton: true,
        title: title,
        width: dialogWidth,
        height: dialogHeight
    });
}

//申请状态
function onAppTypeRenderer(e) {
    for (var i = 0; i < pageAppStatus.length; i++) {
        var str = pageAppStatus[i];
        if (str.id == e.value) {
            return str.text;
        }
    }
    return "";
}

//保存信息之后，刷新关联表格数据
function updateObject(grid, grid1, url) {
    grid.commitEdit();
    var data = grid.getChanges();
    var json = mini.encode(data);
    grid.loading("保存中，请稍后......");
    $.ajax({
        url: url,
        data: json,
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        type: "post",
        success: function (json) {
            var isReload = true;
            if (json.code !== null && json.code !== 0 && json.msg !== null && json.msg !== undefined && json.msg !== '') {
                if (json.code !== 0) {
                    isReload = false;
                }
            }
            if (isReload) {
                grid.reload();
                grid1.reload();
            } else {
                grid.unmask();
                grid.reload();
                grid1.reload();
            }
            mini.alert(json.msg);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            alert(jqXHR.responseText);
        }
    });
}

//删除信息之后，刷新关联表格数据
function deleteObject(grid, grid1, url) {
    var rows = grid.getSelecteds();
    if (rows.length > 0) {
        mini.confirm("确定删除选中记录?", "提示信息", function (action) {
            if (action == "ok") {
                //要发给后台处理的删除
                var dataRows = new Array();
                var j = 0;
                if (rows == null) {
                    return;
                }
                for (var i = 0; i < rows.length; i++) {
                    //新增的记录
                    if (rows[i]['_state'] == "added") {
                        continue;
                    } else {
                        rows[i]['alive'] = 0;
                        dataRows[j] = rows[i];
                        j++;
                    }
                }
                if (dataRows.length == 0) {
                    grid.removeRows(rows, false);
                    return;
                }
                var json = mini.encode(dataRows);
                grid.loading("保存中，请稍后......");
                $.ajax({
                    url: url,
                    data: json,
                    dataType: 'json',
                    contentType: 'application/json; charset=utf-8',
                    type: "POST",
                    success: function (json) {
                        if (json.code !== null && json.code !== 0 && json.msg !== null && json.msg !== undefined && json.msg !== '') {
                            mini.alert(json.msg, "", function () {
                                return;
                            });
                        }
                        grid1.reload();
                        grid.reload();
                        grid.unmask();
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        mini.alert(jqXHR.responseText);
                    }
                });
            } else {
                return;
            }
        });
    } else {
        mini.alert("请选中一条记录");
        return;
    }
}