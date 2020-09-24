package com.yilnz.bluesteel.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.List;

@Slf4j
public class ExcelUtil {

    public static  <T> void export(HttpServletResponse response, List<T> dataList, String fileName){
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("sheet1");
        response.setContentType("application/binary;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;fileName=" + URLEncoder.encode(fileName+".xlsx", "UTF-8"));
            XSSFRow row0 = sheet.createRow(0);
            insertRowCells(row0, dataList.get(0), true);
            for (int i = 0; i < dataList.size(); i++) {
                XSSFRow row = sheet.createRow(i+1);
                T data = dataList.get(i);
                insertRowCells(row, data, false);
            }
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IllegalAccessException | IOException e) {
            log.error("exportEXCEL err", e);
        }
    }

    private static <T> void insertRowCells(XSSFRow row, T data, boolean isTile) throws IllegalAccessException {
        Field[] declaredFields = data.getClass().getDeclaredFields();
        int j = 0;
        for (int i1 = 0; i1 < declaredFields.length; i1++) {
            Field declaredField = declaredFields[i1];
            declaredField.setAccessible(true);
            if(declaredField.isAnnotationPresent(Excel.class)){
                Excel annotation = declaredField.getAnnotation(Excel.class);
                XSSFCell cell = row.createCell(j++);
                if(isTile) {
                    String name = annotation.name();
                    cell.setCellValue(name);
                }else{
                    Class<?> type = declaredField.getType();
                    Object o = declaredField.get(data);
                    if(o != null && type.equals(String.class)){
                        cell.setCellValue((String)o);
                    }
                    if(o != null && type.equals(Date.class)){
                        String format = annotation.format();
                        if(format == null){
                            format = "yyyy-MM-dd HH:mm:ss";
                        }
                        cell.setCellValue(new SimpleDateFormat(format).format(o));
                    }
                    if(o != null && type.equals(Integer.class)){
                        cell.setCellValue((Integer)o);
                    }
                    if(o != null && type.equals(Long.class)){
                        cell.setCellValue((Long)o);
                    }
                }
            }
        }
    }
}
