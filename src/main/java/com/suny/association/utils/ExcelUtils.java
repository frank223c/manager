package com.suny.association.utils;

import com.suny.association.enums.BaseEnum;
import com.suny.association.exception.BusinessException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Comments:   对Excel的相关操作工具类
 *
 * @author :   孙建荣
 *         Create Date: 2017/05/15 18:09
 */
public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
    /**
     * Excel表格2003版本之前的文件的后缀名
     */
    private static final String EXCEL_FILE_BEFORE_2003_EXTENSION = "xls";
    /**
     * Excel表格2007版本之后的文件的后缀名
     */
    private static final String EXCEL_FILE_AFTER_2007_EXTENSION = "xlsx";
    /**
     * 文件魔数,Excel表格2003版本以前的魔术值
     */
    private static final String EXCEL_FILE_BEFORE_2003_MAGIC = "application/vnd.ms-excel";
    /**
     * 文件魔数,Excel表格2007版本以后的魔术值
     */
    private static final String EXCEL_FILE_AFTER_2007_MAGIC = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    /**
     * 定义在Excel表格中超过多少位的值,在Java中就不能使用Integer类型进行运行了
     */
    private static final Integer LONG_NUMBER_LENGTH = 11;

    private ExcelUtils() {
    }

    /**
     * 根据传入的Excel文件解析出数据,然后返回一个有序的List对象，根据POI的解析规则,一步步把解析出来的数据放到数组里去.
     *
     * @param file          Excel文件
     * @param fileExtension Excel文件的扩展名，根据扩展名来解析不同版本的Excel文件
     * @param startSheet    开始读取的工作表下标
     * @param startRow      开始读取的工作行下标
     * @return 有序的List对象，必须是有序的
     */
    public static List<String[]> parseExcel(File file, String fileExtension, int startSheet, int startRow) {
        List<String[]> dataList = new LinkedList<>();
          /*   首选获取一个工作簿   */
        Workbook workbook = getWorkbook(file, fileExtension);
        if (isOverFlowSheet(workbook, startRow)) {
            logger.error("读取Excel表格时读取的工作表下标溢出，不存在这个工作表");
            throw new BusinessException(BaseEnum.SHEET_NUM_OVERFLOW);
        }
          /*   这里传入一个起始工作表的序号，读取这个工作表      */
        Sheet sheet = workbook.getSheetAt(startSheet);
         /*  遍历单元表的每一行的数据    */
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
             /*  Excel的每一行数据  */
            Row row = sheet.getRow(i);
             /*    起始行的行数        */
            int firstCellNum = row.getFirstCellNum();
             /*  获取当前行的列数 */
            int numberOfCells = row.getPhysicalNumberOfCells();
             /* 首先要判断下文件中是否有大于单元格，否则就是空的了，肯定不读取   */
            if (numberOfCells > 0) {
                 /* 有多少列就创建一个多大的数组  */
                String[] dataArray = new String[numberOfCells];
                for (int startNum = firstCellNum; startNum < numberOfCells; startNum++) {
                    /*  Excel一行数据中的每一个单元格的数据  */
                    Cell cell = row.getCell(startNum);
                     /* 把取到的值放到数组中中去 */
                    dataArray[startNum] = getCellValue(cell);
                }
                dataList.add(dataArray);
            } else {
                logger.warn("单元格是空白的，无法读取");
            }

        }
        return dataList;
    }


    /**
     * 检查传入的开始读取的工作表下表是否大于文件中存在的工作表数量,防止下标溢出.
     * <b>startSheet</b>是一个读取工作表的下标位置，请注意是从<b>0</b>开始的，
     * <b>workbook.getNumberOfSheets()</b>得到的是工作表的数量，是从<b>1</b>
     * 开始的，所以在判断时应该判断大于或者等于
     *
     * @param workbook   读取出来的工作簿
     * @param startSheet 开始读取的工作表下标，开始读取时从第0个开始读取的
     * @return 如果越界就说明不可行，返回false，如果没有超出就返回true
     */
    private static boolean isOverFlowSheet(Workbook workbook, int startSheet) {
        return startSheet >= workbook.getNumberOfSheets();
    }


    /**
     * 根据传进来的cell判断是什么类型的值，然后返回什么类型的值
     *
     * @param cell 代表一个小单元格
     * @return 根据类型判断出来的值
     */
    private static String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString();
            /*    取出来的值是数字类型的时候也会分几种情况，第一种情况就是取出来的是日期字符型的时候 ，要么就是纯数字了      */
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue() + "";
                }
                DecimalFormat df = new DecimalFormat("0");
                String stringNumber = df.format(cell.getNumericCellValue());
                /*  长度大于十一位的就应该是Long类型的数字了，就转换成Long类型数字，否则默认就是转换成Integer类型  */
                if (stringNumber.length() >= LONG_NUMBER_LENGTH) {
                    return convertLong(stringNumber) + "";
                }
                return convertInteger(stringNumber) + "";
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_ERROR:
                return "类型错误";
            default:
                return "无法判断的一个类型";
        }

    }


    /**
     * 将一个String类型的数字转换成一个Long类型的数字
     *
     * @param stringNumber String类型的数字
     * @return 如果是一个可以被转换的数字的话就转换成那个数字，否则就默认设置一个默认的值9999，以表示这条数据出错
     */
    private static Long convertLong(String stringNumber) {
        long parseLong;
        try {
            parseLong = Long.parseLong(stringNumber);
        } catch (NumberFormatException e) {
            logger.error("解析出来的数字有问题");
            throw new BusinessException(BaseEnum.ROW_VALUE_CONVERT_NUMBER_FAIL);
        }
        return parseLong == 0L ? 9999 : parseLong;
    }


    /**
     * 将一个String类型的数字转换成一个Integer类型的数字
     *
     * @param stringNumber String类型的数字
     * @return 如果是一个可以被转换的数字的话就转换成那个数字，否则就默认设置一个默认的值9999，以表示这条数据出错
     */
    private static Integer convertInteger(String stringNumber) {
        int parseInt;
        try {
            parseInt = Integer.parseInt(stringNumber);
        } catch (NumberFormatException e) {
            logger.error("解析出来的数字有问题");
            throw new BusinessException(BaseEnum.ROW_VALUE_CONVERT_NUMBER_FAIL);
        }
        return parseInt == 0 ? 9999 : parseInt;
    }


    /**
     * 根据上传的Excel版本返回不同的解析Excel表格的方法
     *
     * @param file          上传的Excel文件
     * @param fileExtension 上传的文件的扩展名
     * @return 根据不同的文件扩展名返回不同的工具方法
     */
    private static Workbook getWorkbook(File file, String fileExtension) {
        try (FileInputStream fs = new FileInputStream(file)) {
            switch (fileExtension) {
                case EXCEL_FILE_BEFORE_2003_EXTENSION:
                    try {
                        return new HSSFWorkbook(fs);
                    } catch (IOException e) {
                        logger.warn("读取的文件格式不支持");
                        throw new BusinessException(BaseEnum.FILE_READ_FAIL);
                    }
                case EXCEL_FILE_AFTER_2007_EXTENSION:
                    try {
                        return new XSSFWorkbook(fs);
                    } catch (IOException e) {
                        logger.warn("读取的文件格式不支持");
                        throw new BusinessException(BaseEnum.FILE_READ_FAIL);
                    }
                default:
                    logger.warn("读取的文件格式不支持");
                    throw new BusinessException(BaseEnum.FILE_NOT_SUPPORT);
            }
        } catch (IOException e) {
            logger.error("不存在这个文件，无法进行读取");
            throw new BusinessException(BaseEnum.FILE_NOT_EXIST);
        }
    }


    /**
     * 比较上传的Excel文件的文件类型是否是Excel表格，防止客户端欺骗
     *
     * @param fileType 上传文件的文件类型
     * @return 如果是Excel表格则返回true，否则就返回false
     */
    public static boolean parseExcelFileType(String fileType, String fileExtension) {

        if (EXCEL_FILE_BEFORE_2003_MAGIC.equals(fileType) && EXCEL_FILE_BEFORE_2003_EXTENSION.equals(fileExtension)) {
            logger.info("Windows 2003版以前的EXCEL表格");
            return true;
        } else if (EXCEL_FILE_AFTER_2007_MAGIC.equals(fileType) && EXCEL_FILE_AFTER_2007_EXTENSION.equals(fileExtension)) {
            logger.info("Windows 2007版后的Excel表格");
            return true;
        }
        logger.warn("未知的文件扩展名");
        return false;
    }


}
