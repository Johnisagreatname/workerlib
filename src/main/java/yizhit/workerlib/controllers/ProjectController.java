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
import yizhit.workerlib.timer.SelectQuartzProjectInfo;

@RestController
@RequestMapping("/asyncapi/sync")
public class ProjectController extends BaseController {

        /**
         * 同步工程信息
         */
        @ResponseBody
        @AccessCtrl
        @RequestMapping(value = "project",method = RequestMethod.POST)
        public ResponseData  SynAlluser(){
            if(SelectQuartzProjectInfo.isActived) {
                return message("数据正在同步中，请稍后查看数据...");
            }
            ThreadUtils.async(new Runnable() {
                @Override
                public void run() {
                    synchronized (this) {
                        SelectQuartzProjectInfo.isActived = true;
                    }
                    SelectQuartzProjectInfo selectQuartzProjectInfof= new SelectQuartzProjectInfo();
                    selectQuartzProjectInfof.batchInsertProjectInfo();
                    synchronized (this) {
                        SelectQuartzProjectInfo.isActived = false;
                    }
                }
            });
            return message("数据同步执行成功");
       }

}
