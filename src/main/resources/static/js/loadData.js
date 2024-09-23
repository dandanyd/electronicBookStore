/**
 * Created by n72515 on 2018/2/8.
 */

var contentPath = getOriginPath() + "/basicSWSTime/";

function getOriginPath() {
    //直接掉nui-mini.js中的isIE方法
    if (isIE) {
        var origin = window.location.protocol + "//" + window.location.hostname + (window.location.port ? ':' + window.location.port : '');
        return origin;
    } else
        return window.location.origin;
}

//方法有点问题,谷歌也会识别成ie,
// function isIE(){
//     if (window.navigator.userAgent.indexOf("MSIE")>=1)
//         return true;
//     else
//         return false;
// }


//公司
function getCompany(cmbControl, selectIndex) {
    $.ajax({
        url: contentPath + 'out/org/getCompanyList',
        type: 'post',
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
        }
    });
}

//号船
function getProjects(cmbControl, cmbToControl, selectIndex) {
    $.ajax({
        url: contentPath + 'sem/getProjByUserNo',
        type: "get",
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            //console.log(data);
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            cmbToControl.setData(data);
            // cmbToControl.select(0);
        }
    });
}

//MDM系统 SWS 工程号
function getSWSProjects(cmbControl, cmbToControl, selectIndex) {
    $.ajax({
        url: contentPath + 'sem/getProjByUserNo',
        type: "get",
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            cmbToControl.setData(data);
            // cmbToControl.select(0);
        }
    });
}

//MDM系统 supplier 工程号
function getMdmProjects(cmbControl, cmbToControl, selectIndex) {
    $.ajax({
        url: contentPath + 'company/getProjNoListBySupplier',
        type: "get",
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            //console.log(data);
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            cmbToControl.setData(data);
            // cmbToControl.select(0);
        }
    });
}

//MDM系统 supplier 工程号
function getApplyProjNoList(cmbControl, selectIndex, projNo, deviceNo) {
    $.ajax({
        url: contentPath + 'company/getApplyProjNoList?projNo=' + projNo + "&deviceNo=" + deviceNo,
        type: "get",
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            // cmbToControl.select(0);
        }
    });
}

//MDM系统 All 工程号
function getMdmAllProjects(cmbControl, cmbToControl, selectIndex) {
    $.ajax({
        url: contentPath + 'company/getMdmAllProjects',
        type: "post",
        async: false,
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            cmbToControl.setData(data);
            // cmbToControl.select(0);
        }
    });
}

//fat系统 All 工程号 api方式
function getFatAllProjectsApi(compCd, cmbControl, cmbToControl, selectIndex) {
    $.ajax({
        url: contentPath + 'fat/sem/getFatAllProjectsApi',
        data: {compCd: compCd},
        type: "post",
        async: false,
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            cmbToControl.setData(data);
            // cmbToControl.select(0);
        }
    });
}

//fat系统 All 工程号 非Api
function getFatAllProjects(compCd, cmbControl, selectIndex) {
    $.ajax({
        url: contentPath + 'fat/common/getFatAllProjects',
        data: {compCd: compCd},
        type: "get",
        async: false,
        success: function (data) {
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            // cmbToControl.select(0);
        }
    });
}

function getFatProjectsWithAll(compCd, cmbControl, selectIndex) {
    $.ajax({
        url: contentPath + 'fat/common/getFatAllProjects',
        data: {compCd: compCd},
        type: "get",
        async: false,
        success: function (data) {
            data.unshift({"projNo": 'ALL'});
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            // cmbToControl.select(0);
        }
    });
}

//MDM设备列表
function getMdmDeviceList(cmbControl, projNo, selectIndex) {

    $.ajax({
        url: contentPath + 'company/getDeviceInfoList',
        type: "get",
        async: false,
        dataType: 'json',
        data: {"projNo": projNo},
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            //console.log(data);
            cmbControl.setData(data);
            cmbControl.select(selectIndex);
            // cmbToControl.select(0);
        }
    });
}

//初始化起始日期和截至日期datePicker add by 13221 lyt
function setDate(startDt, endDt, days) {
    //获取三个月前的时间
    var day1 = new Date();
    day1.setTime(day1.getTime() - days * 24 * 60 * 60 * 1000);
    var s1 = day1.getFullYear() + '-' + (day1.getMonth() + 1) + '-' + day1.getDate();
    //获取今天的时间
    var day2 = new Date();
    day2.setTime(day2.getTime() + 1 * 24 * 60 * 60 * 1000);
    var s2 = day2.getFullYear() + '-' + (day2.getMonth() + 1) + '-' + day2.getDate();
    //给datePicker赋值
    startDt.setValue(s1);
    endDt.setValue(s2);
}

//供方联系人
//供方联系人
var spInfo = new Array();

function getSpInfo(supplierNo) {
    $.ajax({
        url: contentPath + 'fat/getSupplierInfo?supplierNo=' + supplierNo,
        type: 'get',
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            spInfo = data;
        }
    });
}

//检验员
var inspector = new Array();

function getInspector(inspectorControl, selectIndex) {
    $.ajax({
        url: contentPath + 'cp/getInspector',
        type: 'get',
        async: false,
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        success: function (data) {
            inspector = data;
            inspectorControl.setDate(data);
            inspectorControl.select(selectIndex);
        }
    });
}

// currentserInfo
var userId; // currentUserId
function getCurrentUserInfo() {
    $.get(contentPath + "indexInfo",
        function (data, status) {
            if (status == "success" && data) {
                userId = data.userId;
            }
        }
    );
}
