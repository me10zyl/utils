package com.yilnz.bluesteel.springbootplus.controller;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.yilnz.bluesteel.springbootplus.entity.ApiResult;
import com.yilnz.bluesteel.springbootplus.utils.EnumUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enums")
@Api(tags = "获取枚举")
public class EnumsController {

    @ApiOperation("获取枚举")
    @GetMapping("/getEnums")
    public ApiResult<?> appTypeEnums(@RequestParam String enumName) throws ClassNotFoundException {
        /*Reflections reflections = new Reflections("com.tj.xlive.live.enums");
        Set<Class<? extends IEnum>> enums = reflections.getSubTypesOf(IEnum.class);
        Optional<Class<? extends IEnum>> first = enums.stream().filter(e -> e.getSimpleName().equals(enumName)).findFirst();
        if(!first.isPresent()){
            return ApiResult.fail("没有找到这个枚举：" + enumName);
        }*/
        Class<? extends IEnum> aClass = (Class<? extends IEnum>) Class.forName(enumName);
        return ApiResult.ok(EnumUtils.serializeEnum((Class<? extends Enum>) aClass), "成功");
    }
}
