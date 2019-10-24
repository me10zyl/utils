package com.yilnz.util.spring.controller;

import com.yilnz.util.spring.bootmonitor.SpringBootMonitor;
import com.yilnz.util.spring.controller.entity.Jar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

@Controller
@RequestMapping("/utils")
public class MonitorController {

    @Autowired
    private SpringBootMonitor springBootMonitor;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/monitor")
    public String monitor(Model model) {
        final String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        Map<String, Long> maps = new TreeMap<>();
        for (String beanDefinitionName : beanDefinitionNames) {
            maps.put(beanDefinitionName, springBootMonitor.getInitializationTime(beanDefinitionName));
        }

        List<Map.Entry<String, Long>> list = new ArrayList<Map.Entry<String, Long>>(maps.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
            @Override
            public int compare(Map.Entry<String, Long> o1, Map.Entry<String, Long> o2) {
                return - o1.getValue().compareTo(o2.getValue());
            }
        });
        model.addAttribute("beans", list);
        return "monitor/index";
    }

    @GetMapping("/jars")
    public String jars(Model model) {
        final URL[] urLs = ((URLClassLoader) MonitorController.class.getClassLoader()).getURLs();
        List<Jar> jarList = new ArrayList<>();
        for (URL urL : urLs) {
            final Jar jar = new Jar();
            jar.setUrl(urL.toString());
            if(jarList.stream().anyMatch(e->e.getUrl().equals(urL.toString()))){
                jar.setRepeat(true);
            }
            jarList.add(jar);
        }
        model.addAttribute("beans", jarList);
        return "monitor/jars";
    }
}
