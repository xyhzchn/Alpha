package action;

import bean.Case;
import bean.Page;
import bean.Tree;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import common.CommonParam;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.Range;
import jxl.write.Label;
import org.apache.struts2.ServletActionContext;
import service.CaseService;
import service.TreeService;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

/**
 * 测试用例对应action
 * Created by sofronie on 2017/8/1.
 */
public class CaseAction extends ActionSupport {

    //service接口
    private CaseService caseService;
    //定义接口
    private TreeService treeService;
    //用例定义
    private Case acase;
    //定义Page对象
    private Page pageBean;
    //用例list
    private List<Case> cases = new ArrayList<Case>();
    //目录list
    private List<Tree> trees = new ArrayList<Tree>();
    //所有的用例包含的优先级列表
    private List<String> priorityList = new ArrayList<String>();
    //所有的用例包含的版本号列表
    private List<String> versionList = new ArrayList<String>();
    //定义执行结果列表
    private Map<Integer,String> resultList = new HashMap<Integer, String>();
    //所有的自动化测试脚本列表
    private Map<Integer,String> scriptList = new HashMap<Integer, String>();
    //下载模板文件名称
    private String modelFileName = "测试用例模板.xls";
    //导出用例文件名称
    private String downFileName = "测试用例.xls";
    //上传文件 依赖struts文件上传定义，需要和jsp中文件name属性对应
    private File upload;
    //文件名 依赖struts文件上传定义，name属性值+FileName
    private String uploadFileName;
    //文件类型  依赖struts文件上传定义，name属性值+ContentType
    private String uploadContentType;
    //导出测试用例组
    private String[] ids;
    //节点id
    private int catalog_id;
    //分页
    private int page;
    //标记
    private String flag;
    //用例id
    private int caseId;
    //返回json对象定义
    private String json;
    //是否是查询
    private boolean is_search;

    /**
     * 获取用例列表并分页显示
     */
    public String getCaseList(){

        //如果用例bean为空
        if(acase == null || !is_search){

            acase = new Case();

            acase.setSearch(is_search);
            acase.setCatalog_id(catalog_id);//设置节点id

        }else{
                int projectId; //根节点
                //获取当前节点的等级
                int level = treeService.getCatalogLevelById(catalog_id);
                //若等级>0
                if(level > 0){
                    //通过子节点获取根节点的id
                    projectId = treeService.getParentIdBySonId(catalog_id);
                }else {
                    //如果节点的id = 0,说明该节点就是根节点
                    projectId = catalog_id;
                }

                //设置项目
                StringBuffer sb = new StringBuffer();
                //获取节点等级
                Set catalogids = new HashSet();
                //获取根节点下的所有子节点
                catalogids =treeService.getSonsById(projectId,new HashSet());
                //当节点列表>0
                if(catalogids.size() > 0){
                    //循环获取节点并添加到stringBuffer中
                    Iterator it = catalogids.iterator();
                    while (it.hasNext()){
                        sb.append(it.next());
                        sb.append(",");
                    }
                }
                //设置查询的节点列表
                acase.setSearch_ids(sb.toString().substring(0,sb.lastIndexOf(",")));

                //将选择的创建日期开始时间转换为timestamp类型
                if(acase.getSearch_timeFrom() != null && !(acase.getSearch_timeFrom().equals(""))){
                    String timeFromStr = acase.getSearch_timeFrom()+" 00:00:00";
                    acase.setSearch_From(Timestamp.valueOf(timeFromStr));
                }
                //将选择的创建日期截止时间转换为timestamp类型
                if(acase.getSearch_timeTo() != null && !(acase.getSearch_timeTo().equals(""))){
                    String timeToStr = acase.getSearch_timeTo()+" 00:00:00";
                    acase.setSearch_To(Timestamp.valueOf(timeToStr));
                }
            acase.setSearch(is_search);
        }

        //表示每页显示N条记录，page表示当前页
        pageBean = caseService.getPage(CommonParam.PAGE_SIZE,page,acase);
        //获取优先级列表
        priorityList = caseService.getCasePriority();
        //获取版本号列表
        versionList = caseService.getVersionList();
        //定义执行结果列表
        resultList.put(1,"PASS");
        resultList.put(2,"FAILURE");
        resultList.put(3,"BLOCK");

        //获取request对象
        HttpServletRequest request = ServletActionContext.getRequest();
        //设置request属性
        request.setAttribute("pageBean",pageBean);
        request.setAttribute("catalog_id",catalog_id);
        request.setAttribute("is_search",is_search);
//        request.setAttribute("acase",acase);
//        request.setAttribute("flag","allList");
//        request.setAttribute("ids",ids);

        return SUCCESS;
    }

    /**
     * 获取用例的详情信息
     * @return String
     */
    public String caseDetail(){
        //根据id查询用例
        acase = caseService.getCaseById(caseId);
        //获取脚本列表
        //获取session中存放的登录用户的id和角色id
        Map<String,Object> session = ActionContext.getContext().getSession();
        //角色id
        int userRole = Integer.parseInt(session.get("userRole").toString());
        //用户id
        int userId = Integer.parseInt(session.get("userId").toString());
        //自动化测试脚本列表
        scriptList = caseService.getScriptList(userRole, userId);

        //获取用例不为空
        if(acase != null){
            //设置request中标记为detail
            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute("flag","detail");

            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 修改测试用例
     * @return String
     */
    public String updateCase(){

        //设置用例id
        acase.setCase_id(caseId);

        //修改测试用例影响的记录条数
        Case aCase = caseService.updateCase(acase);

        //获取用例不为空
        if(aCase != null){
            //设置request中标记为detail
            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute("flag","detail");

            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 删除用例
     * @return String
     */
    public String deleteCase(){
        //影响的记录条数
        int num = caseService.deleteCase(caseId);
        //定义response
        HttpServletResponse response = ServletActionContext.getResponse();
        try{
            if(num > 0){
                response.getWriter().write("success");
            }else{
                response.getWriter().write("failure");
            }
            //关闭流
            response.getWriter().flush();
            response.getWriter().close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 创建excel并下载
     * @return 执行结果
     */
    public String createAndDownExcel(){

        //查询当前节点及当前节点的父级节点的信息列表
        trees = treeService.getCaseCatalog(catalog_id);
        //文件地址
        String filePath = ServletActionContext.getServletContext().getRealPath("/download/")+modelFileName;
        String dirPath = ServletActionContext.getServletContext().getRealPath("/download");
        if (trees.size()>0){

            try {
                //判断文件夹是否存在
                File dir = new File(dirPath);
                if(!dir.exists() && !dir.isDirectory()){
                    //若不存在，创建文件夹
                    dir.mkdir();
                }
                //判断文件是否存在
                File file = new File(filePath);
                if(file.exists()){
                    System.out.print("下载模板文件已存在");
                }else{
                    //不存在的话，创建文件
                    file.createNewFile();
                }

                //打开文件
                WritableWorkbook workbook = Workbook.createWorkbook(file);
                //生成名为【最后节点+测试用例】的工作表，参数0表示这是第一页
                String sheetName = "【"+trees.get(trees.size()-1).getCatalogName()+"】测试用例";
                WritableSheet sheet = workbook.createSheet(sheetName,0);
                //创建标题单元格样式
                WritableCellFormat wcfByTitle = setTitleFormat();
                //设置标题
                setExcelTitle(trees.size(),workbook,sheet,wcfByTitle);
                //创建单元格样式
                WritableCellFormat wcfByText = setTextFormat();

                //为单元格赋值
                for(int x=1;x<CommonParam.RECORD_NUM;x++){
                    //编辑模块名称
                    int count = trees.size();
                    for(int k=0;k<count;k++){
                        Label modelStr = new Label(k,x,trees.get(k).getCatalogName(),wcfByText);
                        sheet.addCell(modelStr);
                    }
                    //用例描述
                    Label caseTitleStr = new Label(count,x,"考试列表为空时，不展示搜索栏",wcfByText);
                    sheet.addCell(caseTitleStr);
                    //预置条件
                    Label preconditionStr = new Label(count+1,x,"考试列表为空",wcfByText);
                    sheet.addCell(preconditionStr);
                    //测试步骤
                    Label testStepStr = new Label(count+2,x,"1、点击录入后台的考试试卷管理 \n 2、验证[搜索栏]显示",wcfByText);
                    sheet.addCell(testStepStr);
                    //预期结果
                    Label expectedResStr = new Label(count+3,x,"不显示[搜索栏]",wcfByText);
                    sheet.addCell(expectedResStr);
                    //用例版本
                    Label caseVersionStr = new Label(count+4,x,"v1.0.0",wcfByText);
                    sheet.addCell(caseVersionStr);
                    //用例优先级
                    Label priorityStr = new Label(count+5,x,"P0",wcfByText);
                    sheet.addCell(priorityStr);
                    //用例优先级
                    Label caseDescStr = new Label(count+6,x,"",wcfByText);
                    sheet.addCell(caseDescStr);
                }
                workbook.write();
                workbook.close();

            }catch (IOException e){
                e.printStackTrace();
            }catch (WriteException e){
                e.printStackTrace();
            }
            //下载文件
            downloadFile(filePath,modelFileName);

            return SUCCESS;
        }else{
            return "failure";
        }
    }

    /**
     * 下载测试用例模板
     * @param filePath 文件路径
     * @param fileName 文件名称
     */
    public void downloadFile(String filePath,String fileName){

        HttpServletResponse response = ServletActionContext.getResponse();

        File file = new File(filePath);
        try {
            OutputStream out = response.getOutputStream();

            //判断文件是否存在
            if(!file.exists()){
                response.getWriter().write("文件没找到！");
                out.flush();
                return;
            }
            //定义输入流和输出流
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            //先将文件读入
            byte[] buffer = new byte[1024];
            int length = 0;

            response.reset();

            //response.setContentType("multipart/form-data");
            //设置文件头并定义编码格式
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));

            //设置文件的contextType类型，这样设置，会自动判断下载的文件类型
            response.setContentType(ServletActionContext.getServletContext().getMimeType(fileName));

            //读取数据并输出
            while ((length = bis.read(buffer)) > 0){
                //文件输出
                out.write(buffer,0,length);
            }

            bis.close();    //关闭输入流
            out.flush();    //清除输出流内容
            out.close();    //关闭输出流

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 导出测试用例
     * @return String
     */
    public String exportCase(){

        //定义存储位置和文件名字
        String filePath = ServletActionContext.getServletContext().getRealPath("/download/")+downFileName;
        String dirPath = ServletActionContext.getServletContext().getRealPath("/download");

        int projectId; //根节点
        //获取当前节点的等级
        int level = treeService.getCatalogLevelById(catalog_id);
        //若等级>0
        if(level > 0){
            //通过子节点获取根节点的id
            projectId = treeService.getParentIdBySonId(catalog_id);
        }else {
            //如果节点的id = 0,说明该节点就是根节点
            projectId = catalog_id;
        }

        //设置项目
        StringBuffer sb = new StringBuffer();
        //获取节点等级
        Set catalogids = new HashSet();
        //获取根节点下的所有子节点
        catalogids =treeService.getSonsById(projectId,new HashSet());

        //当节点列表>0
        if(catalogids.size() > 0){
            //循环获取节点并添加到stringBuffer中
            Iterator it = catalogids.iterator();
            while (it.hasNext()){
                sb.append(it.next());
                sb.append(",");
            }
        }

        //设置查询的节点列表
        acase.setSearch_ids(sb.toString().substring(0,sb.lastIndexOf(",")));

        //将选择的创建日期开始时间转换为timestamp类型
        if(acase.getSearch_timeFrom() != null && !(acase.getSearch_timeFrom().equals(""))){
            String timeFromStr = acase.getSearch_timeFrom()+" 00:00:00";
            acase.setSearch_From(Timestamp.valueOf(timeFromStr));
        }
        //将选择的创建日期截止时间转换为timestamp类型
        if(acase.getSearch_timeTo() != null && !(acase.getSearch_timeTo().equals(""))){
            String timeToStr = acase.getSearch_timeTo()+" 00:00:00";
            acase.setSearch_To(Timestamp.valueOf(timeToStr));
        }

        //获取选取的用例列表
        cases = caseService.getExportCaseList(acase);


        //若用例列表不为空
        if(cases.size()>0){
            try{

                //判断文件夹是否存在
                File dir = new File(dirPath);
                if (!dir.exists() && !dir.isDirectory()) {
                    //若不存在，创建文件夹
                    dir.mkdir();
                }
                //判断文件是否存在
                File file = new File(filePath);
                if (file.exists()) {
                    System.out.print("下载模板文件已存在");
                } else {
                    //不存在的话，创建文件
                    file.createNewFile();
                }

                //打开文件
                WritableWorkbook workbook = Workbook.createWorkbook(new File(filePath));
                //生成名为【最后节点+测试用例】的工作表，参数0表示这是第一页
                String sheetName = "测试用例";

                WritableSheet sheet = workbook.createSheet(sheetName,0);
                //创建标题单元格样式
                WritableCellFormat wcfByTitle = setTitleFormat();
                //创建单元格样式
                WritableCellFormat wcfByText = setTextFormat();


                //为单元格赋值 单元格从第二行开始，下标为1
                for (int x = 1; x < cases.size() + 1; x++) {
                     //获取目录列表
                     trees = treeService.getCaseCatalog(cases.get(x-1).getCatalog_id());


                     if (trees.size() > 0) {
                         //因为模块是从第二个单元格开始的，所以count+1
                         int count = trees.size();
                         //模块名称
                         for (int k = 0; k < count; k++) {
                             Label modelStr = new Label(k, x, trees.get(k).getCatalogName(), wcfByText);
                             sheet.addCell(modelStr);
                         }
                            //用例简述
                            Label caseTitleStr = new Label(count, x, cases.get(x - 1).getCaseTitle(), wcfByText);
                            sheet.addCell(caseTitleStr);
                            //预置条件
                            Label preconditionStr = new Label(count + 1, x, cases.get(x - 1).getPrecondition(), wcfByText);
                            sheet.addCell(preconditionStr);
                            //测试步骤
                            Label testStepStr = new Label(count + 2, x, cases.get(x - 1).getTestStep(), wcfByText);
                            sheet.addCell(testStepStr);
                            //预期结果
                            Label expectedResStr = new Label(count + 3, x, cases.get(x - 1).getExpectedRes(), wcfByText);
                            sheet.addCell(expectedResStr);
                            //用例版本
                            Label caseVersionStr = new Label(count + 4, x, cases.get(x - 1).getCaseVersion(), wcfByText);
                            sheet.addCell(caseVersionStr);
                            //用例优先级
                            Label priorityStr = new Label(count + 5, x, cases.get(x - 1).getPriority(), wcfByText);
                            sheet.addCell(priorityStr);
                            //用例优先级
                            Label caseDescStr = new Label(count + 6, x, cases.get(x - 1).getCaseDesc(), wcfByText);
                            sheet.addCell(caseDescStr);

                        }

                    }
                //设置标题
                setExcelTitle(trees.size(),workbook,sheet,wcfByTitle);

                workbook.write();
                workbook.close();
            }catch (IOException e){
                e.printStackTrace();

            }catch (WriteException e){
                e.printStackTrace();
            }

            downloadFile(filePath,downFileName);

            return SUCCESS;
        }else{
            return "failure";
        }
    }


    /**
     * 上传文件并持久化到数据库
     * @return String
     */
    public String uploadFile(){
        //获取文件地址
        String root = ServletActionContext.getServletContext().getRealPath("/upload");
        try {
            //判断文件夹是否存在
            File dir = new File(root);
            if(!dir.exists() && !dir.isDirectory()){
                //若不存在，创建文件夹
                dir.mkdir();
            }
            //输入流，在默认地址
            InputStream is =new FileInputStream(upload);
            //定义输出位置及文件名
            OutputStream os = new FileOutputStream(new File(root,uploadFileName));

            //定义缓存字节
            byte[] buffer = new byte[1024];
            int length = 0;
            //当文件中的字节！=-1时，一直读取文件
            while (-1 != (length = is.read(buffer,0,buffer.length))){
                //将缓存中的字节数，写入输出流
                os.write(buffer);
            }
            //关闭输出输入流
            os.flush();
            os.close();
            is.close();
        }catch (IOException e){
            e.printStackTrace();
            addFieldError("fileError","文件上传失败");
        }
        //上次文件成功后，将文件内容保存到数据库
        int  num = readFileAndIntoDB(uploadFileName);

        if(num >0){
            addFieldError("succ","用例上传成功！请刷新页面！");
            return SUCCESS;
        }else{
            addFieldError("succ","用例上传失败！请检查上传文档！");
            return "failure";
        }
    }

    /**
     * 读取上传文件的数据并保存到数据库
     * @param uploadFileName 文件名
     * @return int
     */
    public int  readFileAndIntoDB(String uploadFileName){

        int num = 0;

        boolean flag = true;

        try {
            //创建输入流
            InputStream is = new FileInputStream(ServletActionContext.getServletContext().getRealPath("/upload/")+"/"+uploadFileName);
            //读取excel文件对象
            Workbook workbook = Workbook.getWorkbook(is);
            //获取文件的指定工作表
            Sheet sheet = workbook.getSheet(0);
//            System.out.println(sheet.getRows());
            //定义标题行字段
            List<String> headStr = new ArrayList<String>();
            headStr.add("模块");
            headStr.add("用例描述");
            headStr.add("预置条件");
            headStr.add("测试步骤");
            headStr.add("预期结果");
            headStr.add("用例版本");
            headStr.add("用例优先级");
            headStr.add("备注");

            //模块长度
            int modelCount = 0;
//            System.out.println(sheet.getColumns());
            //获取父节点的信息
            int rootNode = treeService.getParentIdBySonId(catalog_id);
            String rootNodeName = treeService.getEnameById(rootNode);
            //循环所有的列
            for(int i=0;i<sheet.getColumns();i++){
                //当第一行的某一列的值为：模块时
                if(sheet.getCell(i,0).getContents().equals("模块")){
                    modelCount++;
                }
                //首行是否包含定义字符以外的字符
                if(!(headStr.contains(sheet.getCell(i,0).getContents()))){
                    flag = false;
                }
            }

            //定义插入用例列表
            List<Case> caseList = new ArrayList<Case>();
            //定义父级节点的id
            int parentId = catalog_id;
            //获取当前节点的等级
            int catalogLevel = treeService.getCatalogLevelById(catalog_id);
            //获取当前导入用例有最大的等级数
            int maxLevel = catalogLevel+modelCount;

            if(maxLevel > 4){
                addFieldError("succ","用例上传失败！节点等级>5级，请精简！");
            }else{
                //首行未包含定义字符以外的字符
                if(flag){
                    //获取所有的合并单元格
                    Range[] rangeCell = sheet.getMergedCells();
                    //循环所有的行，从第一行开始
                    for(int i=1;i<sheet.getRows();i++){
                        Case simpleCase = new Case();
                        //循环所有的列
                        for(int j=0;j<sheet.getColumns();j++){
                            String str = null;
                            //获取每一个单元格的值
                            str = sheet.getCell(j,i).getContents();
                            //循环所有的合并单元格
                            for (Range r : rangeCell) {
                                //如果当前行号>合并单元格左上角单元格的行号并且当前行号<=合并单元格右下角的行号
                                //并且当前列号>=合并单元格左上角的列表，并且当前列表<=合并单元格右下角的列表，
                                //即如果当前单元格在合并单元格内部。
                                if (i > r.getTopLeft().getRow()
                                        && i <= r.getBottomRight().getRow()
                                        && j >= r.getTopLeft().getColumn()
                                        && j <= r.getBottomRight().getColumn()) {
                                    //则该单元格的值我们设定为合并单元格的值
                                    str = sheet.getCell(r.getTopLeft().getColumn(),
                                            r.getTopLeft().getRow()).getContents();
                                }
                            }

//                            System.out.print(str+"\t");
                            //若第一行的内容是模块
                            if(sheet.getCell(j,0).getContents().equals("模块")){
                                //如果是第一列
                                if(j==0){
                                    //重置parentId
                                    parentId = catalog_id;
                                }
                                //如果单元格内容是空字符串
                                if(str.equals("")){
                                    continue;   //跳过
                                }
                                //若有指定节点则返回，若无则创建节点返回
                                parentId = treeService.getCatalogId(catalogLevel+(j+1),str,parentId);


                            }
                            //设置描述
                            if(sheet.getCell(j,0).getContents().equals("用例描述")){
                                simpleCase.setCaseTitle(str);
                            }
                            //设置预置条件
                            if(sheet.getCell(j,0).getContents().equals("预置条件")){
                                simpleCase.setPrecondition(str);
                            }
                            //设置测试步骤
                            if(sheet.getCell(j,0).getContents().equals("测试步骤")){
                                if(str.equals("")){
                                    addFieldError("succ","第"+(i+1)+"行第"+(j+1)+"列，[测试步骤]为空。请检查！");
                                    return 0;
                                }
                                simpleCase.setTestStep(str);
                            }
                            //设置预期结果
                            if(sheet.getCell(j,0).getContents().equals("预期结果")){
                                if(str.equals("")){
                                    addFieldError("succ","第"+(i+1)+"行第"+(j+1)+"列，[预期结果]为空。请检查！");
                                    return 0;
                                }
                                simpleCase.setExpectedRes(str);
                            }
                            //设置用例版本
                            if(sheet.getCell(j,0).getContents().equals("用例版本")){
                                simpleCase.setCaseVersion(str);
                            }
                            //设置用例优先级
                            if(sheet.getCell(j,0).getContents().equals("用例优先级")){
                                simpleCase.setPriority(str);
                            }
                            //设置备注
                            if(sheet.getCell(j,0).getContents().equals("备注")){
                                simpleCase.setCaseDesc(str);
                            }
                        }
                        //设置用例所属目录id
                        simpleCase.setCatalog_id(parentId);
                        //设置用例创建时间和修改时间
                        Date date = new Date();
                        Timestamp timestamp = new Timestamp(date.getTime());
                        simpleCase.setCreateTime(timestamp);
                        simpleCase.setUpdateTime(timestamp);
                        //设置用例创建者信息
                        Map session = ActionContext.getContext().getSession();
                        simpleCase.setCreatorId(Integer.parseInt(session.get("userId").toString()));
                        simpleCase.setCreatorName(session.get("userName").toString());
                        //设置用例所属的根节点英文名
                        simpleCase.setProject(rootNodeName);
                        System.out.println();

                        caseList.add(simpleCase);
                    }

                    //执行添加用例到数据库的操作
                    num = caseService.addCaseList(caseList);

                }else {
                    addFieldError("succ","标题行有误。请检查！");
                    return 0;
                }
            }
//            int modelCount = 0;
//            //循环所有的列
//            for(int i=0;i<sheet.getColumns();i++){
//                //当第一行的某一列的值为：模块时
//                if(sheet.getCell(i,0).getContents().equals("模块")){
//                   modelCount++;
//                }
//            }


//            Map map2 = new HashMap();
//            //获取所有的合并单元格
//            Range[] rangeCell = sheet.getMergedCells();
//            for(int i=0;i<sheet.getRows();i++) {
//                //循环所有的列
//                Map map = map2;
//                Case simpleCase = new Case();
//                for (int j = 0; j < sheet.getColumns(); j++) {
//                    String str = null;
//                    //获取每一个单元格的值
//                    str = sheet.getCell(j, i).getContents();
//                    //循环所有的合并单元格
//                    for (Range r : rangeCell) {
//                        //如果当前行号>合并单元格左上角单元格的行号并且当前行号<=合并单元格右下角的行号
//                        //并且当前列号>=合并单元格左上角的列表，并且当前列表<=合并单元格右下角的列表，
//                        //即如果当前单元格在合并单元格内部。
//                        if (i > r.getTopLeft().getRow()
//                                && i <= r.getBottomRight().getRow()
//                                && j >= r.getTopLeft().getColumn()
//                                && j <= r.getBottomRight().getColumn()) {
//                            //则该单元格的值我们设定为合并单元格的值
//                            str = sheet.getCell(r.getTopLeft().getColumn(),
//                                    r.getTopLeft().getRow()).getContents();
//                        }
//                    }
//                    //当单元格是模块时
//                    if(sheet.getCell(j,0).getContents().equals("模块") && i != 0) {
//
//                        //如果单元格是空值，跳过
//                        if(str.equals("")){
//                            continue;
//                        }
//                        //等级解析
//                        if(map.get(str) == null) {
//
//                            map.put(str,new HashMap());
//
//                        }
//
//                        if(sheet.getCell(j+1,i).getContents().equals("")
//                                || sheet.getCell(j+1,0).getContents().equals("用例描述")){
//                            map.remove(str);
//                            Map map3 = new HashMap();
//                            map.put(str,map3.put(i,simpleCase));
//                        }
//
//                        map = (HashMap)map.get(str);
//                    }
//
//                    //设置编号
//                    if(sheet.getCell(j,0).getContents().equals("编号") && i != 0){
//                        simpleCase.setCase_id(str);
//                    }
//                    //设置描述
//                    if(sheet.getCell(j,0).getContents().equals("用例描述") && i != 0){
//                        simpleCase.setCaseTitle(str);
//                    }
//                    //设置预置条件
//                    if(sheet.getCell(j,0).getContents().equals("预置条件") && i != 0){
//                        simpleCase.setPrecondition(str);
//                    }
//                    //设置测试步骤
//                    if(sheet.getCell(j,0).getContents().equals("测试步骤") && i != 0){
//                        simpleCase.setTestStep(str);
//                    }
//                    //设置预期结果
//                    if(sheet.getCell(j,0).getContents().equals("预期结果") && i != 0){
//                        simpleCase.setExpectedRes(str);
//                    }
//                    //设置用例版本
//                    if(sheet.getCell(j,0).getContents().equals("用例版本") && i != 0){
//                        simpleCase.setCaseVersion(str);
//                    }
//                    //设置用例优先级
//                    if(sheet.getCell(j,0).getContents().equals("用例优先级") && i != 0){
//                        simpleCase.setPriority(str);
//                    }
//                    //设置备注
//                    if(sheet.getCell(j,0).getContents().equals("备注") && i != 0){
//                        simpleCase.setCaseDesc(str);
//                    }
//                    //设置用例所属目录id
//                    simpleCase.setCatalog_id(0);
//                    //设置用例创建时间和修改时间
//                    Date date = new Date();
//                    Timestamp timestamp = new Timestamp(date.getTime());
//                    simpleCase.setCreateTime(timestamp);
//                    simpleCase.setUpdateTime(timestamp);
//                    //设置用例创建者信息
//                    Map session = ActionContext.getContext().getSession();
//                    simpleCase.setCreatorId(Integer.parseInt(session.get("userId").toString()));
//                    simpleCase.setCreatorName(session.get("userName").toString());
//
//                }
//                System.out.println();
//            }
        }catch (Exception e){
            e.printStackTrace();
        }
        //如果上传用例成功
        if(num>0){
            return num;
        }else{
            return 0;
        }
    }





    /**
     * 设置导出excel文件的标题信息
     */
    public void setExcelTitle(int count,WritableWorkbook workbook,WritableSheet sheet,WritableCellFormat wcfByTitle){

        try {
            //设置模块
            for(int j=0;j<count;j++){
                Label model = new Label(j,0,"模块",wcfByTitle);
                sheet.addCell(model);
            }
            Label caseTitle = new Label(count,0,"用例描述",wcfByTitle);
            sheet.addCell(caseTitle);
            Label precondition = new Label(count+1,0,"预置条件",wcfByTitle);
            sheet.addCell(precondition);
            Label testStep = new Label(count+2,0,"测试步骤",wcfByTitle);
            sheet.addCell(testStep);
            Label expectedRes = new Label(count+3,0,"预期结果",wcfByTitle);
            sheet.addCell(expectedRes);
            Label caseVersion = new Label(count+4,0,"用例版本",wcfByTitle);     //用例版本
            sheet.addCell(caseVersion);
            Label priority = new Label(count+5,0,"用例优先级",wcfByTitle);        //用例优先级
            sheet.addCell(priority);
            Label caseDesc = new Label(count+6,0,"备注",wcfByTitle);        //用例优先级
            sheet.addCell(caseDesc);

        }catch (WriteException e){
            e.printStackTrace();
        }
    }

    /**
     * 执行成功跳转到添加用例页面
     * @return 处理结果
     */
    public String getAddCasePage(){

        //获取session中存放的登录用户的id和角色id
        Map<String,Object> session = ActionContext.getContext().getSession();
        //角色id
        int userRole = Integer.parseInt(session.get("userRole").toString());
        //用户id
        int userId = Integer.parseInt(session.get("userId").toString());
        //自动化测试脚本列表
        scriptList = caseService.getScriptList(userRole, userId);

        //设置request中标记为detail
        HttpServletRequest request = ServletActionContext.getRequest();
        request.setAttribute("catalog_id",catalog_id);

        return SUCCESS;
    }

    /**
     * 添加测试用例
     * @return
     */
    public String addCase(){
        //根据节点获取父节点的id
        int parentId = treeService.getParentIdBySonId(catalog_id);
        //获取父节点的名字并赋值给project
        acase.setProject(treeService.getEnameById(parentId));
        //设置子节点的id
        acase.setCatalog_id(catalog_id);
        //设置用例创建时间和修改时间
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        acase.setCreateTime(timestamp);
        acase.setUpdateTime(timestamp);
        //设置用例创建者信息
        Map session = ActionContext.getContext().getSession();
        acase.setCreatorId(Integer.parseInt(session.get("userId").toString()));
        acase.setCreatorName(session.get("userName").toString());

        int num = caseService.addCase(acase);

        if(num > 0){
            return SUCCESS;
        }else {
            return "failure";
        }


    }
    /**
     * 设置标题样式
     * @return 标题样式
     */
    private WritableCellFormat setTitleFormat(){
        //样式实例化
        WritableCellFormat wcfByTitle = new WritableCellFormat();
        try {
            //定义样式
            wcfByTitle.setBorder(Border.ALL, BorderLineStyle.THIN);     //设置边框线
            Color color = Color.decode("#80C066");
            wcfByTitle.setBackground(Colour.LIGHT_GREEN);               //设置单元格背景颜色为浅绿色
            wcfByTitle.setAlignment(jxl.format.Alignment.CENTRE);       //设置单元格内容居中

        }catch (WriteException e){
            e.printStackTrace();
        }
        //返回样式
        return wcfByTitle;
    }


    /**
     * 设置导出文件文本内容的样式
     * @return 文本样式
     */
    private WritableCellFormat setTextFormat(){

        WritableCellFormat wcfByText = new WritableCellFormat();
        try {
            wcfByText.setBorder(Border.ALL, BorderLineStyle.THIN);      //设置边框线
            wcfByText.setAlignment(jxl.format.Alignment.LEFT);          //设置单元格内容居中
            wcfByText.setWrap(true);                                    //设置通过调整宽度和高度自动换行

        }catch (WriteException e){
            e.printStackTrace();
        }
        return wcfByText;
    }

    /**--------------------------------getter  and   setter--------------------------------*/

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public CaseService getCaseService() {
        return caseService;
    }

    public int getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }

    public void setCaseService(CaseService caseService) {
        this.caseService = caseService;
    }

    public Case getAcase() {
        return acase;
    }

    public void setAcase(Case acase) {
        this.acase = acase;
    }

    public List<Case> getCases() {
        return cases;
    }

    public void setCases(List<Case> cases) {
        this.cases = cases;
    }

    public List<Tree> getTrees() {
        return trees;
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
    }

    public File getUpload() {
        return upload;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getModelFileName() {
        return modelFileName;
    }

    public void setModelFileName(String modelFileName) {
        this.modelFileName = modelFileName;
    }

    public String getDownFileName() {
        return downFileName;
    }

    public void setDownFileName(String downFileName) {
        this.downFileName = downFileName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Page getPageBean() {
        return pageBean;
    }

    public void setPageBean(Page pageBean) {
        this.pageBean = pageBean;
    }

    public List<String> getPriorityList() {
        return priorityList;
    }

    public void setPriorityList(List<String> priorityList) {
        this.priorityList = priorityList;
    }

    public List<String> getVersionList() {
        return versionList;
    }

    public void setVersionList(List<String> versionList) {
        this.versionList = versionList;
    }

    public Map<Integer, String> getScriptList() {
        return scriptList;
    }

    public void setScriptList(Map<Integer, String> scriptList) {
        this.scriptList = scriptList;
    }

    public TreeService getTreeService() {
        return treeService;
    }

    public void setTreeService(TreeService treeService) {
        this.treeService = treeService;
    }

    public boolean isIs_search() {
        return is_search;
    }

    public void setIs_search(boolean is_search) {
        this.is_search = is_search;
    }

    public Map<Integer, String> getResultList() {
        return resultList;
    }

    public void setResultList(Map<Integer, String> resultList) {
        this.resultList = resultList;
    }
}


