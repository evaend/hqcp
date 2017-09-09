package com.phxl.hqcp.common.util;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phxl.core.base.util.DateUtils;

/**
 * 常用数据视图导出工具
 * @date	2016年11月21日 上午10:52:37
 * @author	黄文君
 * @version 1.0
 * @since	JDK 1.6
 */
public class ExportUtil {
	public final static Logger logger = LoggerFactory.getLogger(ExportUtil.class);
			
	/**
	 * 根据给定数据源，导出EXCEL文档
	 * @param	response	HttpServletResponse
	 * @param	fieldName	excel每一列的表头.
	 * @param	dataList	excel数据，其中的key和表头中的值对应，value为数据
	 * @param	title		表格标题，在表格最上面
	 * @param	before		表格页眉（比如制单时间，科室之类的数据），紧跟标题，list中的值由逗号(英文)分隔数据（单元格），每一个String为一排
	 * @param	after		表格页尾（比如制单时间，科室之类的数据），在表格末尾，list中的值由逗号(英文)分隔数据（单元格），每一个String为一排
	 * @param	formatMap		数据格式，其中的key和表头中的key对应，value是格式编码。（如果为日期，则传日期格式yyyy-MM-dd之类的，
	 * 						如果是数字，想保留小数位数，保留1位小数传0.0，保留2位小数传0.00，以此类推）
	 * @param	excelName
	 * @throws	Exception
	 */
	public static void exportExcel(HttpServletResponse response, List<String> fieldName,
			List<Map<String, Object>> dataList, Map<String, String> formatMap, String title, List<String> before,
			List<String> after, String excelName) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		
		/**样式定义*/
		//居中样式
		HSSFCellStyle centerStyle = workbook.createCellStyle();//设置格式
		centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
		centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//居中样式
		HSSFCellStyle rightStyle = workbook.createCellStyle();//设置格式
		rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
		rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//创建顶部标题样式
		HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
		cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//创建顶部标题字体
		HSSFFont fontTitle = workbook.createFont();
		//fontTitle.setFontName("宋体");
		fontTitle.setFontHeightInPoints((short) 16);
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellTitleStyle.setFont(fontTitle);
		//表头列名样式
		HSSFCellStyle colNameStype = workbook.createCellStyle();
		colNameStype.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		colNameStype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		colNameStype.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//灰色25
		colNameStype.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充
		
		/**表名（大标题）*/
		for (int i=0; i<fieldName.size(); i++) {
			sheet.setDefaultColumnStyle(i, centerStyle);
			sheet.setColumnWidth(i, 4500);
		}
		
		/**创建标题*/
		int rowNum = 0;//第一行
		HSSFRow rowTitle = sheet.createRow(rowNum++);
		rowTitle.setHeightInPoints(40);
		HSSFCell cellTitle = rowTitle.createCell(0);//表名单元格
		cellTitle.setCellValue(title);
		cellTitle.setCellStyle(cellTitleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (fieldName.size()-1)));
		
		if (CollectionUtils.isNotEmpty(before)) {
			//创建页眉
			for (String item : before) {
				HSSFRow rowHeader = sheet.createRow(rowNum++);
				rowHeader.setHeightInPoints(18);
				if (StringUtils.isEmpty(item)) {
					continue;
				}
				String[] strs = item.split(",");
				for (int i = 0; i < strs.length; i++) {
					HSSFCell cell = rowHeader.createCell(i);//创建单元格
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
					cell.setCellValue(strs[i]);
				}
			}
		}
		
		//创建表格列表(DataList)列标
		HSSFRow rowTableHead = sheet.createRow(rowNum++);
		rowTableHead.setHeight((short)400);
		for (int i = 0; i < fieldName.size(); i++) {
			HSSFCell cell = rowTableHead.createCell(i);//创建单元格
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
			cell.setCellValue(fieldName.get(i));
			cell.setCellStyle(colNameStype);
		}

		//创建表格列表(DataList)数据区域
		for (int i=0; i<dataList.size(); i++) {
			HSSFRow row = sheet.createRow(rowNum++);//创建行
			row.setHeight((short)400);
			for (int j = 0; j < fieldName.size(); j++) {
				HSSFCell cell = row.createCell(j);//创建单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
				Object columnValue = dataList.get(i).get(fieldName.get(j));//数据
				String columnValueFomart = MapUtils.isEmpty(formatMap)? null : formatMap.get(fieldName.get(j));//数据格式
				if (columnValue instanceof Date) {
					//日期格式化
					if (StringUtils.isBlank(columnValueFomart)) {
						columnValueFomart = DateUtils.TIME_FORMAT;
					}
					cell.setCellValue(DateUtils.format((Date)columnValue, columnValueFomart));
				} else if (columnValue instanceof Number) {
					//数字格式化
					if (null == columnValueFomart) {
						cell.setCellValue(columnValue.toString());
					} else {
						DecimalFormat df1 = new DecimalFormat(columnValueFomart);
						cell.setCellValue(df1.format(columnValue));
					}
					cell.setCellStyle(rightStyle);
				} else {
					cell.setCellValue(columnValue==null ? "" : columnValue.toString());
				}
			}
		}
		
		if (CollectionUtils.isNotEmpty(after)) {
			// 创建页尾
			for (String string : after) {
				HSSFRow fieldRows = sheet.createRow(rowNum);
				rowNum++;
				if (StringUtils.isEmpty(string)) {
					continue;
				}
				String[] strs = string.split(",");
				for (int i = 0; i < strs.length; i++) {
					// 创建单元格
					HSSFCell cell = fieldRows.createCell(i);

					// 定义单元格为字符串类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(strs[i]);
				}
			}
		}
		//导出Excel文件流到浏览器
		sendExcel(response, workbook, excelName);
	}

	/**
	 * 导出Excel文件流到浏览器
	 * @author	黄文君
	 * @date	2016年8月8日 上午9:26:21
	 *
	 * @param	response
	 * @param	workbook
	 * @param	excelName
	 * @throws	Exception
	 * @return	void
	 */
	public static void sendExcel(HttpServletResponse response, HSSFWorkbook workbook, String excelName) throws Exception {
		if (workbook != null) {
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + new String(excelName.getBytes("gb2312"), "ISO8859-1") + ".xls");
			OutputStream ouputStream = null;
			try {
				ouputStream = response.getOutputStream();
				workbook.write(ouputStream);
				ouputStream.flush();
			} catch (IOException e) {
				logger.error("导出Excel文件流到浏览器的时候异常!", e);
			} finally {
				try {
					ouputStream.close();
				} catch (IOException e) {
					logger.error("关闭OutputStream异常!", e);
				}
			}
		}
	}

	public static void exportExcel(HttpServletResponse response, List<String> fieldName,
			List<Map<String, Object>> dataList, Map<String, String> formatMap, String title, List<String> before,
			List<String> after, String excelName, Map<String, String> excelmap) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet(title);
		
		/**样式定义*/
		//居中样式
		HSSFCellStyle centerStyle = workbook.createCellStyle();//设置格式
		centerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//居中
		centerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//居中样式
		HSSFCellStyle rightStyle = workbook.createCellStyle();//设置格式
		rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);//右对齐
		rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//创建顶部标题样式
		HSSFCellStyle cellTitleStyle = workbook.createCellStyle();
		cellTitleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		cellTitleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		//创建顶部标题字体
		HSSFFont fontTitle = workbook.createFont();
		//fontTitle.setFontName("宋体");
		fontTitle.setFontHeightInPoints((short) 16);
		fontTitle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellTitleStyle.setFont(fontTitle);
		//表头列名样式
		HSSFCellStyle colNameStype = workbook.createCellStyle();
		colNameStype.setAlignment(HSSFCellStyle.ALIGN_CENTER);//水平居中
		colNameStype.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);//垂直居中
		colNameStype.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//灰色25
		colNameStype.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);//填充
		
		/**表名（大标题）*/
		for (int i=0; i<fieldName.size(); i++) {
			sheet.setDefaultColumnStyle(i, centerStyle);
			sheet.setColumnWidth(i, 4500);
		}
		
		/**创建标题*/
		int rowNum = 0;//第一行
		HSSFRow rowTitle = sheet.createRow(rowNum++);
		rowTitle.setHeightInPoints(40);
		HSSFCell cellTitle = rowTitle.createCell(0);//表名单元格
		cellTitle.setCellValue(title);
		cellTitle.setCellStyle(cellTitleStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, (short) 0, 0, (short) (fieldName.size()-1)));
		
		if (CollectionUtils.isNotEmpty(before)) {
			//创建页眉
			for (String item : before) {
				HSSFRow rowHeader = sheet.createRow(rowNum++);
				rowHeader.setHeightInPoints(18);
				if (StringUtils.isEmpty(item)) {
					continue;
				}
				String[] strs = item.split(",");
				for (int i = 0; i < strs.length; i++) {
					HSSFCell cell = rowHeader.createCell(i);//创建单元格
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
					cell.setCellValue(strs[i]);
				}
			}
		}
		
		//创建表格列表(DataList)列标
		HSSFRow rowTableHead = sheet.createRow(rowNum++);
		rowTableHead.setHeight((short)400);
		for (int i = 0; i < fieldName.size(); i++) {
			HSSFCell cell = rowTableHead.createCell(i);//创建单元格
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
			cell.setCellValue(excelmap.get(fieldName.get(i)));
			cell.setCellStyle(colNameStype);
		}

		//创建表格列表(DataList)数据区域
		for (int i=0; i<dataList.size(); i++) {
			HSSFRow row = sheet.createRow(rowNum++);//创建行
			row.setHeight((short)400);
			for (int j = 0; j < fieldName.size(); j++) {
				HSSFCell cell = row.createCell(j);//创建单元格
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);//定义单元格为字符串类型
				Object columnValue = dataList.get(i).get(fieldName.get(j));//数据
				String columnValueFomart = MapUtils.isEmpty(formatMap)? null : formatMap.get(fieldName.get(j));//数据格式
				if (columnValue instanceof Date) {
					//日期格式化
					if (StringUtils.isBlank(columnValueFomart)) {
						columnValueFomart = DateUtils.TIME_FORMAT;
					}
					cell.setCellValue(DateUtils.format((Date)columnValue, columnValueFomart));
				} else if (columnValue instanceof Number) {
					//数字格式化
					if (null == columnValueFomart) {
						cell.setCellValue(columnValue.toString());
					} else {
						DecimalFormat df1 = new DecimalFormat(columnValueFomart);
						cell.setCellValue(df1.format(columnValue));
					}
					cell.setCellStyle(rightStyle);
				} else {
					cell.setCellValue(columnValue==null ? "" : columnValue.toString());
				}
			}
		}
		
		if (CollectionUtils.isNotEmpty(after)) {
			// 创建页尾
			for (String string : after) {
				HSSFRow fieldRows = sheet.createRow(rowNum);
				rowNum++;
				if (StringUtils.isEmpty(string)) {
					continue;
				}
				String[] strs = string.split(",");
				for (int i = 0; i < strs.length; i++) {
					// 创建单元格
					HSSFCell cell = fieldRows.createCell(i);

					// 定义单元格为字符串类型
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(strs[i]);
				}
			}
		}
		//导出Excel文件流到浏览器
		sendExcel(response, workbook, excelName);
		
	}
}
