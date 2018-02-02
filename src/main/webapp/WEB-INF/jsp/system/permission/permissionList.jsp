<%--
  Created by IntelliJ IDEA.
  User: 孙建荣
  Date: 2017/5/2
  Time: 13:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
    pageContext.setAttribute("basePath", basePath);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>权限管理页面</title>
    <link href="${basePath}/plugins/bootstrap-3.3.0/css/bootstrap.min.css" rel="stylesheet"/>
    <%--<link href="${basePath}/plugins/bootstrap-table-1.11.0/bootstrap-table.min.css"--%>
    <%--rel="stylesheet"/>--%>
    <%--<link href="${basePath}/plugins/bootstrap-table-1.11.0/bootstrap-editable.css"
          rel="stylesheet"/>--%>
    <link rel="stylesheet" href="${basePath}/plugins/zTree_v3/css/zTreeStyle/zTreeStyle.css">
    <script src="${basePath}/plugins/jquery-3.2.1.min.js"></script>
    <script src="${basePath}/plugins/zTree_v3/js/jquery.ztree.core.min.js"></script>
    <script>
        var zTreeObj;
        // zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
        var setting = {
            view: {
                selectedMulti: true, //设置是否能够同时选中多个节点
                showIcon: true,      //设置是否显示节点图标
                showLine: true,      //设置是否显示节点与节点之间的连线
                showTitle: true,     //设置是否显示节点的title提示信息
            },
            data: {
                simpleData: {
                    enable: false,   //设置是否启用简单数据格式（zTree支持标准数据格式跟简单数据格式，上面例子中是标准数据格式）
                    idKey: "id",     //设置启用简单数据格式时id对应的属性名称
                    pidKey: "pId"    //设置启用简单数据格式时parentId对应的属性名称,ztree根据id及pid层级关系构建树结构
                }
            },
            check: {
                enable: true         //设置是否显示checkbox复选框
            },
            /*callback: {
             onClick: onClick,             //定义节点单击事件回调函数
             onRightClick: OnRightClick,   //定义节点右键单击事件回调函数
             beforeRename: beforeRename,   //定义节点重新编辑成功前回调函数，一般用于节点编辑时判断输入的节点名称是否合法
             onDblClick: onDblClick,       //定义节点双击事件回调函数
             onCheck: onCheck              //定义节点复选框选中或取消选中事件的回调函数
             },*/
            async: {
                enable: true,                      //设置启用异步加载
                type: "get",                       //异步加载类型:post和get
                contentType: "application/json",   //定义ajax提交参数的参数类型，一般为json格式
                url: "/Design/Get",                //定义数据请求路径
                autoParam: ["id=id", "name=name"]  //定义提交时参数的名称，=号前面标识节点属性，后面标识提交时json数据中参数的名称
            }
        };
        // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解）
        var zNodes = [
            {
                name: "父节点1", open: true, children: [
                {name: "父节点1的子节点"}, {name: "父节点1的子节点2"}]
            },
            {
                name: "父节点2", open: true,
                children: [{
                    name: "父节点2的子节点"
                }, {
                    name: "父节点2的子节点2",
                    children: [
                        {name: "父节点2的子节点2的子节点1"}]
                }]
            }
        ];
        $(document).ready(function () {
            zTreeObj = $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        });
    </script>
</head>
<body>
<%--<div class="panel-body" style="padding-bottom:0;">--%>

<%--<div id="toolbar" class="btn-group">--%>
<%--<button id="btn_add" type="button" class="btn btn-default btn-info">--%>
<%--<span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新增--%>
<%--</button>--%>
<%--<button id="btn_edit" type="button" class="btn btn-default btn-warning">--%>
<%--<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改--%>
<%--</button>--%>
<%--<button id="btn_delete" type="button" class="btn btn-default btn-danger">--%>
<%--<span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除--%>
<%--</button>--%>
<%--</div>--%>
<%--<table id="mable" class="table table-hover"></table>--%>
<%--</div>--%>

<div  class="panel panel-default">
    <div class="panel-heading">{{title}}</div>
    <form class="form-horizontal">
        <div class="form-group">
            <div class="col-sm-2 control-label">用户名</div>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="user.username" placeholder="登录账号"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">所属部门</div>
            <div class="col-sm-10">
                <input type="text" class="form-control" style="cursor:pointer;" v-model="user.deptName"
                       @click="deptTree" readonly="readonly" placeholder="所属部门"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">密码</div>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="user.password" placeholder="密码"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">邮箱</div>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="user.email" placeholder="邮箱"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">手机号</div>
            <div class="col-sm-10">
                <input type="text" class="form-control" v-model="user.mobile" placeholder="手机号"/>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">角色</div>
            <div class="col-sm-10">
                <label v-for="role in roleList" class="checkbox-inline">
                    <input type="checkbox" :value="role.roleId" v-model="user.roleIdList">{{role.roleName}}
                </label>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label">状态</div>
            <label class="radio-inline">
                <input type="radio" name="status" value="0" v-model="user.status"/> 禁用
            </label>
            <label class="radio-inline">
                <input type="radio" name="status" value="1" v-model="user.status"/> 正常
            </label>
        </div>
        <div class="form-group">
            <div class="col-sm-2 control-label"></div>
            <input type="button" class="btn btn-primary" @click="saveOrUpdate" value="确定"/>
            &nbsp;&nbsp;<input type="button" class="btn btn-warning" @click="reload" value="返回"/>
        </div>
    </form>
</div>

<form class="form-inline">
    <div class="form-group">
        <label for="exampleInputName2">Name</label>
        <input type="text" class="form-control" id="exampleInputName2" placeholder="Jane Doe">
    </div>
    <div class="form-group">
        <label for="exampleInputEmail2">Email</label>
        <input type="email" class="form-control" id="exampleInputEmail2" placeholder="jane.doe@example.com">
    </div>
    <button type="submit" class="btn btn-default">Send invitation</button>
</form>

<div style="display: none;padding: 10px;">
    <ul id="treeDemo" class="ztree"></ul>
</div>

</body>

<%--<script src="${basePath}/plugins/bootstrap-table-1.11.0/bootstrap-table.min.js"></script>--%>
<%--<script src="${basePath}/plugins/bootstrap-3.3.0/js/bootstrap.min.js"></script>--%>
<%--<script src="${basePath}/plugins/bootstrap-table-1.11.0/locale/bootstrap-table-zh-CN.js"></script>--%>
<%--<script src="${basePath}/plugins/bootstrap-table-1.11.0/extensions/export/bootstrap-table-export.js"></script>--%>
<%--<script src="${basePath}/plugins/bootstrap-table-1.11.0/extensions/tableExport.js"></script>--%>
<%--<script src="${basePath}/plugins/BootstrapMenu.min.js"></script>--%>
<%--<script src="${basePath}/plugins/layer/layer.js"></script>--%>
<%--suppress JSUnresolvedVariable --%>
<script>


    function refresh() {
        $("#mable").bootstrapTable("refresh");
    }
    /* $(function () {
     //1.初始化Table
     var oTable = new TableInit();
     oTable.Init();
     //2.初始化Button的点击事件
     var oButtonInit = new ButtonInit();
     oButtonInit.Init();
     //根据窗口调整表格高度
     $(window).resize(function () {
     $('#mable').bootstrapTable('resetView', {
     height: tableHeight()
     })
     });
     });*/


    function tableHeight() {
        return $(window).height() - 20;
    }
    var TableInit = function () {
        var oTableInit = {};
        //初始化Table
        oTableInit.Init = function () {
            layer.load(0, {shade: false, time: 1000}); //0代表加载的风格，支持0-2
            $('#mable').bootstrapTable({
                url: '${basePath}/system/permission/selectByParam.action',         //请求后台的URL（*）
                method: 'get',                      //请求方式（*）
                toolbar: '#toolbar',                //工具按钮用哪个容器
                striped: true,                      //是否显示行间隔色
                cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                pagination: true,                   //是否显示分页（*）
                smartDisplay: false,                 //是否关闭智能隐藏分页按钮
                sortable: true,                     //是否启用排序
                sortOrder: "asc",                   //排序方式
                showPaginationSwitch: 'true',       //取消或者显示分页
                height: tableHeight(),    //高度调整
                buttonsAlign: "right",//按钮对齐方式
                toolbarAlign: "left",//工具栏对齐方式
                queryParams: oTableInit.queryParams,//传递参数（*）
                sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                pageNumber: 1,                       //初始化加载第一页，默认第一页
                pageSize: 10,                       //每页的记录行数（*）
                pageList: [5, 10, 25, 50, 100],        //可供选择的每页的行数（*）
                search: true,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
                strictSearch: true,
                showColumns: true,                  //是否显示所有的列
                showRefresh: true,                  //是否显示刷新按钮
                minimumCountColumns: 2,             //最少允许的列数
                clickToSelect: true,                //是否启用点击选中行
                singleSelect: true,           // 单选checkbox
                //height: 500,                        行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
                uniqueId: "permissionId",                     //每一行的唯一标识，一般为主键列
                showToggle: true,                    //是否显示详细视图和列表视图的切换按钮
                cardView: false,                    //是否显示详细视图
                showExport: true,                     //是否显示导出
                exportDataType: "basic",              //basic', 'all', 'selected'.
                detailView: false,                   //是否显示父子表
                rowStyle: function (row, index) {
                    //这里有5个取值代表5中颜色['active', 'success', 'info', 'warning', 'danger'];
                    var subclass = "";
                    if (row.accountStatus === false) {
                        subclass = 'danger';//还有一个active
                    }
                    else if (row.accountName === "已删除") {
                        subclass = 'danger';
                    }
                    else {
                        return {};
                    }
                    return {classes: subclass}
                },
                columns: [
                    {title: "全选", field: "select", checkbox: true, width: 20, align: "center", valign: "middle"},
                    {title: "ID", field: "permissionId", sortable: true, order: "value", align: "center"},
                    {
                        field: "permissionName",
                        title: "权限名",
                        sortable: true,
                        titleTooltip: "this is name",
                        align: "center"
                    },
//                    {field: "accountPassword", title: "密码", order: "value"},
                    {field: "description", title: "权限解释", sortable: true, order: "value", align: "center"},
                    {
                        field: "createTime",
                        title: "创建时间",
                        sortable: true,
                        order: "value",
                        align: "center",
                        formatter: "dateFormat"
                    },
                    {
                        field: "permissionStatus",
                        title: "权限状态",
                        sortable: true,
                        order: "value",
                        formatter: "permissionStatusFormatter",
                        align: "center"
                    },

                ],
                onClickRow: function (row, $element) {
                    //$element是当前tr的jquery对象
                    // $element.css("background-color", "#29a176");
                },//单击row事件
                locale: "zh-CN", //中文支持,
                detailFormatter: function (index, row, element) {
                    var html = '';
                    $.each(row, function (key, val) {
                        html += "<p>" + key + ":" + val + "</p>"
                    });
                    return html;
                }
            });
        };

        //得到查询的参数
        oTableInit.queryParams = function (params) {
            return {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                limit: params.limit,   //页面大小
                offset: params.offset,  //页码
                status: $("#txt_search_status").val()
            };
        };
        return oTableInit;
    };

    function permissionStatusFormatter(code, row, index) {
        if (code === true) {
            return '<span class="label label-success account-status">正常</span>';
        }
        if (code === false) {
            return '<span class="label label-default account-status">冻结</span>';
        }
    }

    function dateFormat(code, row, index) {
        var date = row.createTime;
        var Y = date.year + '-';
        var M = date.monthValue + '-';
        var D = date.dayOfMonth + ' ';
        var h = date.hour + ':';
        var m = date.minute + ':';
        var s = date.second;
        return Y + M + D + h + m + s;
    }


    var ButtonInit = function () {
        var oInit = {};
        oInit.Init = function () {
            //初始化页面上面的按钮事件
        };
        return oInit;
    };

    //新增按钮的方法
    $("#btn_add").click(function () {
        insertion();
    });
    function insertion() {
        //弹出即全屏
        layer.open({
            type: 2,
            content: '${basePath}/system/permission/insert.html',
            area: ['320px', '580px'],
            maxmin: true
        });
    }

    //编辑按钮的方法
    $("#btn_edit").click(function () {
        var selectedRadio = $('#mable').bootstrapTable('getSelections');
        if (selectedRadio.length === 0) {
            layer.msg('请先勾选你要编辑的一行数据。。', {icon: 5});
        } else {
            var permissionId = selectedRadio[0].permissionId;
            editMember(permissionId);
        }
    });

    /**
     * 准备编辑表格操作
     */
    function editMember(permissionId) {
        //iframe层-父子操作
        layer.open({
            type: 2,
            area: ['300px', '620px'],
            fixed: true, //不固定
            maxmin: true,
            content: '${basePath}/system/permission/update.html/' + permissionId
        });

    }

    //删除按钮的方法
    $("#btn_delete").click(function () {
        var selectedRadio = $('#mable').bootstrapTable('getSelections');
        if (selectedRadio.length === 0) {
            layer.msg('请先勾选一条你要删除的权限数据。。', {icon: 5});
        } else {
            //询问框
            layer.confirm('您确定要删除【' + selectedRadio[0].permissionName + "】这条权限的信息吗?", {
                btn: ['确定', '点错了'] //按钮
            }, function () {
                layer.msg('准备删除了', {icon: 1});
                var permissionId = selectedRadio[0].permissionId;
                delectation(permissionId);
            }, function () {
                layer.msg('已经取消了', {
                    time: 20000 //20s后自动关闭
                });
            });
        }
    });


    /**
     * 提交删除表格操作
     * */
    function delectation(permissionId) {
        $.ajax({
                type: "get",
                url: "${basePath}/system/permission/deleteById.action/" + permissionId,
                success: function (result) {
                    var statusCode = result.status;
                    if (statusCode == 103) {
                        layer.msg("删除成功，请刷新后查看效果", {icon: 1});
                        layer.load(0, {shade: false, time: 1000});
                        $("#mable").bootstrapTable("refresh");
                    }
                    else if (statusCode == 115) {
                        layer.msg("权限存在引用，无法删除", {icon: 4});
                    }
                    else if (statusCode == 7) {
                        layer.msg("权限为系统初始权限限制，不能够删除", {icon: 4});
                    }
                    else if (statusCode == 5) {
                        layer.msg("没有查询到你要删除的权限", {icon: 4});
                    }
                },
                error: function () {
                    layer.msg('出了点小问题！', {icon: 1});
                }
            }
        )
    }
</script>
</html>



























