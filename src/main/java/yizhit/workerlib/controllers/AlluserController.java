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

@RestController
@RequestMapping("/asyncapi/sync")
public class AlluserController extends BaseController {
    /**
     * 同步所有员工信息
     */
    @ResponseBody
    @AccessCtrl
    @RequestMapping(value = "alluser",method = RequestMethod.GET)
    public ResponseData SynAlluser(){
        if(SelectQuartzAllUserInfo.isActived) {
            return message("数据正在同步中，请稍后查看数据...");
        }
        ThreadUtils.async(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    SelectQuartzAllUserInfo.isActived = true;
                }
                SelectQuartzAllUserInfo userInfo = new SelectQuartzAllUserInfo();
                userInfo.batchInsertArchivesInfo();
                synchronized (this) {
                    SelectQuartzAllUserInfo.isActived = false;
                }
            }
        });
        return message("数据同步执行成功");
    }



}
