package cn.com.open.opensass.privilege.log;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class PrivilegeServiceLogUtil {

    public static void log(PrivilegeServiceLog log, PrivilegeServiceDev privilegeServiceDev) {
        MultiValueMap<String, Object> logMap = new LinkedMultiValueMap<>();
        logMap.add("tag", "privilege");
        logMap.add("logData", JSONObject.toJSONString(log));
        try {
            Thread thread = new Thread(new SendLogToServerThread(logMap, privilegeServiceDev));
            thread.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
