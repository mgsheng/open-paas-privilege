package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.service.PrivilegeUrlService;
import cn.com.open.opensass.privilege.tools.BaseControllerUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;
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
 * redis url 接口
 * 获取数据：getUrl?appId=&appUserId=
 * 修改数据：updateUrl?appId=&appUserId=
 * 删除数据：delUrl?appId=&appUserId=
 * 判断url是否存在：existUrl?appId=&appUserId=&urladdr=
 * 判断key是否在redis中存在 ：existKey?appId=&appUserId=
 */
@Controller
@RequestMapping("/url/")
public class UrlRedisPrivilegeController extends BaseControllerUtil {
    private static final Logger log = LoggerFactory.getLogger(UrlRedisPrivilegeController.class);

    @Autowired
    private PrivilegeUrlService privilegeUrlService;


    /**
     * 新增redis缓存
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "getUrl", method = RequestMethod.POST)
    public void putdata(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId = request.getParameter("appUserId").trim();
        String appId = request.getParameter("appId").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get getDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appUserId, appId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        PrivilegeAjaxMessage ajaxMessage = privilegeUrlService.getRedisUrl(appId, appUserId);
        if (ajaxMessage.getCode().equals("1")) {
            WebUtils.writeJson(response, ajaxMessage.getMessage());
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }

    /**
     * 更新jedis
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "updateUrl", method = RequestMethod.POST)
    public void updateData(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId = request.getParameter("appUserId").trim();
        String appId = request.getParameter("appId").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        String data = "";
        log.info("===================get updateDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appUserId, appId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        PrivilegeAjaxMessage ajaxMessage = privilegeUrlService.updateRedisUrl(appId, appUserId);
        if (ajaxMessage.getCode().equals("1")) {
            WebUtils.writeJson(response, ajaxMessage.getMessage());
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }

    /**
     * 根据key删除jedis
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "delUrl", method = RequestMethod.POST)
    public void deleteData(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId = request.getParameter("appUserId").trim();
        String appId = request.getParameter("appId").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get deleteDataPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appId, appUserId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }

        PrivilegeAjaxMessage ajaxMessage = privilegeUrlService.deleteRedisUrl(appId, appUserId);
        if (ajaxMessage.getCode().equals("1")) {
            WebUtils.writeJson(response, ajaxMessage.getMessage());
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }


    /**
     * 判断url是否存在
     *
     * @param request
     * @param response
     */

    @RequestMapping(value = "existUrl", method = RequestMethod.POST)
    public void existUrlData(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId = request.getParameter("appUserId").trim();
        String appId = request.getParameter("appId").trim();
        String durl = request.getParameter("urladdr").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get existUrlPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appUserId, appId, durl))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        PrivilegeAjaxMessage ajaxMessage = privilegeUrlService.existRedisUrl(appId, appUserId, durl);
        if (ajaxMessage.getCode().equals("1")) {
            WebUtils.writeJson(response, ajaxMessage.getMessage());
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }

    /**
     * 判断key是否存在
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "existKey", method = RequestMethod.POST)
    public void existkeyData(HttpServletRequest request, HttpServletResponse response) {
        /*参数接收*/
        String appUserId = request.getParameter("appUserId").trim();
        String appId = request.getParameter("appId").trim();
        Map<Object, Object> map = new HashMap<Object, Object>();
        log.info("===================get existKeyPrivilege start======================");
        if (!paraMandatoryCheck(Arrays.asList(appUserId, appId))) {
            paraMandaChkAndReturn(10000, response, "必传参数中有空值");
            return;
        }
        PrivilegeAjaxMessage ajaxMessage = privilegeUrlService.existRedisKey(appId, appUserId);
        if (ajaxMessage.getCode().equals("1")) {
            WebUtils.writeJson(response, ajaxMessage.getMessage());
        } else {
            map.put("status", ajaxMessage.getCode());
            map.put("error_code", ajaxMessage.getMessage());/*数据不存在*/
            writeErrorJson(response, map);
        }
        return;
    }
}
