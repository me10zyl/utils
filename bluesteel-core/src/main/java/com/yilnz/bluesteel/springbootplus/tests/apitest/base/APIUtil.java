package com.yilnz.bluesteel.springbootplus.tests.apitest.base;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.baomidou.mybatisplus.core.enums.IEnum;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.google.common.collect.Maps;
import com.yilnz.surfing.core.SurfHttpRequest;
import com.yilnz.surfing.core.SurfHttpRequestBuilder;
import com.yilnz.surfing.core.SurfSpider;
import com.yilnz.surfing.core.basic.Header;
import com.yilnz.surfing.core.basic.Html;
import com.yilnz.surfing.core.basic.Page;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * apiutil
 *
 * @author zyl
 * @date 2021/02/26
 * @return
 */
@Slf4j
public class APIUtil {

    public static Page get(String url) {
        Page page = SurfSpider.get(url);
        Html html = page.getHtml();
        System.out.println(page.getStatusCode() + " : " + html.toString());
        return page;
    }

    public static String toParams(Object pojo){
        Method[] methods = pojo.getClass().getDeclaredMethods();
        List<String> field = new ArrayList<>();
        for (Method method : methods) {
            try {
                field.add(StringUtils.firstCharToLower(method.getName().replace("get", "")) + "=" + method.invoke(pojo, null));
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return field.stream().collect(Collectors.joining("&"));
    }

    @Data
    public static class TokenAndCookie{
        private String token;
        private String cookie;
    }

    public static TokenAndCookie getToken(String baseUrl) {
        String loginUrl = baseUrl + "/login";
        SurfHttpRequestBuilder surf = SurfHttpRequestBuilder.create(loginUrl, "POST");
        SurfHttpRequest build = surf.build();
        build.addHeader("Content-Type", "application/json;charset=UTF-8");
        build.setBody("{\"username\":\"admin\",\"password\":\"123456\"}");
        Page page = SurfSpider.create().addRequest(build).request().get(0);
        //log.info("登录结果：{}. {}",page.getStatusCode(), page.getHtml());
        if (page.getStatusCode() == 200) {
            String token = page.getHtml().selectJson("$.data.token").get();
            List<Header> cookies = page.getHeaders("Set-Cookie");
            StringBuilder sb = new StringBuilder();
            for (Header cookie : cookies) {
                sb.append(cookie.getValue().split(";")[0] + ";");
            }
            TokenAndCookie tokenAndCookie = new TokenAndCookie();
            tokenAndCookie.setToken(token);
            tokenAndCookie.setCookie(sb.toString());
            return tokenAndCookie;
        }

        System.err.println("登录失败：loginUrl:" + loginUrl + ",status=" + page.getStatusCode() + ",body=" + page.getHtml());

        throw new RuntimeException("登录失败");
    }

 /*   public static TokenAndCookie getToken(String username, String password) {
        UserDto.LoginDto loginDto = new UserDto.LoginDto();
        loginDto.setLoginName(username);
        loginDto.setPassword(password);
        loginDto.setRememberMe(false);
        Map<String, String> headers = Maps.newHashMap();
        headers.put("Content-Type", "application/json;charset=utf-8");
        Page page = req(BASE_URL + "/login", JSONObject.toJSONString(loginDto), headers, "POST", true);
        Html html = page.getHtml();
        Assert.assertEquals(200, page.getStatusCode());
        Assert.assertEquals(html.selectJson("$.status").get(), "true");
        JSONObject jsonObject = JSONObject.parseObject(html.selectJson("$.data").get());
        String token =  jsonObject.getString("X-Auth-Token");
        List<Header> cookies = page.getHeaders("Set-Cookie");
        StringBuilder sb = new StringBuilder();
        for (Header cookie : cookies) {
            sb.append(cookie.getValue().split(";")[0] + ";");
        }
        TokenAndCookie tokenAndCookie = new TokenAndCookie();
        tokenAndCookie.setToken(token);
        tokenAndCookie.setCookie(sb.toString());
        return tokenAndCookie;
    }*/

    private static Page req(String url, String body, Map<String, String> headers, String method, boolean check) {
        SurfHttpRequestBuilder builder = new SurfHttpRequestBuilder(url, "POST");
        SurfHttpRequest req = builder.build();
        req.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        if (headers != null) {
            for (Map.Entry<String, String> en : headers.entrySet()) {
                req.addHeader(en.getKey(), en.getValue());
            }
        }
        req.setMethod(method);
        req.setBody(body);
        Page page = SurfSpider.create().addRequest(req).request().get(0);
        Html html = page.getHtml();
        System.out.println(method + " : " + page.getStatusCode() + " : " + html.toString());
        if(check && page.getHeader("Content-Type").contains("json")) {
            Assert.assertEquals(page.getStatusCode(), 200);
            Assert.assertEquals(html.selectJson("$.success").get(), "true");
        }
        //log.info("REQ:url[{}],result：\n{}", url, page.getHtml());
        return page;
    }

    public static File download(String url, String method, Object body, String fileDir, TokenAndCookie token) throws ExecutionException, InterruptedException, IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient build = httpClientBuilder.build();
        try {
            HttpUriRequest request = null;
            if(method.equalsIgnoreCase("POST")) {
                request = new HttpPost(url);
                ((HttpPost)request).setEntity(new StringEntity(toJson(body)));
            }else if(method.equalsIgnoreCase("GET")){
                request = new HttpGet(url);
            }
            request.addHeader("Cookie", token.getCookie());
            request.addHeader("token", token.getToken());
            request.addHeader("Content-Type", "application/json;charset=utf-8");
            CloseableHttpResponse response = build.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            assert statusCode == 200;
            org.apache.http.Header firstHeader = response.getFirstHeader("Content-Disposition");
            Matcher matcher = Pattern.compile("fileName=(.+)").matcher(firstHeader.getValue());
            matcher.find();
            String fileName = matcher.group(1);
            fileName = URLDecoder.decode(fileName, "UTF-8");
            InputStream content = response.getEntity().getContent();
            Path target = Paths.get(fileDir, fileName);
            Files.copy(content, target, StandardCopyOption.REPLACE_EXISTING);
            return target.toFile();
        }finally {
            build.close();
        }
    }

    public static Page postJson(String url, Object pojo, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("Cookie", token.getCookie());
        map.put("token", token.getToken());
        map.put("Content-Type", "application/json;charset=utf-8");
        String body;
        if(pojo instanceof String){
            body = (String) pojo;
        }else{
            body = JSON.toJSONString(pojo);
        }
        return req(url, body, map, "POST", true);
    }

    public static Page postJsonNoCheck(String url, Object pojo, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        map.put("Content-Type", "application/json;charset=utf-8");
        return req(url, JSON.toJSONString(pojo), map, "POST", false);
    }

    public static Page post(String url, String body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        return req(url, body, map, "POST", true);
    }

    public static Page postNoCheck(String url, String body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        return req(url, body, map, "POST", false);
    }


    public static Page get(String url, String body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        return req(url, body, map, "GET", true);
    }

    public static Page getNoCheck(String url, String body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        return req(url, body, map, "GET", false);
    }

    public static Page putJson(String url, Object body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        map.put("Content-Type", "application/json;charset=utf-8");
        return req(url, JSON.toJSONString(body), map, "PUT", true);
    }

    public static Page delete(String url, String body, TokenAndCookie token) {
        HashMap<String, String> map = Maps.newHashMap();
        map.put("token", token.getToken());
        map.put("Cookie", token.getCookie());
        return req(url, body, map, "DELETE", true);
    }

    public static String toJson(Object pojo){
        if(pojo instanceof String){
            return (String) pojo;
        }
        SerializeConfig serializeConfig = new SerializeConfig();
        Field[] declaredFields = pojo.getClass().getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if(IEnum.class.isAssignableFrom(declaredField.getType())){
                serializeConfig.put(declaredField.getType(), new EnumSerializer());
            }
        }
        return JSON.toJSONString(pojo, serializeConfig);
    }

    private static class EnumSerializer implements ObjectSerializer{

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
            if(object instanceof IEnum){
                Serializable value = ((IEnum) object).getValue();
                serializer.out.writeInt((Integer) value);
            }
        }
    }
}
