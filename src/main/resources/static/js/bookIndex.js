/**
 * 《工作包管理》
 * Created by  on 2019/9/12.
 */

$(document).ready(function () {
    pageInit();

});
var qx = true; //权限
function pageInit() {
    mini.parse();
    page.initCtrl();
    page.initMethod();
    page.event();
}

var page = {
    messageStr: {
        Info: "提示",
        Question: "确定删除记录？",
        Warning: "警告！",
        Error: "操作失败！",
        succeess: "操作成功",
        Download: "导入中，请稍后！",
        msgNoSel: "请至少选择一条记录!",
        selectRow: "请选择一条数据!"
    },
    initCtrl: function () {
        this.tool = new PageTool();
        //btn(增删改查)
        this.btnSearch = mini.get("btnSearch");
        this.btnAdd = mini.get("btnAdd");
        this.btnBorrow = mini.get("btnBorrow");
        this.btnReturn = mini.get("btnReturn");
        this.btnSale = mini.get("btnSale");
        this.btnLog = mini.get("btnLog");
        this.btnExcel = mini.get("btnExcel");
        //弹出窗口的按钮
        this.add_submitButton = mini.get("add_submitButton");
        this.btnBorrowWin = mini.get("btnBorrowWin");
        this.btnReturnWin = mini.get("btnReturnWin");

        //借阅内容
        this.borrow_bookId = mini.get("borrow_bookId");//书籍号
        this.borrow_userName = mini.get("borrow_userName");//借阅人
        this.borrow_dueDate = mini.get("borrow_dueDate");//计划归还日期
        //归还内容
        this.return_bookId = mini.get("return_bookId");//书籍号
        this.return_userName = mini.get("return_userName");//归还人
        this.return_damageLevel = mini.get("return_damageLevel");//破损程度

        this.win1 = mini.get("win1");
        this.win2 = mini.get("win2");
        this.win3 = mini.get("win3");
        this.win4 = mini.get("win4");


        this.bookGrid = mini.get("bookGrid");
        this.logGrid = mini.get("logGrid");
        this.log_txt = mini.get("log_txt");
        this.bookId = "";
    },
    initMethod: function () {
    },
    event: function () {
        //查询按钮
        this.btnSearch.on('click', this.tool.myBind(this.searchData, page));
        this.btnAdd.on('click', this.tool.myBind(this.insertData, page));
        this.btnBorrow.on('click', this.tool.myBind(this.borrowWin, page));
        this.btnReturn.on('click', this.tool.myBind(this.returnWin, page));
        this.btnLog.on('click', this.tool.myBind(this.logWin, page));
        this.btnExcel.on('click', this.tool.myBind(this.reportExcel, page));
        this.add_submitButton.on('click',this.tool.myBind(this.addBookForm, page));
        this.btnBorrowWin.on('click',this.tool.myBind(this.borrowData, page));
        this.btnReturnWin.on('click',this.tool.myBind(this.returnData, page));

    },
    searchData: function () {
        this.bookGrid.load();
    },
    insertData: function () {
        this.win1.show();
    },

    addBookForm: function () {
       // var form = new mini.Form("#bookForm");
       // if (form.validate()) {
            // 使用原生的 form 提交方法
        //    document.getElementById("bookForm").submit();
       // }
        $.ajax({
            url: '/api/books/addBook',
            type: 'POST',
            data: $('#bookForm').serialize(), // 序列化表单数据
            contentType: 'application/x-www-form-urlencoded; charset=UTF-8',
            success: function(response) {
                alert('Form submitted successfully');
                page.win1.hide();
            },
            error: function(error) {
                console.error('Error submitting form:', error);
            }
        });
    },

    borrowWin: function(){
        var selRow = page.bookGrid.getSelected();
        page.bookId = selRow.id;
        debugger;
        if (selRow){
            page.win2.show();
            page.borrow_bookId.setValue(selRow.isbn);
        }else{
            mini.alert("请选择需要借阅的书籍！");
            return;
        }
    },

    borrowData: function () {
       // var selRow = page.bookGrid.getSelected();
        $.ajax({
            url: '/api/borrow/borrowingBooks',
            type: 'POST',
            data: {
                bookId:page.bookId,
                userId:page.borrow_userName.getValue(),
                dueDate:page.borrow_dueDate.getValue()
            },
            success: function(response) {
                mini.alert(response.msg);
                page.win2.hide();
            },
            error: function(error) {
                console.error('Error submitting form:', error);
            }
        });
    },

    returnWin: function () {
        page.win3.show();
    },

    returnData:function () {
        $.ajax({
            url: '/api/borrow/returnBooks',
            type: 'POST',
            data: {
                bookId:page.return_bookId.getValue(),
                userId:page.return_userName.getValue(),
                damageLevel:page.return_damageLevel.getValue()
            },
            success: function(response) {
                if (response != '' && response != 'undefined'){
                    mini.alert(response.msg);
                }else{
                    alert('归还成功！');
                }
                page.win3.hide();
            },
            error: function(error) {
                console.error('Error submitting form:', error);
            }
        });
    },

    logWin:function () {
        $.ajax({
            url: '/getFileLog',
            type: 'GET',
            success: function(response) {
                page.log_txt.setValue(response);
                page.win4.show();
            },
            error: function(error) {
                console.error('Error submitting form:', error);
            }
        });
    },

    reportExcel: function () {
        document.getElementById("excelForm").action = "/api/report/reportData";
        var excelForm = document.getElementById("excelForm");
        excelForm.submit();
    }



}
