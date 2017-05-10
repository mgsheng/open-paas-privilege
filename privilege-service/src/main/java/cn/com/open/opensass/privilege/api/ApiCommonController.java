package cn.com.open.opensass.privilege.api;

import cn.com.open.opensass.privilege.service.PrivilegeUserService;
import cn.com.open.opensass.privilege.tools.MonitorInfo;
import cn.com.open.opensass.privilege.tools.MonitorTools;
import cn.com.open.opensass.privilege.tools.NetworkUtil;
import cn.com.open.opensass.privilege.tools.WebUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 项目名称 : 健康检查页面.
 * 创建日期 : 2017/5/8.
 * 创建时间 : 10:00.
 */
@Controller
public class ApiCommonController {

    @Autowired
    private PrivilegeUserService privilegeUserService;
    /**
     * 获取服务器状态
     * @param request
     * @param response
     */
    @RequestMapping("common/status")
    public void status(HttpServletRequest request, HttpServletResponse response) {
        String ip = NetworkUtil.getIpAddress(request);
        System.out.println(ip);
        Map<String,Object> map = new LinkedHashMap<String, Object>();
        //运行状态
        map.put("running", true);
        //连接数据库状态
        int val = 0;
        try {
            val = privilegeUserService.connectionTest();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if(val==1){
            map.put("database", true);
        }
        else{
            map.put("database", false);
        }
        //系统状态
        try {
            MonitorInfo monitorInfo = MonitorTools.getMonitorInfo();
            map.put("cpuratio", monitorInfo.getCpuRatio());
            map.put("freememory", monitorInfo.getFreePhysicalMemorySize());
            map.put("totalmemory", monitorInfo.getTotalMemorySize());
            map.put("freespace", monitorInfo.getUsableSpace());
            map.put("totalspace", monitorInfo.getTotalSpace());
        } catch (Exception e) {
            //	e.printStackTrace();
        }
        WebUtils.writeJson(response, JSONObject.fromObject(map));
    }
}
