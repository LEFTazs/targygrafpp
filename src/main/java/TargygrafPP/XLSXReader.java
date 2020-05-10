package TargygrafPP;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XLSXReader implements XLSXReaderInterface{

    private static XSSFWorkbook workBook;
    private static XSSFSheet sheet;
    private static List<UserSubject> results;
    
    @Override
    public UserSubject[] readSubjects(String filePath, Subject[] subjects) {
        try {
            workBook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
        } catch (IOException ex) {
            throw new IllegalArgumentException("XLSX file couldn't be loaded from given path.");
        }
       
        results = new ArrayList<>();
        sheet = workBook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        
        while (rowIterator.hasNext()) { 
            
            Row row = rowIterator.next();
            
            if(row.getRowNum() == 0){
                rowIterator.next();
            }else{
                
                Subject subject = new Subject();
                String[] lecturers = new String[0];
                String attendedSemester = "";
                boolean hasSignature = false;
                int mark = 0;
                
                for(int i = 0; i < row.getLastCellNum(); i++){
                    
                    Cell cell = row.getCell(i);
                    
                    
                    switch (i){
                    case 0:
                        subject = findSubject(cell.getStringCellValue(), subjects);
                        break;
                    case 1:
                        lecturers = findLecturers(cell.getStringCellValue());
                        break;
                    case 3:
                        attendedSemester = cell.getStringCellValue();
                        break;
                    case 7:
                        hasSignature = findSignature(cell.getStringCellValue());
                        break;
                    case 8:
                        mark = findMark(cell.getStringCellValue());
                        break;
                    }
                }
                
                results.add(new UserSubject(subject, lecturers, attendedSemester, hasSignature, mark));
            }
        }
        return results.toArray(new UserSubject[0]);
    }

    
    
    private Subject findSubject(String subCode, Subject[] subjects) {
        for(Subject subject : subjects){
            if(subject.getCode().equals(subCode))
                return subject;
        }
        return new Subject();
    } 

    private Boolean findSignature(String cellValue) {
        String[] words = cellValue.split(" ");
        return words[0].equals("Aláírva");
    }

    private String[] findLecturers(String cellValue) {
        String[] words = cellValue.split(",");
        List<String> lecturers = new ArrayList<>();
        
        for(int i = 1; i < words.length; i++){
            lecturers.add(words[i]);
        }
        return lecturers.toArray(new String[0]);
    }

    private int findMark(String cellValue) {
        String[] words = cellValue.split(" ");
        String mark = words[0];
        
        switch(mark){
            case "Elégtelen":
                return 1;
            case "Elégséges":
                return 2;
            case "Közepes":
                return 3;
            case "Jó":
                return 4;
            case "Jeles":
                return 5;
        }
      return 0;
    }
}
