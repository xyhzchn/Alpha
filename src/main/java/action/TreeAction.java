package action;

import bean.Tree;
import com.opensymphony.xwork2.ActionSupport;
import service.TreeService;
import serviceImpl.TreeServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by sofronie on 2017/7/27.
 */
public class TreeAction extends ActionSupport {

    //接口注入
    private TreeService treeService;
    //javabean
    private Tree tree = new Tree();
    //目录ID
    private int catalog_id;
    //目录列表
    private List<Tree> trees = new ArrayList<Tree>();
    //返回的json数据定义
    private String jsonData;
    //节点中文名称
    private String name;
    //节点英文名称
    private String ename;

    /**
     * 添加节点
     * @return 执行结果
     */
    public String setRootNote(){

        //将传过来的节点id，作为父节点id
        tree.setParent_id(catalog_id);

        //执行添加节点操作
        int num = treeService.setRootNote(tree);

        if (num>0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 查询数据库中所有的节点信息
     * @return 执行结果
     */
    public String selectAllNodes(){

        //查询所有的节点列表
        trees = treeService.selectAllNodes();
        //定义返回json
        StringBuffer sb = new StringBuffer("[");

        for (Tree atree:trees){

            sb.append("{id:"+atree.getCatalogId()+",");
            sb.append("pId:"+atree.getParent_id()+",");
            sb.append("name:\""+atree.getCatalogName()+"\",");
            if(atree.getLevel() != 0){
                sb.append("level:"+atree.getLevel()+",");
            }
            jsonData = sb.substring(0,sb.lastIndexOf(","))+"},";
            sb = new StringBuffer(jsonData);
        }
        jsonData = sb.substring(0,sb.length()-1)+"]";

        return SUCCESS;
    }

    /**
     * 修改节点信息
     * @return 执行结果
     */
    public String updateNote(){

        //设置节点信息
        tree.setCatalogId(catalog_id);
        tree.setCatalogName(name);
        tree.setEname(ename);
        //修改节点
        int num = treeService.updateNote(tree);

        if (num>0){
            return SUCCESS;
        }else {
            return "failure";
        }
    }

    /**
     * 删除节点
     * @return 是否删除成功
     */
    public String deleteNode(){

        //逻辑删除节点
        int num = treeService.deleteNode(catalog_id);
        //若影响记录条数>0
        if (num > 0){
            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 验证提交表单数据
     */
    public void validateSetRootNote(){
        if (tree.getCatalogName() == null || tree.getCatalogName().equals("")){
            addFieldError("nameError","节点中文名不可为空");
        }
        if (tree.getEname() == null || tree.getEname().equals("")){
            addFieldError("enameError","节点英文名不可为空");
        }
    }


    /**
     * 验证提交表单数据
     */
    public void validateUpdateNote(){
        if (name == null || name.equals("")){
            addFieldError("nameError","节点中文名不可为空");
        }
        if (ename == null || ename.equals("")){
            addFieldError("enameError","节点英文名不可为空");
        }
    }

    /**-------------------------------getter  and  setter---------------------------------*/

    public TreeService getTreeService() {
        return treeService;
    }

    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

}
