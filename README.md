# Java Utils Library (Beta.)

Some useful tools for developing faster.

#### Let Spring Boot Faster
Speed up boot time, use this only dev profile. 

spring config xml:
```xml
 <beans profile="dev">
        <bean class="com.yilnz.util.spring.bootfaster.SpringBootFaster" />
        <bean class="com.yilnz.util.spring.bootfaster.SpringBootConstructerFaster"/>
    </beans>
```

run with jvm args:  -noverify -XX:TieredStopAtLevel=1 -Dspring.jmx.enabled=false -Dspring.profiles.active=dev

the basic theory speed up spring is lazy mode and remove slowly services. 

#### Spring Boot Monitor

```xml
     <beans profile="dev">
        <bean class="com.yilnz.util.spring.bootmonitor.BootMonitorConfiguration" />
        <bean class="com.yilnz.util.spring.quickconf.ThymeleafConfiguration" />
     </beans>
```

open the url: http://localhost:port/utils/monitor to view services boot time,
find out max boot time service and add to removedClass property of SpringBootFaster class.

```xml
<bean class="com.yilnz.util.spring.bootfaster.SpringBootFaster">
            <property name="removedClass">
                <list>
                    <value>com.company.service.bootVerySlowlyService</value>
                </list>
            </property>
        </bean>
```