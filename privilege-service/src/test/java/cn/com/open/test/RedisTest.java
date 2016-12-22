package cn.com.open.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.com.open.opensass.privilege.service.PrivilegeUserRedisService;
import cn.com.open.opensass.privilege.vo.PrivilegeAjaxMessage;

public class RedisTest {
	@Test
	public void test(){
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
		PrivilegeUserRedisService service=(PrivilegeUserRedisService) context.getBean("privilegeUserRedisService");
		PrivilegeAjaxMessage message=service.getRedisUserRole("23","10001");
		System.err.println(message.getMessage());
	}
	
}
