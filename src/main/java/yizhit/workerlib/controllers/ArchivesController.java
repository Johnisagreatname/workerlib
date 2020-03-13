package yizhit.workerlib.controllers;

import ccait.ccweb.annotation.AccessCtrl;
import ccait.ccweb.controllers.BaseController;
import ccait.ccweb.model.ResponseData;
import entity.tool.util.ThreadUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import yizhit.workerlib.timer.SelectQuartzAllUserInfo;
import yizhit.workerlib.timer.SelectQuartzArvhivesInfo;

@RestController
@RequestMapping("/asyncapi/sync")
public class ArchivesController extends BaseController {

    /**
     * 同步工程下的人员信息
     */
    @ResponseBody
    @AccessCtrl
    @RequestMapping(value = "archives",method = RequestMethod.GET)
    public ResponseData SynArchives(){
        if(SelectQuartzArvhivesInfo.isActived) {
            return message("数据正在同步中，请稍后查看数据...");
        }
        ThreadUtils.async(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    SelectQuartzArvhivesInfo.isActived = true;
                }
                SelectQuartzArvhivesInfo selectQuartzArvhivesInfo = new SelectQuartzArvhivesInfo();
                selectQuartzArvhivesInfo.batchInsertArvhivesInfo();
                synchronized (this) {
                    SelectQuartzArvhivesInfo.isActived = false;
                }
            }
        });
        return message("数据同步执行成功");
    }






}
