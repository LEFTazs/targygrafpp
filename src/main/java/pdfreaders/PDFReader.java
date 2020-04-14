package pdfreaders;

import TargygrafPP.Subject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

@Slf4j
public class PDFReader implements PDFReaderInterface {
    private TableExtractor extractor = new TableExtractor();
    
    @Override
    public Subject[] readSubjects(String filePath) {
        extractor.extractTablesFromPDF(filePath);
        
        List<Subject> extractedSemesterSubjects = extractSemesterSubjects(8, 13);
        List<Subject> extractedDifferentialSubjects = extractDifferentialSubjects(14, 16);
        extractedSemesterSubjects.addAll(extractedDifferentialSubjects);
        return extractedSemesterSubjects.toArray(new Subject[0]);
    }
    
    
    private List<Subject> extractSemesterSubjects(int fromPage, int toPage) {
        List<Subject> extractedSubjects = new ArrayList<>();
        short currentSemester = 1;
        for (Map.Entry<Integer, Table> entry : extractor.getTables().entrySet()) {
            int page = entry.getKey();
            if (fromPage <= page && page <= toPage) {
                Table table = entry.getValue();
                List<List<RectangularTextContainer>> rows = table.getRows();
                for (List<RectangularTextContainer> row : rows.subList(1, rows.size() - 1)) {
                    Subject subject = extractSubjectFromRow(row, currentSemester);
                    extractedSubjects.add(subject);
                }
                currentSemester++;
            }
        }
        return extractedSubjects;
    }
    
    private Subject extractSubjectFromRow(List<RectangularTextContainer> row, short semester) {
        Iterator<RectangularTextContainer> iterator = row.iterator();
        String name = iterator.next().getText();
        name = name.replace("\r", " ");
        String code = iterator.next().getText();
        iterator.next();
        String creditCell = iterator.next().getText();
        int creditValue = Integer.parseInt(creditCell.split("\r")[0]);
        iterator.next();
        String[] prerequisites = extractPrerequisites(iterator.next().getText());
        
        Subject subject = new Subject(name, code, semester, creditValue);
        subject.setPrerequisites(prerequisites);
        return subject;
    }
    
    private String[] extractPrerequisites(String cellContents) {
        if (cellContents.equals("-"))
            return new String[0];
        else return cellContents.split("\r");
    }
    
    private List<Subject> extractDifferentialSubjects(int fromPage, int toPage) {
        List<Subject> extractedSubjects = new ArrayList<>();
        short differentialSemesterId = 0;
        for (Map.Entry<Integer, Table> entry : extractor.getTables().entrySet()) {
            int page = entry.getKey();
            if (fromPage <= page && page <= toPage) {
                Table table = entry.getValue();
                List<List<RectangularTextContainer>> rows = table.getRows();
                for (List<RectangularTextContainer> row : rows) {
                    if (!row.get(1).getText().isBlank() && !"tantárgy neve".equals(row.get(0).getText())) {
                        Subject subject = extractSubjectFromRow(row, differentialSemesterId);
                        extractedSubjects.add(subject);
                    }
                }
            }
        }
        return extractedSubjects;
    }
    
    private void printTables() {
        for (Table table : extractor.getTables().values()) {
            System.out.println(table.getExtractionMethod());
            List<List<RectangularTextContainer>> rows = table.getRows();
            for (List<RectangularTextContainer> row : rows) {
                for (RectangularTextContainer cell : row) {
                    System.out.print(cell.getText() + "\t");
                }
                System.out.println("");
            }
        }
    }
}
