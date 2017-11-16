package cn.com.open.opensass.privilege.base;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath*:/spring/*.xml", "file:src/main/webapp/WEB-INF/soc-servlet.xml"})
@Transactional
public class BaseTest  {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    public void demo() {
        Assert.assertEquals("1", "1");
    }

}
