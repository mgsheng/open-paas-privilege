package cn.com.open.opensass.privilege.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import cn.com.open.opensass.privilege.dev.PrivilegeServiceDev;

/**
 * 项目启动执行任务
 * @author dongminghao
 *
 */
public class InitJob implements ApplicationListener<ContextRefreshedEvent> {
	
	private static final Logger log = Logger.getLogger(InitJob.class);
	@Autowired
	private PrivilegeServiceDev userManagerDev;
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {/*
		System.out.println("onApplicationEvent:~~~~~~~~~~~~~~~~~~~~~~~~~"+event.getApplicationContext().getDisplayName());
		if(event.getApplicationContext().getDisplayName().equals("Root WebApplicationContext")){
         log.info("~~~~~~~~~~~~~~~Kafka message service start~~~~~~~~~~~~~~~~");
		Thread thread = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
		thread.run();
		   log.info("~~~~~~~~~~~~~~~Kafka message service start2~~~~~~~~~~~~~~~~");
			Thread thread2 = new Thread( new KafkaConsumer(userAccountBalanceService,userManagerDev));
			thread2.run();
		}
	*/}
}
