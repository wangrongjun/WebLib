/**
 *  by wangrongjun on 2017/8/28.
 */

/*
 需要配合jquery的treeview控件使用：
 $("#show").html(getFileTreeHtml(json));
 $("#show").addClass("filetree");
 $(".filetree span").click(function () {
 $(".filetree span").css("background-color", "#fff");
 $(this).css("background-color", "#ddd");
 alert($(this).attr("data-path"));
 });
 $(document).ready(function () {
 $(".filetree").treeview();
 });
 */

function getFileTreeHtml(json) {
    var s = "";
    for (var i = 0; i < json.children.length; i++) {
        s += "<li class='closed'><span class='folder' data-path='" +
            json.children[i].path + "'>" +
            json.children[i].folderName + "</span>" +
            getFileTreeHtml(json.children[i]) +
            "</li>"
    }
    if (json.path == '/') {// 如果当前是根目录
        s = "<ul><span class='folder' data-path='/'>我的云盘</span>" + s + "</ul>"
    } else if (s.length > 0) {
        s = "<ul>" + s + "</ul>"
    }
    return s;
}

/*
 var treeViewTestJson = {
 "folderName": "file",
 "path": "/",
 "children": [{
 "folderName": "PDF",
 "path": "/PDF/",
 "children": [{
 "folderName": "03",
 "path": "/PDF/03/",
 "children": []
 }, {
 "folderName": "新建文件夹",
 "path": "/PDF/新建文件夹/",
 "children": []
 }]
 }, {
 "folderName": "音乐",
 "path": "/音乐/",
 "children": [{
 "folderName": "天地创造",
 "path": "/音乐/天地创造/",
 "children": []
 }]
 }]
 };
 */