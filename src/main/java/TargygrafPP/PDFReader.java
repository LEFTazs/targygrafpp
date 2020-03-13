package TargygrafPP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

@Slf4j
public class PDFReader implements PDFReaderInterface{
    @Override
    public Subject[] readSubjects(String filePath) {
         //TABULA algorythms
        PDDocument pd = null;
        try {
            pd = PDDocument.load(new File(filePath));
        } catch (IOException ex) {
            log.error(ex.getMessage());  //TODO: saját Exception osztályt dobni itt
        }
        ObjectExtractor oe  = new ObjectExtractor(pd);
        SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
        
        //Declaration
        List<Page> pages = new ArrayList();
        List<Table> tables = new ArrayList();
        Map<Integer, List<Table>> semesters = new HashMap();
        int NoSemesters = 0;
        
        //Extracting PDF pages
        for(int i = 1; i < 30; i++){
            try{
                Page page = oe.extract(i);
                pages.add(page);
            }catch(IndexOutOfBoundsException e){
                break;
            }
        }
        
        //Extracting tables from pages
        for(Page p: pages){
            List<Table> table = sea.extract(p);
            for(Table t : table)
                tables.add(t);
        }
        
        
        for(int i = 0; i < tables.size(); i++){
            if(tables.get(i).getCell(0, 0).getText(false).contains("tantárgy neve")){
                List<Table> currentTableList = new ArrayList();
                currentTableList.add((tables.get(i)));
                if(tables.get(i+1).getCell(0, 2).getText(false).charAt(1) == '+'){
                    int j = i + 1;
                    while(tables.get(j).getCell(0, 2).getText(false).charAt(1) == '+'){
                        currentTableList.add(tables.get(j));
                        j++;
                    }
                }
                semesters.put(NoSemesters, currentTableList);
                NoSemesters++;
            }
        }
                
        Subject[] subjects = new Subject[200];
        int subjectCount = 0;
        
        for(int i = 0; i < NoSemesters; i++){
            if(i != NoSemesters - 1){
                for(int k = 0; k < semesters.get(i).size(); k++){
                    
                    Table table = semesters.get(i).get(k);
                    
                    for(int j = 1; j < 100; j++){
                        try{
                                                    
                            String subjectName = table.getCell(j, 0).getText(false);
                            String subjectCode = table.getCell(j, 1).getText(false);
                            int subjectCredit = table.getCell(j, 3).getText(false).charAt(0);
                            
                            subjects[subjectCount] = new Subject(subjectName, subjectCode, (short) (i  + 1), subjectCredit);
                            subjectCount++;
                            
                        }catch(NullPointerException e){ // NullPointerException
                            break;
                        }
                    }
                }
            }
            else{
                
                Table table = semesters.get(i).get(0);

                for(int j = 1; j < 100; j++){
                    try{

                        String subjectName = table.getCell(j, 0).getText(false);
                        String subjectCode = table.getCell(j, 1).getText(false);
                        int subjectCredit = table.getCell(j, 3).getText(false).charAt(0);

                        subjects[subjectCount] = new Subject(subjectName, subjectCode, (short) i, subjectCredit);
                        subjectCount++;

                    }catch(NullPointerException e){ // NullPointerException
                        break;
                    }
                }
                
                for(int k = 1; k < semesters.get(i).size(); k++){
                    
                    table = semesters.get(i).get(k);
                    
                    for(int j = 1; j < 100; j++){
                        try{

                            String subjectName = table.getCell(j, 0).getText(false);
                            String subjectCode = table.getCell(j, 1).getText(false);
                            int subjectCredit = table.getCell(j, 3).getText(false).charAt(0);

                            subjects[subjectCount] = new Subject(subjectName, subjectCode, (short) i, subjectCredit);
                            subjectCount++;

                        }catch(NullPointerException e){ // NullPointerException
                            break;
                        }
                    }
                }
                
            }
        }

        return subjects;
    }
    
}
