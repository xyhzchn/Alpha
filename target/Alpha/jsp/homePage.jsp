<%--
  Created by IntelliJ IDEA.
  User: sofronie
  Date: 2017/7/21
  Time: 11:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="s" uri="/struts-tags" %>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>测试用例管理系统</title>
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/common.css" type="text/css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/zTreeStyle.css" type="text/css">
  <link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui.css">
  <script src="<%=request.getContextPath()%>/js/jquery-3.2.1.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery-ui.js"></script>
  <script src="<%=request.getContextPath()%>/js/jquery.ztree.all.js"></script>
  <!--弹出框样式，暂时写在文件中，因在外部样式表中总是不起效果-->
  <style type="text/css">
    .theme-popover-mask {
      position:fixed;
      top:0;
      left:0;
      width:100%;
      height:100%;
      background:#000;
      opacity:0.4;
      filter:alpha(opacity=40);
      display:none
    }
    .theme-popover {
      position:fixed;
      top:50%;
      left:40%;
      width:400px;
      height:300px;
      margin:-180px 0 0 -30px;
      border-radius:5px;
      border:solid 1px #666;
      background-color:#fff;
      display:none;
      box-shadow: 0 0 10px #666;
    }
    .theme-poptit {
      border-bottom:1px solid #ddd;
      padding:12px;
      position: relative;
    }
    .theme-popbod {
      padding:60px 15px;
      color:#444;
      height: 148px;
    }

    .theme-poptit .close {
      float:right;
      color:#999;
      margin:-2px -5px -5px;
      font:bold 14px/14px simsun;
      text-shadow:0 1px 0 #ddd
    }
    .theme-poptit .close:hover {
      color:#444;
    }

    .dform {
      padding-top: 5%;
      text-align: center;
    }
    .ztree li span.button.add {
      margin-left:2px;
      margin-right: -1px;
      background-position:-144px 0;
      vertical-align:top;
      *vertical-align:middle;
      background: url(<%=request.getContextPath()%>/img/addNode.png);
      width:82px;
      height:20px;
    }
    .ztree li span.button.edit {
      margin-left:2px;
      margin-right: -1px;
      background-position:-144px 0;
      vertical-align:top;
      *vertical-align:middle;
      background: url(<%=request.getContextPath()%>/img/editNode.png);
      width:47px;
      height:20px;
    }
    .ztree li span.button.remove {
      margin-left:2px;
      margin-right: -1px;
      background-position:-144px 0;
      vertical-align:top;
      *vertical-align:middle;
      background: url(<%=request.getContextPath()%>/img/delNode.png);
      width:45px;
      height:20px;
    }
  </style>
  <script>
    <!--左侧菜单栏事件定义-->
      $(function() {
          $( "#accordion" ).accordion({
              heightStyle: "content"
          });
      });
    <!--弹出框事件定义-->
    function alertAddNodeDiv(id){
        if(id == 'addNoteLink'){
            //当点击的是“添加项目”时，设置catalog_id = 0
            document.getElementById('catalog_id').value = 0;
        }
        $('.theme-popover-mask').fadeIn(100);
        $('.theme-popover').slideDown(200);
    }
    function closeAddNodeDiv(){
        $('.theme-popover-mask').fadeOut(100);
        $('.theme-popover').slideUp(200);
    }

    var zTreeObj;
    var zNodes;
    var setting = {
        check: {
            //没有复选框
            enable: false  //不可选择
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",      //节点的id,对应json中的id
                pIdKey: "pId",    //节点的parent_id，对应json的pId
                rootPId: 0        //根节点设置为0
            },
            key: {
                name: "name"      //节点的name属性，对应json中的name
            }
        },
        async: {                  //异步加载
            enable: true,
            url: 'nodeQuery.action',
            dataFilter: filter    //渲染
        },
        view:{
            expandSpeed:"",
            addHoverDom:addHoverDom,          //用于当鼠标移动到节点时，显示用户自定义的控件，务必与setting.view.removeHoverDom同时使用
            removeHoverDom:removeHoverDom,    //用户当鼠标移出节点时，隐藏用户自定义的控件，务必与addHoverDom同时使用
            dbClickExpand:false,
            selectedMulti:false,
            showLine:true
        },
        edit:{
            enable:true,
            editNameSelectAll:true,      //节点编辑名称，input初次显示时，设置txt内容是否为全选状态
            showRenameBtn:true,
            showRemoveBtn:true,
            removeTitle:"删除",          //删除按钮的Title辅助信息
            renameTitle:"重命名",        //编辑名称按钮的Title辅助信息
        },
        callback:{                      //回调函数

            beforeEditName:beforeEditName,    //用于捕获节点编辑按钮的click事件，并且根据返回值确定是否允许进入名称编辑状态
            beforeRemove:beforeRemove,          //用于捕获节点被删除之前的事件回调函数，并且根据返回值确定是否允许删除操作
            //beforeRename:beforeRename//用于捕获节点编辑名称结束（input失去焦点，或按下enter键）之后，更新节点名称数据之前的事件回调函数，并且根据返回值确定是否允许更改名称的操作
            //onRename:onRename,
            //beforeClick:beforeClick,
            onClick:zTreeOnClick,  //节点点击事件
            beforeAsync:beforeAsync,    //异步加载之前调用
            onAsyncSuccess:onAsyncSuccess  //异步加载数据成功之后调用
        }
    };
    //渲染
    function filter(treeId,parentNode,childNodes){
        if (!childNodes) return null;
        zNodes = eval("("+childNodes+")");
        return zNodes;
    }
    //调用
    $(document).ready(function(){
        //节点列表
        zTreeObj = $.fn.zTree.init($('#nodeTree'),setting,zNodes);
        //节点自动展开
        setTimeout(function(){
            expandAll("nodeTree");
        },1000);
    });
    //异步加载前
    function beforeAsync(){
      curAsyncCount++;
    }
    //异步加载成功后
    function onAsyncSuccess(event,treeId,treeNode,msg){
        curAsyncCount--;
        if(curStatus == "expand"){
          expandNodes(treeNode.children);
        }else if(curStatus == "async"){
          expandNodes(treeNode.children);
        }

        if(curAsyncCount <= 0){
            curStatus ="";
        }
    }
    //展开全部节点入口
    var curStatus = "init", curAsyncCount = 0,goAsync = false;
    function expandAll(){
        if(!check()){
            return;
        }
        var zTree = $.fn.zTree.getZTreeObj("nodeTree");
        expandNodes(zTree.getNodes());
        if(!goAsync){
            curStatus ="";
        }
    }
    //展开全部节点执行方法
    function expandNodes(nodes){
        if(!nodes) return;
        curStatus ="expand";
        var zTree = $.fn.zTree.getZTreeObj("nodeTree");
        for(var i=0,l=nodes.length;i<l;i++){
          zTree.expandNode(nodes[i],true,false,false);//展开节点就会调用后台查询子节点
            if(nodes[i].isParent && nodes[i].zAsync){
              expandNodes(nodes[i].children);//递归
            }else{
                goAsync = true;
            }
        }

    }
    //检查
    function check(){
        if(curAsyncCount > 0){
            return false;
        }
        return true;
    }
    //添加按钮和添加子节点操作定义
    var newCount = 1;
    function addHoverDom(treeId,treeNode) {

        var sObj = $("#"+treeNode.tId+"_span");
        if($("#userRole").val() == 1){
            //span不为空
            if(treeNode.editNameFlag || $("#addBtn_"+treeNode.tId).length > 0) return;
            if(treeNode.level < 4){
                //增加 添加子节点 按钮
                var addStr = "<span class='button add' id='addBtn_"+treeNode.tId+"' title='添加' onfocus='this.blur();'></span>";
                //span后面添加按钮
                sObj.after(addStr);
            }
            //添加子节点 按钮定义
            var btn = $("#addBtn_"+treeNode.tId);
            //添加子节点
            if(btn){
                newCount++;
                btn.bind("click",alertAddNodeDiv);
                var catalog_id = treeNode.id;
                //给隐藏域中的目录id赋值
                document.getElementById('catalog_id').value = catalog_id;
                return false;
            }
        }else{
            $("span[class='button edit']").detach();
            $("span[class='button remove']").detach();
        }
      };

    //用于当鼠标移出节点时，隐藏用户自定义的控件
    function removeHoverDom(treeId,treeNode) {
        $("#addBtn_"+treeNode.tId).unbind().remove();
    }

    //删除操作
    function beforeRemove(treeId,treeNode){

        //删除之前弹出框
        var zTree = $.fn.zTree.getZTreeObj("nodeTree");
        zTree.selectNode(treeNode);
        var isDel = confirm("确定删除节点["+treeNode.name+"]么?");

        //删除
        if(isDel){
            //删除之前判断
            if (treeNode.isParent){
                alert('要删除的是父节点，请先删除子节点');
                return false;
            }
          $.ajax({
              type:'post',
              url:'deleteNode.action',
              data:{
                  catalog_id:treeNode.id
              },
              dataType:"text",
              success:function(result){
                  alert("删除成功！");
              },
              error:function(e){
                  alert("删除失败！");
              }
          });
        }else {
            return isDel;
        }
    }

    //重命名节点
    function beforeEditName(treeId,treeNode){
        var zTree = $.fn.zTree.getZTreeObj("nodeTree");
        zTree.selectNode(treeNode);
        var isEdit = confirm("确定要重命名["+treeNode.name+"]吗？");

        if(isEdit){
            var btn = $('#' + treeNode.tId + '_edit');
            //编辑节点
            if(btn) {
                //设置隐藏域中目录ID的值
                var catalog_id = treeNode.id;
                document.getElementById('catalog_id').value = catalog_id;

                //弹出遮罩层
                $('.theme-popover-mask').fadeIn(100);
                $('.theme-popover').slideDown(200);
                    //保存按钮点击
                    $('#save').click(function () {
                        //获取节点中文名和英文名
                        var name = document.getElementById('name').value;
                        var ename = document.getElementById('ename').value;
                        //获取form1
                        form = document.forms['form1'];
                        //修改form1的action。
                        form.action = 'updateNote.action?name='+name+'&ename='+ename;
                        //form表单提交
                        form.submit();
                    });
                return false;
            }
        }else{
            return isEdit;
        }

    }
    //点击事件
    function zTreeOnClick(event,treeId,treeNode){
        //获取节点id
        document.getElementById('catalog_id').value = treeNode.id;
        //节点的a标签
        var aObj = $("#"+treeNode.tId+"_a");
        //a标签上增加属性
        aObj.attr("target","content");
        aObj.attr("href",'getCaseList.action?catalog_id='+treeNode.id);
        //修改a标签
        aObj.updateNode();
    }

  </script>
</head>
<body class="bodyTextStyle">
  <%@ include file="head.jsp"%>
  <div id="mainContent">
    <div id="menu">
      <div id="accordion">

        <h3>用例管理</h3>
            <div class="theme-buy">
              <a id="addNoteLink" class="aStyle" href="javascript:;" onclick="alertAddNodeDiv('addNoteLink')">添加项目</a>
              <ul id="nodeTree" class="ztree"></ul>
            </div>

        <h3>任务管理</h3>
          <div>
            <ul>
              <!--管理员权限下数据处理-->
              <s:if test="#session.userRole == 1">
              <li><a href="adminTaskList.action" target="content" class="aStyle">任务管理</a></li>
              </s:if>
              <!--管理员权限以外用户的数据处理-->
              <s:else>
              <li><a href="testerTaskList.action" target="content" class="aStyle">任务管理</a></li>
              </s:else>
            </ul>
          </div>
        <h3>脚本管理</h3>
        <div>
          <ul>
            <li><a href="script.action" target="content" class="aStyle">脚本列表</a></li>
          </ul>
        </div>
        <h3>用户管理</h3>
          <div>
            <ul>
              <li><a href="user.action" target="content" class="aStyle">用户管理</a></li>
              <li><a href="role.action" target="content" class="aStyle">角色管理</a></li>
              <!--<li><a href="#">权限管理</a></li>-->
            </ul>
          </div>
        <h3>报表统计</h3>
          <div>
            <p>敬请期待…</p>
          </div>
      </div>

    </div>
    <div id="pageDiv">
      <iframe id="content" name="content" frameborder="no" scrolling="no" width="100%" height="100%"></iframe>
    </div>
  </div>
  <div style="width: 100%;height: 100%;">
    <div class="theme-popover-mask"></div>
    <div class="theme-popover">
      <div class="theme-poptit">
        <a href="javascript:;" title="关闭" class="close" onclick="closeAddNodeDiv()">×</a>
      </div>
      <div class="theme-popbod dform">
        <s:form id="noteForm" name="form1" theme="simple" method="POST" action="addNode">
          <s:fielderror style="color:red" name="nameError" />
          <div class="textMar">
            <label>节点中文名称：</label>
            <s:textfield  name="tree.catalogName" class="textStyle" placeholder="请输入节点中文名称" id="name" />
          </div>
          <div class="textMar">
            <label>节点英文名称：</label>
            <s:textfield name="tree.ename" class="textStyle" placeholder="请输入节点英文名称" id="ename"/>
          </div>
          <div class="textMar">
            <s:submit id="save" class="btnStyle" value="保   存" />
          </div>
          <input type="hidden" id="catalog_id" name="catalog_id" value=""/>
        </s:form>
      </div>
    </div>
  </div>
  <input type="hidden" id="userRole" value="<s:property value="#session.userRole"/>" />
</body>
</html>
