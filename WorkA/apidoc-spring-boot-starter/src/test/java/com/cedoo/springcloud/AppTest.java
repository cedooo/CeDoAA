package com.cedoo.springcloud;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("dev")
@ContextConfiguration(classes = {com.cedoo.springcloud.SwaggerAutoConfiguration.class})
public class AppTest 
{
    //TODO 测试默认配置是否成功加载
    @Autowired
    private Docket docket;

    @Test
    public void t(){
        assert "默认分组".equals(docket.getGroupName());
    }


    //TODO 测试swagger2界面能否成功加载： http状态是否是200


}
