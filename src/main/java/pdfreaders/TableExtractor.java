package pdfreaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.pdfbox.pdmodel.PDDocument;
import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

/**
 * This class handles the first stage of extraction, it extracts tables from a pdf.
 * The tables will be stored with respect to the page they reside in.
 * The extracted pages can also be retrieved, if that's required.
 */
class TableExtractor {
    private PDDocument pdfDocument;
    private ObjectExtractor pageExtractor;
    private SpreadsheetExtractionAlgorithm tableExtractor;

    @Getter private List<Page> pages;
    @Getter private List<Table> tables;
    @Getter private List<Integer> pageOfTables;

    /**
     * Given a pdf filepath, it extracts tables.
     * @param filePath Filepath to the PDF that is to be extracted.
     */
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
        tables = new ArrayList<>();
        pageOfTables = new ArrayList<>();
        for (Page page : pages) {
            List<Table> tablesOfPage = tableExtractor.extract(page);
            for (Table table : tablesOfPage) {
                tables.add(table);
                pageOfTables.add(page.getPageNumber());
            }
        }
    }

}
