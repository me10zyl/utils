package com.yilnz.bluesteel.arthas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

public class ArthasInbound {

    private static final Logger logger = LoggerFactory.getLogger(ArthasInbound.class);

    public static void startArthas(){
        final URL[] urLs = ((URLClassLoader) ArthasInbound.class.getClassLoader()).getURLs();
        String f = null;
        for (URL urL : urLs) {
            if(urL.toString().contains("arthas-boot")){
                f = urL.getFile();
            }
        }

        String finalF = f;
        if (finalF != null) {
            new Thread(()->{
                try {
                    logger.info("arthas inbound started at http://127.0.0.1:9999");
                    final Runtime runtime = Runtime.getRuntime();
                    System.out.println(finalF);
                    final Process exec = runtime.exec("java -jar " + finalF + " com.taobao.arthas.boot.Bootstrap", new String[]{String.valueOf(getProcessId()), "--http-port=9999", "--telnet-port=65523"});
                    final InputStream errorStream = exec.getErrorStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(errorStream));
                    String str = null;
                    while ((str = br.readLine()) != null) {
                        System.out.println(str);
                    }
                    br.close();
                    exec.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public static int getProcessId() {
        int pid = 0;
        try {
            java.lang.management.RuntimeMXBean runtime =
                    java.lang.management.ManagementFactory.getRuntimeMXBean();
            java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            sun.management.VMManagement mgmt =
                    (sun.management.VMManagement) jvm.get(runtime);
            java.lang.reflect.Method pid_method =
                    mgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);

            pid = (Integer) pid_method.invoke(mgmt);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return pid;
    }
}
