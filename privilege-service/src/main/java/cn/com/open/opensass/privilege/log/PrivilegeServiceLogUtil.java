package cn.com.open.opensass.privilege.log;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrivilegeServiceLogUtil {

    public static void log(PrivilegeServiceLog log, PrivilegeServiceDev privilegeServiceDev) {
        Map<String, String> logMap = new HashMap<String, String>();
        logMap.put("tag", "privilege");
        logMap.put("logData", JSONObject.toJSONString(log));
        try {
            Thread thread = new Thread(new SendLogToServerThread(logMap, privilegeServiceDev));
            thread.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
