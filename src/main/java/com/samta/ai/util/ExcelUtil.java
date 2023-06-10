package com.samta.ai.util;

import com.samta.ai.handler.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class ExcelUtil {

    public static boolean isRowEmpty(final XSSFRow row) {
        if (row == null)
            return true;
        for (int cellIndex = row.getFirstCellNum(); cellIndex < row.getLastCellNum(); cellIndex++) {
            final var currentCell = row.getCell(cellIndex);
            if (currentCell != null && currentCell.getCellType() != CellType.BLANK)
                return false;
        }
        return true;
    }

    public static XSSFWorkbook getWorkBookFromFile(final MultipartFile file) throws CustomException {
        XSSFWorkbook workBook;
        try {

            workBook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            log.error("Unable to create workBook from provided file: {}", e);
            throw new CustomException(HttpStatus.BAD_REQUEST.value(), "Unable to create workBook from provided file");
        }
        return workBook;
    }

    public static void closeWorkbook(XSSFWorkbook xssfWorkbook)  {
        if(xssfWorkbook!=null) {
            try {
                xssfWorkbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
