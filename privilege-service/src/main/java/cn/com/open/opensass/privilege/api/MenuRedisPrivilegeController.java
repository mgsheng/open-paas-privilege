package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.service.PrivilegeMenuService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jh on 2016/12/19.
 */
/**
 * redis menu 接口
 * 获取数据：getMenu?appId=&appUserId=
 */
@Controller
@RequestMapping("/menu/")
public class MenuRedisPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(MenuRedisPrivilegeController.class);
    @Autowired
    private PrivilegeMenuService privilegeMenuService;
    /**
     * 新增redis缓存
     * @param request
     * @param response
     */
    @RequestMapping(value = "getMenu", method = RequestMethod.POST)
    public void putdata(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId=request.getParameter("appUserId").trim();
        String appId=request.getParameter("appId").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appUserId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }

        PrivilegeAjaxMessage ajaxMessage =privilegeMenuService.getMenuRedis(appId,appUserId);
        if (ajaxMessage.getCode().equals("1")) {
            map.put("menuList",ajaxMessage.getMessage());
            WebUtils.writeJson(response, JSONObject.fromObject(map));
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }
}
