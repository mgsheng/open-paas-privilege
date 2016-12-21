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
		PrivilegeAjaxMessage message=service.getRedisUserRole("appcbfb25e6c0d611e6a6df0050568c069m", "0bb6f4bfc0d711e6a6df0050568c069a");
		System.err.println(message.getMessage());
	}
	@Test
	public void test2(){
		ApplicationContext context=new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
		PrivilegeUserRedisService service=(PrivilegeUserRedisService) context.getBean("privilegeUserRedisService");
		PrivilegeAjaxMessage message=service.getRedisUserMenu("a84836b1c5cc11e6a6df0050568c069a", "bd475bf1c5cc11e6a6df0050568c069a");
		System.err.println(message.getMessage());
	}
}
