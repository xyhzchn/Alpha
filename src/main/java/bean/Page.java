package bean;

import java.util.List;

/**
 *
 * Created by sofronie on 2017/8/8.
 */
public class Page {

    //通过sql从数据库分页查询出来的list集合
    private List list;
    //总记录数
    private int allRows;
    //总页数
    private int totalPage;
    //当前页
    private int currentPage;

    /**
     * 得到总页数
     * @param pageSize  每页记录数
     * @param allRows   总记录数
     * @return int
     */
    public int getTotalPages(int pageSize,int allRows){
        //三目运算符
        int totalPage = (allRows%pageSize == 0)? (allRows/pageSize):(allRows/pageSize)+1;

        return totalPage;
    }

    /**
     * 得到当前开始的记录号
     * @param pageSize 每页记录数
     * @param currentPage 当前页
     * @return int
     */
    public int getCurrentPageOffset(int pageSize,int currentPage){

        int offset = pageSize * (currentPage-1);

        return offset;
    }

    /**
     * 得到当前页，如果为0，则从第一页开始，否则为当前页
     * @param page 页号
     * @return int
     */
    public int getCurPage(int page){

        int currentPage = (page == 0)?1:page;

        return currentPage;
    }



    /**--------------------------------getter and setter---------------------------*/

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public int getAllRows() {
        return allRows;
    }

    public void setAllRows(int allRows) {
        this.allRows = allRows;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
}
