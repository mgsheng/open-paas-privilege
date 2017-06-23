package cn.com.open.opensass.privilege.log;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;
import cn.com.open.opensass.privilege.tools.HttpTools;

import java.util.Map;

public class SendLogToServerThread implements Runnable {

    private Map<String, String> logMap;
    private PrivilegeServiceDev privilegeServiceDev;

    public SendLogToServerThread(Map<String, String> logMap, PrivilegeServiceDev privilegeServiceDev) {
        this.logMap = logMap;
        this.privilegeServiceDev = privilegeServiceDev;
    }

    @Override
    public void run() {
        try {
            String result = HttpTools.doPost(privilegeServiceDev.getKong_log_url(), logMap, "UTF-8");
            System.out.printf(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
