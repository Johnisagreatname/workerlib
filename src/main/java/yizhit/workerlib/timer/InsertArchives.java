package yizhit.workerlib.timer;

import ccait.ccweb.utils.EncryptionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import entity.query.Datetime;
import entity.tool.util.RequestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import yizhit.workerlib.entites.AllUserInfoUpdate;
import yizhit.workerlib.entites.ArchivesInfo;
import yizhit.workerlib.entites.InvoLvedproject;
import yizhit.workerlib.entites.PageNum;
import yizhit.workerlib.entites.ProjectWorkType;
import yizhit.workerlib.entites.UserModel;
import yizhit.workerlib.entites.WorkType;
import yizhit.workerlib.interfaceuilt.FinalUtil;
import yizhit.workerlib.uitls.DateUtils;
import yizhit.workerlib.uitls.ParamUitls;
import yizhit.workerlib.uitls.StringUitls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *@ClassName InsertArchives
 *@Description 
 *@Author xieya
 *@Date 2020/4/14  18:10
 */
public class InsertArchives {

    public static boolean isActived = false;

    @Value("${entity.security.encrypt.MD5.publicKey:ccait}")
    private String md5PublicKey;

    @Value("${entity.encoding:UTF-8}")
    private String encoding;

    @Value("${entity.upload.workerlib.people.code.path}")
    private String qrCodePath;     //图片地址

    @Value("${qrcode.width}")
    private int width;

    @Value("${qrcode.height}")
    private int height;

    @Value("${entity.security.encrypt.AES.publicKey:ccait}")
    private String aesPublicKey;

    @Value("${qrcode.server}")      //IP
    private String server;

    @Value("${enableTasks:false}")
    private Boolean enableTasks;

    private static final Logger logger = LogManager.getLogger(InsertArchives.class);

    /**
     * @Author xieya
     * @Description
     * @Date 2020/4/15 11:25
     * @param pageIndex 页码
     * @param sum       在线人员总数
     * @return void
     **/
    public void archives(int pageIndex, int sum) throws IOException, SQLException {

        logger.info("页码pageIndex=" + pageIndex);

        try {
            //查询数据库页码
            PageNum pageNum = new PageNum();
            if (pageIndex == 1) {
                List<PageNum> pageNumList = pageNum.where("[id]=1").select("page_index").query();
                Integer dbNum = pageNumList.get(0).getPageIndex();
                if (dbNum > 1) {
                    pageIndex = dbNum;
                }
            }

            //组装入参
            String com2user = ParamUitls.setParam(pageIndex, ParamUitls.com2user);
            logger.info("入参params=" + com2user);
            HashMap<String, String> header = new HashMap<>();
            header.put("Content-Type", "application/json");
            String com2userResult = RequestUtils.post(FinalUtil.url, com2user, header);
            logger.info("返回参数com2userResult=" + com2userResult);
            JSONObject com2userJson = JSONObject.parseObject(com2userResult);

            //数据获取正确
            if (com2userJson.containsKey("code") && com2userJson.get("code").equals("0")) {
                JSONArray array = com2userJson.getJSONObject("data").getJSONArray("list");
                int total = Integer.parseInt(String.valueOf(com2userJson.getJSONObject("data").get("total")));
                int pageSize = Integer.parseInt(String.valueOf(com2userJson.getJSONObject("data").get("pageSize")));
                int intTotal = total / pageSize;

                //工程下的用户信息
                List<ArchivesInfo> archivesInfoList = JSONArray.parseArray(array.toJSONString(), ArchivesInfo.class);
                if (archivesInfoList.size() == 500) {
                    pageIndex++;
                }
                for (ArchivesInfo archivesInfo : archivesInfoList) {

                    if ("进行中".equals(archivesInfo.getCwrUserStatus())) {
                        sum++;
                    }
                    //插入历史记录表信息
                    importProjectWorkerHistory(archivesInfo);
                    //工种表导入工种信息
                    importProjectWorkerType(archivesInfo);
                    //工程下的用户信息表
                    importArchives(archivesInfo);
                }
                logger.info("在线人数sum=" + sum);

                //递归
                if (pageIndex <= intTotal) {
                    pageNum.setPageIndex(pageIndex);
                    pageNum.where("[id]=1").update("[page_index]=#{pageIndex}");
                    archives(pageIndex, sum);
                }

                logger.info("操作完成");
                //清空页码数据
                pageNum.setPageIndex(1);
                pageNum.where("[id]=1").update("[page_index]=#{pageIndex}");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * @Author xieya
     * @Description 工程下的用户信息表
     * @Date 2020/4/13 11:00
     * @param archivesInfo
     * @return void
     **/
    private void importArchives(ArchivesInfo archivesInfo) {

        try {
            if (archivesInfo.getEafName() != null) {

                if ("进行中".equals(archivesInfo.getCwrUserStatus())) {
                    archivesInfo.setLeave(1);
                }
                if ("结束".equals(archivesInfo.getCwrUserStatus())) {
                    archivesInfo.setLeave(2);
                }
                //根据工程id和个人id查询一条数据(唯一)
                ArchivesInfo archives = archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}").first();

                UserModel account = new UserModel();
                account.setUsername(archivesInfo.getCwrIdnum());
                account = account.where("username=#{username}").first();

                if (StringUitls.isEmpty(archives)) {

                    if (account == null) {
                        //为空的时候  新建一条user数据
                        addUser(archives);
                    } else {
                        archivesInfo.setCreateBy(account.getId());
                    }

                    logger.info("archivesInfo=" + archivesInfo.toString());
                    archivesInfo.insert();
                    //跟新createBy和id一样
//                    archivesInfo.where("[createBy]=0").update("[createBy]=#{id}");
//                    archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}").update("[createBy]=#{id}");
                } else {
                    archivesInfo.where("[archives_id]=#{userid}").and("[project_id]=#{cwrPrjid}")
                            .update("[cwrUserStatus]=#{cwrUserStatus}," +
                                    "[eafId]=#{eafId}," +
                                    "[unit_id]=#{cwrComid}," +
                                    "[name]=#{eafName}," +
                                    "[phone]=#{eafPhone}," +
                                    "[cwrIdnumType]=#{cwrIdnumType}," +
                                    "[id_number]=#{cwrIdnum}," +
                                    "[CwrWorkClass]=#{CwrWorkClass}," +
                                    "[work_type]=#{CwrWorkName}," +
                                    "[createOn]=#{eafCreatetime}," +
                                    "[modifyBy]=#{createBy}," +
                                    "[modifyOn]=#{eafModifytime}," +
                                    "[createBy]=#{createBy}," +
                                    "[eafRLeftid]=#{eafRLeftid}," +
                                    "[cwrWorkclassId]=#{cwrWorkclassId}," +
                                    "[cwrWorktype]=#{cwrWorktype}," +
                                    "[cwrUserIn]=#{cwrUserIn}," +
                                    "[cwrUserOut]=#{cwrUserOut}," +
                                    "[leave]=#{leave}");
                }
            }
        } catch (Exception e) {
            logger.error("插入工程下的用户信息出错： ======>", e);
        }
    }

    private void addUser(ArchivesInfo archives) throws UnsupportedEncodingException, NoSuchAlgorithmException, SQLException {

        UserModel userModel = new UserModel();
        userModel.setUsername(archives.getCwrIdnum());
        String passWord = "";
        if (entity.tool.util.StringUtils.isEmpty(archives.getCwrIdnum()) || archives.getCwrIdnum().length() < 6) {
            passWord = "123456";
        } else {
            passWord = archives.getCwrIdnum().substring(archives.getCwrIdnum().length() - 6);
        }
        userModel.setPassword(EncryptionUtil.md5(passWord, md5PublicKey, encoding));
        userModel.setPath("0/1");
        userModel.setCreateBy(Integer.valueOf(1));
        userModel.setCreateOn(DateUtils.getCurrentDateTime());
        userModel.insert();

        //CreateBy和id一样
        userModel.where("[createBy]=0").update("[createBy]=#{id}");
    }


    /**
     * @Author xieya
     * @Description 工种表导入工种信息
     * @Date 2020/4/13 10:57
     * @param info
     * @return void
     **/
    private void importProjectWorkerType(ArchivesInfo info) {
        try {
            //给所有人员表插入单位ID
            AllUserInfoUpdate allUserInfoUpdate = new AllUserInfoUpdate();
            allUserInfoUpdate.setCwrIdnum(info.getCwrIdnum());
            allUserInfoUpdate.setUnitId(info.getCwrComid());
            if (StringUtils.isNotEmpty(info.getCwrIdnum()) && StringUtils.isNotEmpty(info.getCwrComid())) {
                allUserInfoUpdate.where("[cwrIdnum]=#{cwrIdnum}").update("[unit_id]=#{unitId}");
                allUserInfoUpdate = allUserInfoUpdate.where("[cwrIdnum]=#{cwrIdnum}").first();
            }

            if (allUserInfoUpdate == null) {
                return;
            }

            if (StringUtils.isEmpty(info.getCwrWorkName())) {
                return;
            }

            //给所有工种表导入工种信息
            ProjectWorkType projectWorkType = new ProjectWorkType();
            projectWorkType.setEafId(info.getUserid());
            projectWorkType.setProjectId(info.getCwrPrjid());
            projectWorkType.setWorkType(info.getCwrWorkName());
            projectWorkType.setCreateBy("1");
            projectWorkType.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            projectWorkType.setUserPath("0/1");
            ProjectWorkType jsEafid = projectWorkType.where("[eafId] = #{eafId}")
                    .and("[projectId]=#{projectId}").and("[WorkType]=#{workType}").first();
            if (jsEafid == null) {
                projectWorkType.insert();
            }

            //给工种表导入工种信息
            WorkType workType = new WorkType();
            workType.setEafId(allUserInfoUpdate.getEafId());
            workType.setWorkType(info.getCwrWorkName());
            workType.setCreateBy("1");
            workType.setCreateOn(Datetime.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            WorkType jstype_id = workType.where("[eafId] = #{eafId}").and("[WorkType]=#{workType}").first();
            if (jstype_id == null) {
                workType.insert();
            }
        } catch (Exception e) {
            logger.error("工种表导入工种信息出错： =========>", e);
        }
    }

    /**
     * @Author xieya
     * @Description 插入历史记录表信息
     * @Date 2020/4/13 10:56
     * @param info
     * @return void
     **/
    private void importProjectWorkerHistory(ArchivesInfo info) {
        try {
            //给历史记录表
            InvoLvedproject invoLvedproject = new InvoLvedproject();
            invoLvedproject.setArchivesId(info.getUserid());
            invoLvedproject.setProjectId(info.getCwrPrjid());
            invoLvedproject.setUnitId(info.getCwrComid());
            invoLvedproject.setStartTime(info.getCwrUserIn());
            invoLvedproject.setEnd_time(info.getCwrUserOut());
            invoLvedproject.setCreateBy(1);
            invoLvedproject.setCreateOn(DateUtils.getCurrentDate());
            InvoLvedproject js_id = invoLvedproject.where("[archives_id] = #{archives_id}").and("[project_id] = #{project_id}").first();
            if (js_id == null) {
                invoLvedproject.insert();
            } else {
                invoLvedproject.where("[archives_id] = #{archives_id}").and("[project_id] = #{project_id}").update("[unit_id] = #{unit_id},[start_time] = #{start_time},[end_time] = #{end_time}," +
                        "[createOn] = #{createOn},[createBy] = #{createBy}");
            }
        } catch (Exception e) {
            logger.error("历史记录表信息表出错： ======>", e);
        }
    }
}