package pdfreaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

class TableExtractor {
    private PDDocument pdfDocument;
    private ObjectExtractor pageExtractor;
    private SpreadsheetExtractionAlgorithm tableExtractor;

    @Getter private List<Page> pages;
    @Getter private Map<Integer, Table> tables;

    protected void extractTablesFromPDF(String filePath) {
        initalizeSubExtractors(filePath);
        extractPages();
        extractTables();
    }

    private void initalizeSubExtractors(String filePath) {
        try {
            pdfDocument = PDDocument.load(new File(filePath));
        } catch (IOException ex) {
            throw new IllegalArgumentException("PDF couldn't be loaded from given path.");
        }
        pageExtractor  = new ObjectExtractor(pdfDocument);
        tableExtractor = new SpreadsheetExtractionAlgorithm();
    }

    private void extractPages() {
        pages = new ArrayList<>();
        PageIterator pageIterator = pageExtractor.extract();
        while (pageIterator.hasNext()) {
            Page page = pageIterator.next();
            pages.add(page);
        }
    }

    private void extractTables() {
        tables = new HashMap<>();
        for (Page page : pages) {
            List<Table> tablesOfPage = tableExtractor.extract(page);
            for (Table table : tablesOfPage)
                tables.put(page.getPageNumber(), table);
        }
    }

}
