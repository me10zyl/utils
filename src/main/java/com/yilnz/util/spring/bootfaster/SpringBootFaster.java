package com.yilnz.util.spring.bootfaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.List;

/**
 * How to use:
 * JVM参数设置 -noverify -XX:TieredStopAtLevel=1 -Dspring.jmx.enabled=false -Dspring.profiles.active=dev
 *  <beans profile="dev">
 *         <bean class="com.yilnz.util.spring.bootfaster.SpringBootFaster" />
 *  </beans>
 */
public class SpringBootFaster implements BeanFactoryPostProcessor {

    private static final Logger logger = LoggerFactory.getLogger(SpringBootFaster.class);

    private List<String> removedClass;
    private List<String> ignoreClass;

    public void setIgnoreClass(List<String> ignoreClass) {
        this.ignoreClass = ignoreClass;
    }

    public void setRemovedClass(List<String> removedClass) {
        this.removedClass = removedClass;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            boolean ignored = false;
            if(ignoreClass != null){
                for (String aClass : ignoreClass) {
                    if (beanDefinition.getBeanClassName() != null && beanDefinition.getBeanClassName().contains(aClass)) {
                        ignored = true;
                        break;
                    }
                }
            }
            if(ignored){
                continue;
            }
            beanDefinition.setLazyInit(true);
            if (removedClass != null && removedClass.size() > 0) {
                boolean ignore = false;
                for (String ignoreClass : removedClass) {
                    final String beanClassName = beanDefinition.getBeanClassName();
                    if(beanClassName != null && beanClassName.contains(ignoreClass)){
                        ignore = true;
                        break;
                    }
                }
                if (ignore) {
                   logger.info("[SpringBootFaster]ignore bean " + beanName);
                    ((DefaultListableBeanFactory) beanFactory).removeBeanDefinition(beanName);
                }
            }
        }
    }
}
