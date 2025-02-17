package cn.enilu.flash.api.controller.system;

import cn.enilu.flash.api.controller.BaseController;
import cn.enilu.flash.bean.constant.factory.PageFactory;
import cn.enilu.flash.bean.core.BussinessLog;
import cn.enilu.flash.bean.entity.system.LoginLog;
import cn.enilu.flash.bean.enumeration.Permission;
import cn.enilu.flash.bean.vo.front.Rets;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.service.system.LoginLogService;
import cn.enilu.flash.utils.BeanUtil;
import cn.enilu.flash.utils.DateUtil;
import cn.enilu.flash.utils.factory.Page;
import cn.enilu.flash.warpper.LogWrapper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 登录日志controller
 *
 * @author enilu
 * @version 2018/10/5 0005
 */
@RestController
@RequestMapping("/loginLog")
public class LoginLogController extends BaseController {

    private final LoginLogService loginlogService;

    public LoginLogController(LoginLogService loginlogService) {
        this.loginlogService = loginlogService;
    }

    @GetMapping(value = "/list")
    @RequiresPermissions(value = {Permission.LOGIN_LOG})
    public Object list(@RequestParam(required = false) String beginTime,
                       @RequestParam(required = false) String endTime,
                       @RequestParam(required = false) String logName) {
        Page<LoginLog> page = new PageFactory<LoginLog>().defaultPage();
        page.addFilter("createTime", SearchFilter.Operator.GTE, DateUtil.parseDate(beginTime));
        page.addFilter("createTime", SearchFilter.Operator.LTE, DateUtil.parseDate(endTime));
        page.addFilter("logname", SearchFilter.Operator.LIKE, logName);
        Page pageResult = loginlogService.queryPage(page);
        pageResult.setRecords((List<LoginLog>) new LogWrapper(BeanUtil.objectsToMaps(pageResult.getRecords())).warp());
        return Rets.success(pageResult);

    }


    /**
     * 清空日志
     */
    @DeleteMapping
    @BussinessLog(value = "清空登录日志")
    @RequiresPermissions(value = {Permission.LOGIN_LOG_CLEAR})
    public Object clear() {
        loginlogService.clear();
        return Rets.success();
    }

}
