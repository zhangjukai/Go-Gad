package com.zjk.hy.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

/**
 * @author 张居开 2019-01-30
 */
@Slf4j
public class ExcelUtils {

    public static final String SUFFIX = ".xlsx";

    /**
     * 导出Excel数据
     *
     * @param response
     * @param data       数据
     * @param headers    表头
     * @param properties 每列的key，和表头顺序一致
     * @param fileName   文件名
     * @param <T>
     */
    public static <T> void exportToExcel(HttpServletResponse response, List<T> data, List<String> headers, List<String> properties, String fileName) {
        response.reset();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        if (!StringUtils.hasText(fileName)) {
            fileName = IdUtils.uuid();
        }

        try (BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("gbk"), "iso8859-1") + SUFFIX);
            generateExcel(out, data, headers, properties);
        } catch (IOException e) {
            log.error("获取输出流失败", e);
        }
    }

    /**
     * 导出Excel数据
     *
     * @param out
     * @param data       数据
     * @param headers    表头
     * @param properties 每列的key，和表头顺序一致
     * @param <T>        data的对象类型
     */
    public static <T> void generateExcel(OutputStream out, List<T> data, List<String> headers, List<String> properties) {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet();
            int[] maxWidth = new int[headers.size()];
            // Create headers
            createHeader(headers, maxWidth, wb, sheet);
            createBody(data, properties, wb, sheet, maxWidth);
            for (int index = 0; index < headers.size(); index++) {
                sheet.setColumnWidth(index, maxWidth[index]);
            }
            wb.write(out);
        } catch (IOException e) {
            log.error("写出excel失败", e);
        }
    }

    /**
     * 创建Excel导出内容体
     *
     * @param data
     * @param properties
     * @param wb
     * @param sheet
     * @param maxWidth
     * @param <T>
     */
    private static <T> void createBody(List<T> data, List<String> properties, Workbook wb, Sheet sheet, int[] maxWidth) {
        int i = 1, j;
        Cell cell = null;
        Row row = null;
        CellStyle cellStyle = createCellStyle(wb);
        Object value = null;
        for (T d : data) {
            sheet.autoSizeColumn(i);
            row = sheet.createRow(i++);
            j = -1;
            for (String key : properties) {
                j++;
                Field field = ReflectionUtils.findField(d.getClass(), key);
                field.setAccessible(true);
                cell = row.createCell(j);
                cell.setCellStyle(cellStyle);
                try {
                    value = field.get(d);
                    if (Date.class.isAssignableFrom(field.getType())) {
                        cell.setCellValue(DateTimeUtils.formatDate(Date.class.cast(value), DateTimeUtils.YYYY_MM_DD_HH_MM_SS_H));
                    } else {
                        cell.setCellValue(value.toString());
                    }
                    int newWidth = HyStringUtils.length(cell.getStringCellValue()) * 256 + 256;
                    maxWidth[j] = maxWidth[j] > newWidth ? maxWidth[j] : newWidth;
                } catch (IllegalAccessException e) { // 通过反射无法获取到值
                    cell.setCellValue("");
                }
            }
        }
    }

    /**
     * 创建表头
     *
     * @param headers
     * @param maxWidth
     * @param wb
     * @param sheet
     * @return
     */
    private static void createHeader(List<String> headers, int[] maxWidth, Workbook wb, Sheet sheet) {
        sheet.autoSizeColumn(0);
        Row row = sheet.createRow(0);
        Cell cell = null;
        // 标题样式（加粗，垂直居中）
        CellStyle cellStyle = createCellStyle(wb);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setFont(createFontStyle(wb, (short) 14));
        sheet.setDefaultRowHeight((short) (sheet.getDefaultRowHeight() + 30));
        int i = 0;
        for (String header : headers) {
            maxWidth[i] = HyStringUtils.length(header) * 300 + 300;
            //sheet.setColumnWidth(i, maxWidth[i]);
            cell = row.createCell(i++);
            cell.setCellValue(header);
            cell.setCellStyle(cellStyle);
        }
    }

    /**
     * 创建FontStyle
     *
     * @param wb
     * @param point 字体大小
     * @return
     */
    public static Font createFontStyle(Workbook wb, short point) {
        Font fontStyle = wb.createFont();
        fontStyle.setFontHeightInPoints(point);  //设置标题字体大小
        return fontStyle;
    }

    /**
     * 创建CellStyle
     *
     * @param wb
     * @return
     */
    public static CellStyle createCellStyle(Workbook wb) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
        cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
        cellStyle.setBorderTop(BorderStyle.THIN);//上边框
        cellStyle.setBorderRight(BorderStyle.THIN);//右边框
        return cellStyle;
    }

    private ExcelUtils() {
        throw new AssertionError("本类是一个工具类，不期望被实例化");
    }
}
