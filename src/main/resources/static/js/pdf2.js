function addWater() {
    try {
        var objPDF = document.getElementById("oPDF");
        objPDF.ShowTitleBar(0);
        for (var i = 0; i < objPDF.PageCount; i++) {
            objPDF.AddWaterMark(i, "CSSC文档", 101, 50, 50, 192, 0, 25, 45);
        }
        //objPDF.ShowToolBar(0);
        objPDF.ShowToolbarButton(0, 0);
        objPDF.ShowToolbarButton(1, 0);
        objPDF.ShowToolbarButton(2, 0);
        objPDF.ShowToolbarButton(3, 0);
        objPDF.ShowToolbarButton(4, 0);

        objPDF.ShowToolbarButton(10, 0);
        objPDF.ShowToolbarButton(11, 0);
        objPDF.ShowToolbarButton(12, 0);

        objPDF.ShowToolbarButton(25, 0);
        objPDF.ShowToolbarButton(26, 0);
        objPDF.ShowToolbarButton(27, 0);

        objPDF.ShowToolbarButton(29, 0);
        objPDF.ShowToolbarButton(30, 0);
        objPDF.ShowToolbarButton(31, 0);
        objPDF.ShowBookmark(0);
        //objPDF.UnLockActiveX("SDKAXYX2327","5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
        //objPDF.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
        objPDF.UnLockActiveX("SDKAXFZ6499", "53C6D93FDA59F7FF80AFF1865B5F623EEF7DEA47");
        //objPDF.SaveAs("d:\\My Documents\\Visual Studio 2005\\Projects\\foritReader\\foritReader\\ecd.pdf");
    } catch (exception) {
        //alert("发生异常了");
        return;
    }
}

function openFile() {
    try {
        var objPDF = document.getElementById("oPDF");
        objPDF.ShowTitleBar(0);
        //objPDF.ShowToolBar(0);
        objPDF.ShowToolbarButton(0, 0);
        objPDF.ShowToolbarButton(1, 0);
        objPDF.ShowToolbarButton(2, 0);
        objPDF.ShowToolbarButton(3, 0);
        objPDF.ShowToolbarButton(4, 0);

        objPDF.ShowToolbarButton(10, 0);
        objPDF.ShowToolbarButton(11, 0);
        objPDF.ShowToolbarButton(12, 0);

        objPDF.ShowToolbarButton(25, 0);
        objPDF.ShowToolbarButton(26, 0);
        objPDF.ShowToolbarButton(27, 0);

        objPDF.ShowToolbarButton(29, 0);
        objPDF.ShowToolbarButton(30, 0);
        objPDF.ShowToolbarButton(31, 0);
        objPDF.ShowBookmark(0);
        //objPDF.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
        objPDF.UnLockActiveX("SDKAXFZ6499", "53C6D93FDA59F7FF80AFF1865B5F623EEF7DEA47");
    } catch (exception) {
        alert("你可能还没有安装PDF控件");
        window.location.href = "www.foxitReader.com";
    }
}

function showPDF() {
    try {
        var frsc = document.getElementById("oPDF");
        var mainDiv = document.getElementById("mainDiv");
        var code = document.getElementById("verifyCode").value;
        frsc.src = "PDFHandler.ashx?verifyCode=" + code;
        frsc.style.display = "block";
        mainDiv.style.display = "none";

    } catch (exception) {
        // alert(exception.message);
    }
}

function printPDF(fileid) {
    var objPDF = document.getElementById("oPDF");
    objPDF.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
    objPDF.PrintWithDialog();

    createHTTP();

    xmlhttp.open("GET", "AjaxFiles/FileOperationRecord.aspx?rfIds=" + fileid + "&oper=1", true);
    xmlhttp.send(null);

}

/*************************Ajax获取流Begin****************************/
var xmlhttp;

function createHTTP() {
    if (window.ActiveXObject) {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    } else if (window.XMLHttpRequest) {
        xmlhttp = new XMLHttpRequest();
    }
}

function startHTTP(fileid) {
    createHTTP();
    xmlhttp.onreadystatechange = StateDO;
    var rndpwd = (Math.random() * 10000000).toString().substring(0, 6);
    xmlhttp.open("GET", "dm/downloadFile?fileId=" + fileid + "&_r=" + rndpwd, true);
    xmlhttp.send(null);
}

function getFile2(fileids) {
    var sdk = document.getElementById("oPDF");

    sdk.ShowTitleBar(0);
    //sdk.ShowToolBar(0);
    sdk.ShowToolbarButton(0, 0);
    sdk.ShowToolbarButton(1, 0);
    sdk.ShowToolbarButton(2, 0);
    sdk.ShowToolbarButton(3, 0);
    sdk.ShowToolbarButton(4, 0);

    sdk.ShowToolbarButton(10, 0);
    sdk.ShowToolbarButton(11, 0);
    sdk.ShowToolbarButton(12, 0);

    sdk.ShowToolbarButton(25, 0);
    sdk.ShowToolbarButton(26, 0);
    sdk.ShowToolbarButton(27, 0);

    sdk.ShowToolbarButton(29, 0);
    sdk.ShowToolbarButton(30, 0);
    sdk.ShowToolbarButton(31, 0);
    sdk.ShowBookmark(0);

    sdk.UnLockActiveX("SDKAXFZ6499", "53C6D93FDA59F7FF80AFF1865B5F623EEF7DEA47");
    //sdk.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
    var sdkDiv = document.getElementById("oPDFDiv");
    sdkDiv.style.display = "none";
    var rndpwd = (Math.random() * 10000000).toString().substring(0, 6);
    //xmlhttp.open("GET", "dm/downloadFile?fileId=" + fileid + "&_r=" + rndpwd, true);

    var url_arr = window.location.href.split('/');
    var url_prefix = url_arr[0] + "//" + url_arr[2] + "/" + url_arr[3] + "/";
    sdk.OpenFile(url_prefix + "blardFile/downloadMergeFile?fileIds=" + fileids + "&_r=" + rndpwd, "");
    sdk.ShowTitleBar(0);
    //sdk.ShowToolBar(0);
    sdk.ShowToolbarButton(0, 0);
    sdk.ShowToolbarButton(1, 0);
    sdk.ShowToolbarButton(2, 0);
    sdk.ShowToolbarButton(3, 0);
    sdk.ShowToolbarButton(4, 0);

    sdk.ShowToolbarButton(10, 0);
    sdk.ShowToolbarButton(11, 0);
    sdk.ShowToolbarButton(12, 0);

    sdk.ShowToolbarButton(25, 0);
    sdk.ShowToolbarButton(26, 0);
    sdk.ShowToolbarButton(27, 0);

    sdk.ShowToolbarButton(29, 0);
    sdk.ShowToolbarButton(30, 0);
    sdk.ShowToolbarButton(31, 0);
    sdk.ShowBookmark(0);
    var sdkDiv = document.getElementById("oPDFDiv");
    sdkDiv.style.display = "";

}

function getFile(fileid) {
    startHTTP(fileid);
}

function StateDO() {
    if (xmlhttp.readyState == 1) {
        var sdk = document.getElementById("oPDF");

        sdk.ShowTitleBar(0);
        //sdk.ShowToolBar(0);
        sdk.ShowToolbarButton(0, 0);
        sdk.ShowToolbarButton(1, 0);
        sdk.ShowToolbarButton(2, 0);
        sdk.ShowToolbarButton(3, 0);
        sdk.ShowToolbarButton(4, 0);

        sdk.ShowToolbarButton(10, 0);
        sdk.ShowToolbarButton(11, 0);
        sdk.ShowToolbarButton(12, 0);

        sdk.ShowToolbarButton(25, 0);
        sdk.ShowToolbarButton(26, 0);
        sdk.ShowToolbarButton(27, 0);

        sdk.ShowToolbarButton(29, 0);
        sdk.ShowToolbarButton(30, 0);
        sdk.ShowToolbarButton(31, 0);
        sdk.ShowBookmark(0);

        sdk.UnLockActiveX("SDKAXFZ6499", "53C6D93FDA59F7FF80AFF1865B5F623EEF7DEA47");
        //sdk.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
        var sdkDiv = document.getElementById("oPDFDiv");
        sdkDiv.style.display = "none";

    }

    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var sdk = document.getElementById("oPDF");
            var bin = xmlhttp.ResponseBody;
            //alert(sdk);
            // alert(getLength(bin))
            var l = getLength(bin);
            //alert(bin);
            sdk.OpenBuffer(bin, l, "");
            ////添加水印
            //for (var i = 0; i < sdk.PageCount; i++) {
            //    sdk.AddWaterMark(i, "CSSC33", 250, 200, 50, 192, 0, 25, 45);
            //}

            //sdk.UnLockActiveX("SDKAXYX2327", "5DA637FC0F3F429ED37ABB14F718FCE0A0CAB5EB");
            sdk.UnLockActiveX("SDKAXFZ6499", "53C6D93FDA59F7FF80AFF1865B5F623EEF7DEA47");
            sdk.ShowTitleBar(0);
            //sdk.ShowToolBar(0);
            sdk.ShowToolbarButton(0, 0);
            sdk.ShowToolbarButton(1, 0);
            sdk.ShowToolbarButton(2, 0);
            sdk.ShowToolbarButton(3, 0);
            sdk.ShowToolbarButton(4, 0);

            sdk.ShowToolbarButton(10, 0);
            sdk.ShowToolbarButton(11, 0);
            sdk.ShowToolbarButton(12, 0);

            sdk.ShowToolbarButton(25, 0);
            sdk.ShowToolbarButton(26, 0);
            sdk.ShowToolbarButton(27, 0);

            sdk.ShowToolbarButton(29, 0);
            sdk.ShowToolbarButton(30, 0);
            sdk.ShowToolbarButton(31, 0);
            sdk.ShowBookmark(0);
            var sdkDiv = document.getElementById("oPDFDiv");
            sdkDiv.style.display = "";
        }
    }
}

/*************************Ajax获取流End****************************/

//**************关闭事件Begin****************//
function closeFile() {
    var sdk = document.getElementById("oPDF");
    //sdk=null;
    //sdk.AboutBox();
    try {
        //sdk.Save();
        //sdk.SaveToStream();
        var rndpwd = (Math.random() * 10000000).toString().substring(0, 6);
        //alert(rndpwd);

        sdk.SaveAs("C:\\Documents and Settings\\eb.ax");
        sdk.openFile("C:\\Documents and Settings\\eb.ax", "");
        sdk.AddPassword(rndpwd);

        sdk.CloseFile();
        //sdk.Dispose(false);
    } catch (exception) {

    }
    //Confirm("OK");
}

//**************关闭事件End****************//
function CloseWindow() {
    //window.parent.close();
}


