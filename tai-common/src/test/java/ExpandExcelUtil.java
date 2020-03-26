//package com.zaimi.center.product.common.utils;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.zaimi.common.utils.BeansUtil;
//import com.zaimi.common.utils.excel.ExcelUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections.MapUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.poi.hssf.usermodel.HSSFDateUtil;
//import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.ss.util.CellRangeAddress;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Demo ExpandExcelUtil
// *
// * @author ydy
// * @date 2020/3/16 18:08
// */
//@Slf4j
//public class ExpandExcelUtil {
//
//    private static SimpleDateFormat SIMPLEDATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    public static <T> List<T> parseExcelByContentType(InputStream excel, String contentType, Class<T> type, Map<String, String> mappedHeader) {
//        Workbook workbook = null;
//        try {
//            if (Objects.isNull(excel)) {
//                throw new RuntimeException("文件为空");
//            }
//            if (!isBasicType(type) && MapUtils.isEmpty(mappedHeader)) {
//                throw new RuntimeException("表头映射为空");
//            }
//            if (ExcelUtil.isXLS(contentType)) {
//                workbook = new HSSFWorkbook(excel);
//            } else if (ExcelUtil.isXLSX(contentType)) {
//                workbook = new XSSFWorkbook(excel);
//            } else {
//                throw new RuntimeException("非excel，解析失败");
//            }
//            return parseExcel(type, mappedHeader, workbook);
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            throw new RuntimeException("识别excel文件失败");
//        } finally {
//            if (null != workbook) {
//                try {
//                    workbook.close();
//                } catch (IOException e) {
//                    log.error(e.getMessage(), e);
//                }
//            }
//        }
//    }
//
//    private static <T> List<T> parseExcel(Class<T> type, Map<String, String> mappedHeader, Workbook workbook) {
//        List<T> result = Lists.newArrayList();
//        Sheet sheet = workbook.getSheetAt(0);
//        int rowCount = sheet.getPhysicalNumberOfRows();
//        int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
//
//        for (int i = 1; i < rowCount; i++) {
//            for (int j = 0; j < colCount; j++) {
//                if (Objects.isNull(sheet.getRow(i))) {
//                    continue;
//                }
//                Cell cell = sheet.getRow(i).getCell(j);
//                if (Objects.isNull(sheet.getRow(i).getCell(j)) || StringUtils.isBlank(getStringCellValue(sheet.getRow(i).getCell(j)))) {
//                    if (isMergedRegion(sheet, i, j)) {
//                        sheet.getRow(i).getCell(j).setCellValue(getMergedRegionValue(sheet, i, j));
//                    } else {
//                        continue;
//                    }
//                }
//                sheet.getRow(i).getCell(j).setCellValue(getStringCellValue(sheet.getRow(i).getCell(j)));
//                Cell cellAfter = sheet.getRow(i).getCell(j);
//            }
//        }
//
//        if (isBasicType(type)) {
//            int startIndex = MapUtils.isEmpty(mappedHeader) ? 0 : 1;
//            for (int i = startIndex; i < rowCount; i++) {
//                for (int j = 0; j < colCount; j++) {
//                    Object value = null;
//                    String cellValue = null;
//                    if (Objects.nonNull(sheet.getRow(i)) && Objects.nonNull(sheet.getRow(i).getCell(j))) {
//                        cellValue = getStringCellValue(sheet.getRow(i).getCell(j));
//                    }
//                    if (StringUtils.isNotBlank(cellValue)) {
//                        if (Integer.class.isAssignableFrom(type)) {
//                            value = Integer.valueOf(cellValue);
//                        } else if (Float.class.isAssignableFrom(type)) {
//                            value = Float.valueOf(cellValue);
//                        } else if (Double.class.isAssignableFrom(type)) {
//                            value = Double.valueOf(cellValue);
//                        } else if (Long.class.isAssignableFrom(type)) {
//                            value = Long.valueOf(cellValue);
//                        } else if (String.class.isAssignableFrom(type)) {
//                            value = cellValue.trim();
//                        }
//                    }
//                    if (Objects.isNull(value)) {
//                        continue;
//                    }
//                    result.add(type.cast(value));
//                }
//            }
//        } else {
//
//            Map<Integer, String> titleMap = new HashMap<>();
//            for (int i = 0; i < colCount; i++) {
//                if (Objects.nonNull(sheet.getRow(0).getCell(i))) {
//                    titleMap.put(i, getStringCellValue(sheet.getRow(0).getCell(i)));
//                }
//            }
//
//            for (int i = 1; i < rowCount; i++) {
//                Map<String, Object> row = Maps.newHashMap();
//                for (int j = 0; j < colCount; j++) {
//                    String title = titleMap.get(j);
//                    if (Objects.isNull(title)) {
//                        continue;
//                    }
//                    String value = null;
//                    if (Objects.nonNull(sheet.getRow(i)) && Objects.nonNull(sheet.getRow(i).getCell(j))) {
//                        value = getStringCellValue(sheet.getRow(i).getCell(j));
//                    }
//                    if (StringUtils.isNotBlank(value)) {
//                        row.put(mappedHeader.getOrDefault(title, title), value);
//                    }
//                }
//                if (MapUtils.isNotEmpty(row)) {
//                    result.add(BeansUtil.map2Bean(row, type));
//                }
//            }
//        }
//        return result;
//    }
//
//    /**
//     * 功能描述: 判断指定的单元格是否是合并单元格
//     *
//     * @param sheet  1
//     * @param row    行下标
//     * @param column 列下标
//     * @return : boolean
//     * @author : ydy
//     * @date : 2020/3/16 19:25
//     */
//    private static boolean isMergedRegion(Sheet sheet, int row, int column) {
//        int sheetMergeCount = sheet.getNumMergedRegions();
//        for (int i = 0; i < sheetMergeCount; i++) {
//            CellRangeAddress range = sheet.getMergedRegion(i);
//            int firstColumn = range.getFirstColumn();
//            int lastColumn = range.getLastColumn();
//            int firstRow = range.getFirstRow();
//            int lastRow = range.getLastRow();
//            if (row >= firstRow && row <= lastRow) {
//                if (column >= firstColumn && column <= lastColumn) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 功能描述: 获取合并单元格的值
//     *
//     * @param sheet  1
//     * @param row    2
//     * @param column 3
//     * @return : java.lang.String
//     * @author : ydy
//     * @date : 2020/3/16 19:28
//     */
//    private static String getMergedRegionValue(Sheet sheet, int row, int column) {
//        int sheetMergeCount = sheet.getNumMergedRegions();
//
//        for (int i = 0; i < sheetMergeCount; i++) {
//            CellRangeAddress ca = sheet.getMergedRegion(i);
//            int firstColumn = ca.getFirstColumn();
//            int lastColumn = ca.getLastColumn();
//            int firstRow = ca.getFirstRow();
//            int lastRow = ca.getLastRow();
//
//            if (row >= firstRow && row <= lastRow) {
//                if (column >= firstColumn && column <= lastColumn) {
//                    Row fRow = sheet.getRow(firstRow);
//                    Cell fCell = fRow.getCell(firstColumn);
//                    return getStringCellValue(fCell);
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 功能描述: 获取单元格的值
//     *
//     * @param cell 1
//     * @return : java.lang.String
//     * @author : ydy
//     * @date : 2020/3/16 19:27
//     */
//    private static String getStringCellValue(Cell cell) {
//        String cellValue = "";
//        if (cell == null) {
//            return cellValue;
//        } else if (cell.getCellType() == CellType.STRING) {
//            cellValue = cell.getStringCellValue();
//        } else if (cell.getCellType() == CellType.NUMERIC) {
//            if (HSSFDateUtil.isCellDateFormatted(cell)) {
//                double d = cell.getNumericCellValue();
//                Date date = HSSFDateUtil.getJavaDate(d);
//                cellValue = SIMPLEDATEFORMAT.format(date);
//            } else {
//                cellValue = BigDecimal.valueOf(cell.getNumericCellValue()).setScale(3, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString();
//            }
//        } else if (cell.getCellType() == CellType.BLANK) {
//            cellValue = "";
//        } else if (cell.getCellType() == CellType.BOOLEAN) {
//            cellValue = String.valueOf(cell.getBooleanCellValue());
//        } else if (cell.getCellType() == CellType.ERROR) {
//            cellValue = "";
//        } else if (cell.getCellType() == CellType.FORMULA) {
//            FormulaEvaluator formulaEvaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
//            getStringCellValue(formulaEvaluator.evaluateInCell(cell));
//        }
//        return cellValue.trim();
//    }
//
//
//    private static <T> boolean isBasicType(Class<T> type) {
//        return Integer.class.isAssignableFrom(type) || Float.class.isAssignableFrom(type) || Double.class.isAssignableFrom(type)
//                || Long.class.isAssignableFrom(type);
//    }
//}
