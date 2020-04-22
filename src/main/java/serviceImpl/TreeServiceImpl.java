package serviceImpl;

import bean.Tree;
import dao.TreeDao;
import daoImpl.TreeDaoImpl;
import service.TreeService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by sofronie on 2017/7/27.
 */
public class TreeServiceImpl implements TreeService{

    //注入dao层
    private TreeDao treeDao;

    /**
     * 创建节点
     * @return 添加的节点数量
     */
    public int setRootNote(Tree tree){
        return treeDao.setRootNote(tree);
    }

    /**
     * 查询数据库中所有的节点信息
     * @return 所有节点列表
     */
    public List<Tree> selectAllNodes(){
       return  treeDao.selectAllNodes();
    }

    /**
     * 删除节点
     * @param catalog_id 节点id
     * @return 删除节点数量
     */
    public int deleteNode(int catalog_id){
        return treeDao.deleteNode(catalog_id);
    }

    /**
     * 修改节点
     * @return 修改节点数量
     */
    public int updateNote(Tree tree){
        return treeDao.updateNote(tree);
    }

    /**
     * 获取当前节点的所有父级节点列表
     * @param catalog_id 节点id
     * @return 节点列表
     */
    public List<Tree> getCaseCatalog(int catalog_id){
        return treeDao.getCaseCatalog(catalog_id);
    }

    /**
     * 根据节点Id获取到节点等级
     * @param catalogId 节点id
     * @return 节点等级
     */
    public int getCatalogLevelById(int catalogId){
        return treeDao.getCatalogLevelById(catalogId);
    }

    /**
     * 根据level和name获取节点，若节点存在，则获取该节点的id。或节点不存在，则创建节点，
     * 返回该节点的id
     * @param level 节点等级
     * @param name  节点名字
     * @param parentId  父节点
     * @return  节点id
     */
    public int getCatalogId(int level,String name,int parentId){
        return treeDao.getCatalogId(level,name,parentId);
    }

    /**
     * 获取项目列表
     * @return 项目列表
     */
    public Map<Integer,String> getProjectList(){
        return treeDao.getProjectList();
    }

    /**
     * 通过子节点的id获取到根节点的id
     * @return 根节点的id
     */
    public int getParentIdBySonId(int catalogId){
        return treeDao.getParentIdBySonId(catalogId);
    }

    /**
     * 根据一个节点查询所有的子节点
     * @param catalogId 节点id
     * @return 所有的子节点
     */
    public Set getSonsById(int catalogId,Set set){
        return treeDao.getSonsById(catalogId,set);
    }

    /**
     * 根据节点id获取节点的英文名
     * @param catalogId 节点id
     * @return 节点英文名
     */
    public String getEnameById(int catalogId){
        return treeDao.getEnameById(catalogId);
    }

    /**--------------------------getter and setter-----------------------------*/
    public TreeDao getTreeDao() {
        return treeDao;
    }

    public void setTreeDao(TreeDao treeDao) {
        this.treeDao = treeDao;
    }
}
