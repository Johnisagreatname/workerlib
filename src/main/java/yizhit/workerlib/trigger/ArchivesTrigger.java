package yizhit.workerlib.trigger;

import ccait.ccweb.annotation.*;
import ccait.ccweb.filter.RequestWrapper;
import ccait.ccweb.model.QueryInfo;
import ccait.ccweb.utils.EncryptionUtil;
import ccait.ccweb.utils.UploadUtils;
import entity.query.Datetime;
import org.springframework.stereotype.Component;
import yizhit.workerlib.entites.ArchivesInfo;
import yizhit.workerlib.entites.InvoLvedproject;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Component
@Trigger(tablename = "archives") //触发器注解
public final class ArchivesTrigger {

    /***
     * 新增数据事件
     * @param list （提交的数据）
     * @param request （当前请求）
     * @throws Exception
     */
    @OnInsert
    public void onInsert(List<Map<String, Object>> list, HttpServletRequest request) throws Exception {
        for(Map item : list) {
            item.put("leave",(int) item.get("leave"));
            item.put("cwrUserIn",Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));

            //插入一条数据到历史记录表
            InvoLvedproject lvedproject = new InvoLvedproject();
            lvedproject.setArchivesId((String) item.get("archives_id"));
            lvedproject.setProjectid((String) item.get("project_id"));
            lvedproject.setUnitId((String)item.get("unit_id"));
            lvedproject.setUserPath((String)item.get("userPath"));
            lvedproject.setStartTime(Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.setCreateBy((long)item.get("createBy"));
            lvedproject.setCreateOn(Datetime.format((Date)item.get("createOn"), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.insert();

        }
        RequestWrapper wrapper = (RequestWrapper) request;
        wrapper.setPostParameter(list);
    }


    /***
     * 更新数据事件
     * @param queryInfo （提交的数据）
     * @param request （当前请求）
     * @throws Exception
     */
    @OnUpdate
    public void onUpdate(QueryInfo queryInfo, HttpServletRequest request) throws Exception {
        Map item = queryInfo.getData();
        ArchivesInfo archivesInfo = new ArchivesInfo();
        InvoLvedproject lvedproject = new InvoLvedproject();
        if((int)item.get("leave") == 2) {
            archivesInfo.setLeave(2);
            archivesInfo.setCwrUserOut(Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            item.put("leave", 2);
            item.put("cwrUserOut", Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));

            //修改involvedproject
            lvedproject.setEnd_time(Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.setArchivesId((String) item.get("archives_id"));
            lvedproject.setModifyBy(item.get("modifyBy").toString());
            lvedproject.setCreateOn(Datetime.format((Date)item.get("modifyOn"), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.setEnd_time(Datetime.format((Date)item.get("modifyOn"), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.where("[archives_id]=#{archives_id}").update("[modifyOn]=#{modifyOn},[modifyBy]=#{modifyBy},[end_time]=#{end_time}");
        }else{
            item.put("leave", 1);
            item.put("cwrUserIn", Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            item.put("cwrUserOut", null);
            //插入一条数据到历史记录表
            lvedproject.setArchivesId((String) item.get("archives_id"));
            lvedproject.setProjectid((String) item.get("project_id"));
            lvedproject.setUnitId((String) item.get("unit_id"));
            lvedproject.setUserPath((String) item.get("userPath"));
            lvedproject.setStartTime(Datetime.format(Datetime.now(), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.setCreateBy((long)item.get("createBy"));
            lvedproject.setCreateOn(Datetime.format((Date)item.get("createOn"), "yyyy-MM-dd HH:mm:ss"));
            lvedproject.insert();

        }
        RequestWrapper wrapper = (RequestWrapper) request;
        wrapper.setPostParameter(queryInfo);
    }
}

