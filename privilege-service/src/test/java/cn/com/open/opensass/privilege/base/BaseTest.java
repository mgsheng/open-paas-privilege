package cn.com.open.opensass.privilege.base;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/*.xml", "file:src/main/webapp/WEB-INF/soc-servlet.xml"})
public class BaseTest  {

    protected Logger log = LoggerFactory.getLogger(this.getClass());



}
