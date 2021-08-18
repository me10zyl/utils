package com.yilnz.bluesteel.springbootplus.tests.apitest.base;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yilnz.bluesteel.reflection.ReflectionUtil;
import com.yilnz.bluesteel.springbootplus.tests.mockito.MockUtil;
import com.yilnz.bluesteel.string.StringUtil;
import com.yilnz.surfing.core.basic.Page;
import com.yilnz.surfing.core.selectors.Selectable;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yilnz.bluesteel.springbootplus.tests.apitest.base.APIUtil.get;
import static com.yilnz.bluesteel.springbootplus.tests.apitest.base.APIUtil.getToken;
import static com.yilnz.bluesteel.springbootplus.tests.apitest.base.APIUtil.post;
import static com.yilnz.bluesteel.springbootplus.tests.apitest.base.APIUtil.postJson;

/**
 * 默认的api测试
 *
 * @author zyl
 * @date 2021/06/07
 * @return
 */
@Slf4j
public class DefaultApiTest {
    private void assertPageTrue(Page page){
        Assert.assertEquals(page.getHtml().selectJson("$.status").get(), "true");
    }

    public void postCreate(Object mocked){

    }

    public final Connection getConnection(String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.4.251:3306/"+dbName+"?useUnicode=true&characterEncoding=UTF-8&useSSL=false", "root", "root@db.251");
        return conn;
    }

    public void testAdd() throws Exception {
        ApiTest apiTest = this.getClass().getAnnotation(ApiTest.class);
        String baseURL = apiTest.baseUrl();
        Class<?> type = apiTest.entityClass();
        Object mocked = MockUtil.mock(type, "zyl", DateUtil.format(new Date(), "MM-dd HH:mm:ss"));
        Connection connection = getConnection(apiTest.dbName());
        try {
            TableName tableName = type.getAnnotation(TableName.class);
            Map<String, Map<String, String>> maps = new HashMap<>();
            ResultSet rst = connection.getMetaData().getColumns(null, apiTest.dbName(), tableName.value(), "%");
            while (rst.next()) {
                Map<String, String> map = new HashMap<String, String>();
                int columnCount = rst.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String string = rst.getString(i);
                    map.put(rst.getMetaData().getColumnName(i), string);
                    //log.info("columnInfo:" + rst.getMetaData().getColumnName(i)  + ":" + string);
                }
                maps.put(map.get("COLUMN_NAME"), map);
            }
            List<Method> allSetterMethods = ReflectionUtil.getAllSetterMethods(type);
            for (Method setterMethod : allSetterMethods) {
                String javaFieldName = StringUtil.toLowerCaseLetter1(setterMethod.getName().substring(3));
                Map<String, String> prop = maps.get(StringUtils.camelToUnderline(javaFieldName));
                if(prop == null){
                    continue;
                }
                String columnSize = prop.get("COLUMN_SIZE");
                if (setterMethod.getParameterTypes()[0].equals(String.class)) {
                    String ran = MockUtil.randomString() + "|" + DateUtil.format(new Date(), "MM-dd HH:mm:ss") + "|byzyltest";
                    int colSize = Integer.parseInt(columnSize);
                    String substring = ran.length() > colSize ? ran.substring(0, colSize) : ran;
                    setterMethod.invoke(mocked, substring);
                }
                if (prop.get("TYPE_NAME").equals("TINYINT")) {
                    try {
                        setterMethod.invoke(mocked, 1);
                    }catch (Exception e){

                    }
                }
            }
            postCreate(mocked);
            String url = baseURL + apiTest.controllerUrl() + "/add";
            postJson(url, mocked, getToken(baseURL));
            log.info("插入数据：{}", JSONObject.toJSONString(mocked));
            String sql1 = "select MAX(id) from " + tableName.value();
            ResultSet resultSet1 = connection.prepareStatement(sql1).executeQuery();
            resultSet1.next();
            long lastId = resultSet1.getLong(1);
            log.info("数据库查询到最大ID {}：{}" ,sql1, lastId);
            String sql = "select * from " + tableName.value() + " where id = " + lastId;
            log.info("数据库结果 {}：", sql);
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                int columnCount = resultSet.getMetaData().getColumnCount();
                StringBuilder header = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    header.append(columnName + " ");
                }
                StringBuilder row = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    String val = resultSet.getString(i);
                    row.append(val + " ");
                }
                log.info(header.toString());
                log.info(row.toString());
            }else{
                throw new RuntimeException("插入数据失败");
            }
        }finally {
            connection.close();
        }
    }

    public void testGet(){
        ApiTest apiTest = this.getClass().getAnnotation(ApiTest.class);
        String baseURL = apiTest.baseUrl();
        Page page = postJson(baseURL + apiTest.controllerUrl() + "/getPageList", "{\"pageIndex\":1,\"pageSize\":20}", getToken(baseURL));
        long id = Long.parseLong(page.getHtml().selectJson("$.data.records").nodes().get(0).selectJson("id").get());
        Page page2 = get(baseURL + apiTest.controllerUrl() + "/info/" + id, null, getToken(baseURL));
        String id2 = page2.getHtml().selectJson("$.data.id").get();
        Assert.assertEquals(id + "", id2);
    }

    public void testList(){
        ApiTest apiTest = this.getClass().getAnnotation(ApiTest.class);
        String baseURL = apiTest.baseUrl();
        Page page = postJson(baseURL + apiTest.controllerUrl() + "/getPageList", "{\"pageIndex\":1,\"pageSize\":20}", getToken(baseURL));
        Page page2 = postJson(baseURL + apiTest.controllerUrl() + "/getPageList", "{\"pageIndex\":2,\"pageSize\":20}", getToken(baseURL));
        long id = Long.parseLong(page.getHtml().selectJson("$.data.records").nodes().get(0).selectJson("id").get());
        List<Selectable> nodes = page2.getHtml().selectJson("$.data.records").nodes();
        if(nodes.size() > 0) {
            long id2 = Long.parseLong(nodes.get(0).selectJson("id").get());
            Assert.assertNotEquals(id, id2);
        }
    }

    public void testDelete(){
        ApiTest apiTest = this.getClass().getAnnotation(ApiTest.class);
        String baseURL = apiTest.baseUrl();
        Page page = postJson(baseURL + apiTest.controllerUrl() + "/getPageList", "{\"pageIndex\":1,\"pageSize\":20}", getToken(baseURL));
        long id = Long.parseLong(page.getHtml().selectJson("$.data.records").nodes().get(0).selectJson("id").get());
        post(baseURL + apiTest.controllerUrl() + "/delete/" + id, null, getToken(baseURL));
        Page page2 = postJson(baseURL + apiTest.controllerUrl() + "/getPageList", "{\"pageIndex\":1,\"pageSize\":20}", getToken(baseURL));
        List<Selectable> nodes = page2.getHtml().selectJson("$.data.records").nodes();
        if(nodes.size()>0){
            Assert.assertNotEquals(nodes.get(0).selectJson("id").get(), id + "");
        }
    }
}
