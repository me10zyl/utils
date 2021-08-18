package com.yilnz.bluesteel.springbootplus.utils.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ExcelUtil {

    /**
     * 出口
     *
     * @param response 响应
     * @param dataList 数据列表
     * @param fileName 文件名称
     * @return
     * @author zyl
     * @date 2020/09/16
     */
    public static <T> void export(HttpServletResponse response, List<T> dataList, String fileName) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        response.setContentType("application/binary;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
            if (dataList.size() > 0) {
                XSSFRow row0 = sheet.createRow(0);
                insertRowCells(row0, dataList.get(0), true);
                for (int i = 0; i < dataList.size(); i++) {
                    XSSFRow row = sheet.createRow(i + 1);
                    T data = dataList.get(i);
                    insertRowCells(row, data, false);
                }
                ServletOutputStream outputStream = response.getOutputStream();
                workbook.write(outputStream);
                outputStream.flush();
                outputStream.close();
            }
        } catch (IllegalAccessException | IOException e) {
            log.error("exportEXCEL err", e);
        }
    }

    private static <T> void insertRowCells(XSSFRow row, T data, boolean isTile) throws IllegalAccessException {
        Field[] declaredFields = ReflectUtil.getFields(data.getClass());
        List<Field> sorted = Arrays.stream(declaredFields).filter(e -> e.isAnnotationPresent(Excel.class)).sorted((o1, o2) -> {
            Excel a1 = o1.getAnnotation(Excel.class);
            Excel a2 = o2.getAnnotation(Excel.class);
            return Integer.parseInt(a1.orderNum()) - Integer.parseInt(a2.orderNum());
        }).collect(Collectors.toList());
        int j = 0;
        for (Field declaredField : sorted) {
            declaredField.setAccessible(true);
            Excel annotation = declaredField.getAnnotation(Excel.class);
            XSSFCell cell = row.createCell(j++);
            if (isTile) {
                String name = annotation.name();
                cell.setCellValue(name);
            } else {
                Class<?> type = declaredField.getType();
                Object o = declaredField.get(data);
                if (o != null && type.equals(String.class)) {
                    cell.setCellValue((String) o);
                }
                if (o != null && type.equals(Date.class)) {
                    String format = annotation.format();
                    if (StringUtils.isBlank(format)) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    }
                    cell.setCellValue(new SimpleDateFormat(format).format(o));
                }
                if (o != null && type.equals(Integer.class)) {
                    cell.setCellValue((Integer) o);
                }
                if (o != null && type.equals(Long.class)) {
                    cell.setCellValue((Long) o);
                }
                if (type.equals(Boolean.class)) {
                    cell.setCellValue(o == null ? "否" : ((Boolean) o ? "是" : "否"));
                }
                if (o != null && IEnum.class.isAssignableFrom(type)) {
                    Field name = null;
                    try {
                        name = type.getDeclaredField("name");
                    } catch (NoSuchFieldException fieldException) {
                        log.info("no such field error", fieldException);
                    }
                    name.setAccessible(true);
                    cell.setCellValue((String) name.get(o));
                }
            }
        }
    }
}
