//table打印自动分页JS
AutoPage = {
    header: null,//页面顶部显示的信息
    content: null,//页面正文TableID
    footer: null,//页面底部
    totalHeight: null,//总的高度
    tableCss: null,//正文table样式
    divID: null,//全文divID

    init: function (header, content, footer, totalHeight, tableCss, divID, type) {
        AutoPage.header = header;
        AutoPage.content = content;
        AutoPage.footer = footer;
        AutoPage.totalHeight = totalHeight;
        AutoPage.tableCss = tableCss;
        AutoPage.divID = divID;
        //初始化分页信息
        if (type == 1)
            AutoPage.initPageSingle();
        else if (type == 2)
            AutoPage.initPageDouble();
        //隐藏原来的数据
        AutoPage.hidenContent();
        //开始分页
        //AutoPage.beginPage();

    },
    //分页 重新设定HTML内容(单行)
    initPageSingle: function () {

        var tmpRows = $("#" + AutoPage.content)[0].rows; //表格正文
        var height_tmp = 0; //一页总高度
        var html_tmp = "";  //临时存储正文
        var html_header = "<table class='" + AutoPage.tableCss + "'>";
        var html_foot = "</table>";
        var page = 0; //页码

        var tr0Height = tmpRows[0].clientHeight; //table标题高度
        var tr0Html = "<tr>" + tmpRows[0].innerHTML + "</tr>";//table标题内容
        height_tmp = tr0Height;
        for (var i = 1; i < tmpRows.length; i++) {
            var trHtmp = tmpRows[i].clientHeight;//第i行高度
            var trMtmp = "<tr>" + tmpRows[i].innerHTML + "</tr>";//第i行内容

            height_tmp += trHtmp;
            if (height_tmp < AutoPage.totalHeight) {
                if (height_tmp == tr0Height + trHtmp) {
                    html_tmp += AutoPage.header + html_header + tr0Html;
                    page++;//页码
                }
                html_tmp += trMtmp;
                if (i == tmpRows.length - 1) {
                    html_tmp += html_foot + AutoPage.footer;
                }
            } else {
                html_tmp += html_foot + AutoPage.footer + AutoPage.addPageBreak();
                i--;
                height_tmp = tr0Height;
            }
        }


        $("#" + AutoPage.divID).html(html_tmp);

        var tdpagecount = $("*[name='tdPageCount']");//document.getElementsByName("tdPageCount");
        for (var i = 0; i < tdpagecount.length; i++) {
            tdpagecount[i].innerText = (i + 1) + "/" + page;
        }

    },

    //分页 重新设定HTML内容(双行)
    initPageDouble: function () {

        var tmpRows = $("#" + AutoPage.content)[0].rows; //表格正文
        var height_tmp = 0; //一页总高度
        var html_tmp = "";  //临时存储正文
        var html_header = "<table class='" + AutoPage.tableCss + "'>";
        var html_foot = "</table>";
        var page = 0; //页码

        var tr0Height = tmpRows[0].clientHeight + tmpRows[1].clientHeight; //table标题高度
        var tr0Html = "<tr>" + tmpRows[0].innerHTML + "</tr>" + "<tr>" + tmpRows[1].innerHTML + "</tr>";//table标题内容
        height_tmp = tr0Height;
        for (var i = 1; i < tmpRows.length; i++) {
            var trHtmp = tmpRows[(i - 1) * 2].clientHeight + tmpRows[(i - 1) * 2 + 1].clientHeight;//第i行高度
            var trMtmp = "<tr>" + tmpRows[(i - 1) * 2].innerHTML + "</tr>" + "<tr>" + tmpRows[(i - 1) * 2 + 1].innerHTML + "</tr>";//第i行内容

            height_tmp += trHtmp;
            if (height_tmp < AutoPage.totalHeight) {
                if (height_tmp == tr0Height + trHtmp) {
                    html_tmp += AutoPage.header + html_header + tr0Html;
                    page++;//页码
                }
                html_tmp += trMtmp;
                if (i == tmpRows.length - 1) {
                    html_tmp += html_foot + AutoPage.footer;
                }
            } else {
                html_tmp += html_foot + AutoPage.footer + AutoPage.addPageBreak();
                i--;
                height_tmp = tr0Height;
            }
        }


        $("#" + AutoPage.divID).html(html_tmp);

        var tdpagecount = $("*[name='tdPageCount']");//document.getElementsByName("tdPageCount");
        for (var i = 0; i < tdpagecount.length; i++) {
            tdpagecount[i].innerText = (i + 1) + "/" + page;
        }

    },

    //隐藏原来的数据
    hidenContent: function () {
        $(AutoPage.header).hide();
        $(AutoPage.content).hide();
        $(AutoPage.footer).hide();
    },

    ////添加分页符
    addPageBreak: function () {
        return "<p style='page-break-before:always;'></p>";
    },

};
