import java.io.FileInputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.*;
import java.io.*;

public class termPcompiler {
   public static class excel {
      int row;
      int col;
      boolean italic;
      String color;
      String value;

      excel(int r, int c, boolean i, String color, String value) {
         this.row = r;
         this.col = c;
         this.italic = i;
         this.color = color;
         this.value = value;
      }
   }

   public static File extract_excel(String filename) {
      FileWriter writer = null;
      try {

         File tempfile = File.createTempFile("temp", ".txt", null);
         tempfile.deleteOnExit();

         writer = new FileWriter(tempfile);
         BufferedWriter bw = new BufferedWriter(writer);
         ArrayList<excel> excel_list = new ArrayList<>();
         ArrayList<String> color_table = new ArrayList<>();
         color_table.add("null");
         try {
            FileInputStream file = new FileInputStream(filename);
            XSSFWorkbook workbook = new XSSFWorkbook(file);

            int rowindex = 0;
            int columnindex = 0;
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rows = sheet.getPhysicalNumberOfRows();
            for (rowindex = 0; rowindex < rows; rowindex++) {
               XSSFRow row = sheet.getRow(rowindex);
               if (row != null) {
                  int cells = row.getPhysicalNumberOfCells();
                  for (columnindex = 0; columnindex <= cells; columnindex++) {
                     XSSFCell cell = row.getCell(columnindex);
                     String value = "";
                     if (cell == null) {
                        continue;
                     } else {
                        switch (cell.getCellType()) {
                        case XSSFCell.CELL_TYPE_BOOLEAN:
                        	if(cell.getBooleanCellValue()) {
                        		value = "TRUE";
                        	} else {
                        		value = "FALSE";                        		
                        	}
                        	break;
                        case XSSFCell.CELL_TYPE_FORMULA:
                           value = cell.getCellFormula();
                           break;
                        case XSSFCell.CELL_TYPE_NUMERIC:
                        	cell.setCellType(HSSFCell.CELL_TYPE_STRING );
                        	value = cell.getStringCellValue();
                           break;
                        case XSSFCell.CELL_TYPE_STRING:
                           value = cell.getStringCellValue() + "";
                           break;
                        case XSSFCell.CELL_TYPE_BLANK:
                           value = "null";
                           break;
                        case XSSFCell.CELL_TYPE_ERROR:
                           value = cell.getErrorCellValue() + "";
                           break;
                        }
                     }
                     XSSFCellStyle cs = cell.getCellStyle();
                     String color;
                     boolean italic;
                     try {
                        color = cs.getFillForegroundXSSFColor().getARGBHex();
                        if (!color_table.contains(color)) {
                           color_table.add(color);
                        }
                     } catch (Exception e) {
                        color = "null";
                     }
                     italic = cs.getFont().getItalic();
                     excel ex = new excel(rowindex, columnindex, italic, color, value);
                     excel_list.add(ex);
                  }
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
         }

         String export = "";
         for (int i = 0; i < excel_list.size(); i++) {
            if (excel_list.get(i).value.equals("null") && (color_table.indexOf(excel_list.get(i).color) == 0))
               continue;
            export += excel_list.get(i).row + " ";
            export += excel_list.get(i).col + " ";
            export += color_table.indexOf(excel_list.get(i).color) + " ";
            if (excel_list.get(i).italic == true)
               export += "\"" + excel_list.get(i).value + "\"" + " ";
            else
               export += excel_list.get(i).value + "";
            export += "\n";
         }
         bw.write(export);
         bw.flush();
         //System.out.println(export);
         return tempfile;

      } catch (IOException e) {
         e.printStackTrace();
         return null;
      }
   }

   public static void main(String[] args) {
      File f = extract_excel("test.xlsx");
   }
}