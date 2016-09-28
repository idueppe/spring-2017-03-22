package io.crowdcode.speedbay.auction.config;

import io.crowdcode.speedbay.auction.service.MessageServiceBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static io.crowdcode.speedbay.common.AnsiColor.green;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Ingo Düppe (Crowdcode)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Component
@ContextConfiguration(classes=MessageServiceAnnotationConfiguration.class)
public class MessageServiceAnnotationConfigurationTest {

    @Autowired(required = false)
    private MessageServiceBean messageServiceBean;

    @Autowired
    private ApplicationContext applicationContext;


    @Test
    public void testIfBeanExists() throws Exception {
        assertThat(messageServiceBean, is(notNullValue()));
//        assertNotNull(messageServiceBean);
    }

    @Test
    public void testLogFoundBeans() throws Exception {
        String[] names = applicationContext.getBeanDefinitionNames();
        Arrays.stream(names).forEach((n)-> System.out.println(green(">"+n)));
    }
}