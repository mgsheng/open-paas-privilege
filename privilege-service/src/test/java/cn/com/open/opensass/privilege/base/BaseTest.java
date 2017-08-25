package cn.com.open.opensass.privilege.base;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/*.xml","file:src/main/webapp/WEB-INF/soc-servlet.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
public class BaseTest implements ApplicationContextAware {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    protected ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext applicationContext)  {
        this.applicationContext = applicationContext;
    }

}
