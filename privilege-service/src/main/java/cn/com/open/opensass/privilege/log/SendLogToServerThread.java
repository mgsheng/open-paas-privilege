package cn.com.open.opensass.privilege.log;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class SendLogToServerThread implements Runnable {

    private MultiValueMap<String, Object> logMap;
    private PrivilegeServiceDev privilegeServiceDev;

    public SendLogToServerThread(MultiValueMap<String, Object> logMap, PrivilegeServiceDev privilegeServiceDev) {
        this.logMap = logMap;
        this.privilegeServiceDev = privilegeServiceDev;
    }

    @Override
    public void run() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
            headers.setContentType(type);
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(logMap, headers);
            ResponseEntity<String> result = restTemplate.exchange(privilegeServiceDev.getKong_log_url(), HttpMethod.POST, entity, String.class);
            System.out.println(result.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
