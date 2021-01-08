package com.yilnz.bluesteel.spring.quickconf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ImportResource;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import javax.annotation.PostConstruct;

@ImportResource(locations = "classpath*:spring-thymeleaf.xml")
public class ThymeleafConfiguration {
    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;
    private static final Logger logger = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    @PostConstruct
    public void init(){
        logger.info("thymeleaf inited " + thymeleafViewResolver);
    }

   /*@Bean
    public SpringResourceTemplateResolver springResourceTemplateResolver(){
        final SpringResourceTemplateResolver springResourceTemplateResolver = new SpringResourceTemplateResolver();
        springResourceTemplateResolver.setCacheable(false);
        springResourceTemplateResolver.setPrefix("classpath:/templates/");
        springResourceTemplateResolver.setSuffix(".html");
        springResourceTemplateResolver.setTemplateMode("HTML5");
        return springResourceTemplateResolver;
    }

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver(){
        final InternalResourceViewResolver internalResourceViewResolver = new InternalResourceViewResolver();
        internalResourceViewResolver.setViewClass(JstlView.class);
        internalResourceViewResolver.setPrefix("classpath:/templates/");
        internalResourceViewResolver.setSuffix(".jsp");
        internalResourceViewResolver.setOrder(2);
        internalResourceViewResolver.setViewNames("*jsp");
        return internalResourceViewResolver;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine(SpringResourceTemplateResolver springResourceTemplateResolver){
        final SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(springResourceTemplateResolver);
        return springTemplateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(ITemplateEngine templateEngine){
        final ThymeleafViewResolver thymeleafViewResolver = new ThymeleafViewResolver();
        thymeleafViewResolver.setTemplateEngine(templateEngine);
        thymeleafViewResolver.setCache(false);
        return thymeleafViewResolver;
    }*/
}
