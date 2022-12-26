package com.yilnz.bluesteel.tests;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = MainConf.class)
@SpringBootTest
@EnableAutoConfiguration
@Slf4j
public class MainTests {

    @Test
    public void test1(){

    }
}
