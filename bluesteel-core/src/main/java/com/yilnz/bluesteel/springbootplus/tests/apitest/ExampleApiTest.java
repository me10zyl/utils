package com.yilnz.bluesteel.springbootplus.tests.apitest;

import com.yilnz.bluesteel.springbootplus.tests.apitest.base.ApiTest;
import com.yilnz.bluesteel.springbootplus.tests.apitest.base.DefaultApiTest;
import org.junit.Test;

@ApiTest(controllerUrl = "/admin/businessType", entityClass = ExampleEntity.class, baseUrl = "http://localhost:51000", dbName = "exampleDB")
public class ExampleApiTest extends DefaultApiTest {

    @Override
    @Test
    public void testAdd() throws Exception {
        super.testAdd();
    }

    @Override
    @Test
    public void testGet() {
        super.testGet();
    }

    @Override
    @Test
    public void testList() {
        super.testList();
    }

    @Override
    @Test
    public void testDelete() {
        super.testDelete();
    }
}
