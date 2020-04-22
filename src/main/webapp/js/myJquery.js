/**
 *
 * Created by sofronie on 2017/7/19.
 */

//全选和取消全选
function checkAll(obj) {

    if(obj.checked){
        $("input[name='checkbox']").prop("checked",true);
    }else{
        $("input[name='checkbox']").prop("checked",false);
    }
}
<!-------------------------------------角色相关----------------------------->
