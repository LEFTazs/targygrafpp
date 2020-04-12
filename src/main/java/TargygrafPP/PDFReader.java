package TargygrafPP;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    private static PDDocument pdfDocument;
    private static ObjectExtractor pageExtractor;
    private static SpreadsheetExtractionAlgorithm tableExtractor;
    
    private static List<Page> pages;
    private static Map<Integer, Table> tables;
    
    @Override
    public Subject[] readSubjects(String filePath) {
        try {
            pdfDocument = PDDocument.load(new File(filePath));
        } catch (IOException ex) {
            throw new IllegalArgumentException("PDF couldn't be loaded from given path.");
        }
        pageExtractor  = new ObjectExtractor(pdfDocument);
        tableExtractor = new SpreadsheetExtractionAlgorithm();
        
        extractPages();
        extractTables();
        
        List<Subject> extractedSemesterSubjects = extractSemesterSubjects(8, 13);
        List<Subject> extractedDifferentialSubjects = extractDifferentialSubjects(14, 16);
        extractedSemesterSubjects.addAll(extractedDifferentialSubjects);
        return extractedSemesterSubjects.toArray(new Subject[0]);
    }
    
    private static void extractPages() {
        pages = new ArrayList<>();
        PageIterator pageIterator = pageExtractor.extract();
        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            pages.add(page);
        }
    }

    private static void extractTables() {
        tables = new HashMap<>();
        for (Page page : pages) {
            List<Table> tablesOfPage = tableExtractor.extract(page);
            for (Table table : tablesOfPage)
                tables.put(page.getPageNumber(), table);
        }
    }
    
    private static List<Subject> extractSemesterSubjects(int fromPage, int toPage) {
        List<Subject> extractedSubjects = new ArrayList<>();
        short currentSemester = 1;
        for (Map.Entry<Integer, Table> entry : tables.entrySet()) {
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
    
    private static Subject extractSubjectFromRow(List<RectangularTextContainer> row, short semester) {
        Iterator<RectangularTextContainer> iterator = row.iterator();
        String name = iterator.next().getText();
        name = name.replace("\r", " ");
        String code = iterator.next().getText();
        iterator.next();
        String creditCell = iterator.next().getText();
        int creditValue = Integer.parseInt(creditCell.split("\r")[0]);
        iterator.next();
        iterator.next(); // TODO: prerequisite
        
        return new Subject(name, code, semester, creditValue);
    }
    
    private static List<Subject> extractDifferentialSubjects(int fromPage, int toPage) {
        List<Subject> extractedSubjects = new ArrayList<>();
        short differentialSemesterId = 0;
        for (Map.Entry<Integer, Table> entry : tables.entrySet()) {
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
    
    private static void printTables() {
        for (Table table : tables.values()) {
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
