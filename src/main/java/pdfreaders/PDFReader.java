package pdfreaders;

import pojos.Subject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

/**
 * This is class handles the extraction of Subject objects from Tables.
 * Given a list of Template objects, it uses that information to extract
 * the Subject objects from the right location from the Tables.
 * It uses TableExtractor to extract the tables.
 */
@Slf4j
public class PDFReader implements PDFReaderInterface {
    private TableExtractor extractor;
    private Template currentTemplate;
    private int currentTable;
    
    private List<Subject> extractedSubjects;
    
    /**
     * Using TableExtractor, extract all Subjects according to the given Templates.
     * @param filePath Filepath of the PDF 
     * @param templates List of previously extracted templates
     */
    @Override
    public void readSubjects(String filePath, Template[] templates) {
        this.extractor = new TableExtractor();
        this.extractor.extractTablesFromPDF(filePath);
                
        this.extractedSubjects = new ArrayList<>();
        for (Template template : templates) {
            this.currentTemplate = template;
            extractSubjectsWithCurrentTemplate();
        }
    }
    
    private void extractSubjectsWithCurrentTemplate() {
        List<Table> tables = extractor.getTables();
        List<Integer> pageOfTables = extractor.getPageOfTables();
        this.currentTable = 0;
        for (int i = 0; i < tables.size(); i++) {
            int page = pageOfTables.get(i);
            Table table = tables.get(i);
            if (isPageNeeded(page)) {
                short currentSemester = chooseSemester(table);
                extractSubjectsFromTable(table, currentSemester);
            }
        }
    }
    
    private boolean isPageNeeded(int page) {
        return currentTemplate.getPages().contains(page);
    }
    
    private short chooseSemester(Table table) {
        Template.SemesterMode semesterMode = currentTemplate.getSemesterMode();
        if (semesterMode == Template.SemesterMode.CONSTANT) {
            return semesterMode.getSemester();
        } else if (semesterMode == Template.SemesterMode.INCREMENT) {
            if (doesTableHaveHeader(table)) 
                currentTable++;
            return (short) currentTable;
        }
        
        throw new RuntimeException("SemesterMode is unknown to PDFReader");
    }
    
    private boolean doesTableHaveHeader(Table table) {
        int firstRowId = 0;
        for (int i = 0; i < table.getColCount(); i++) {
            String cell = convertCellToString(table.getCell(firstRowId, i));
            int collumn = i + 1;
            if (collumn == currentTemplate.getNameCollumn()) {
                Pattern header = currentTemplate.getNameHeaderRegex();
                Matcher matcher = header.matcher(cell);
                if (!matcher.find()) 
                    return false;
            } else if (collumn == currentTemplate.getCodeCollumn()) {
                Pattern header = currentTemplate.getCodeHeaderRegex();
                Matcher matcher = header.matcher(cell);
                if (!matcher.find()) 
                    return false;
            } else if (collumn == currentTemplate.getCreditsCollumn()) {
                Pattern header = currentTemplate.getCreditsHeaderRegex();
                Matcher matcher = header.matcher(cell);
                if (!matcher.find()) 
                    return false;
            } else if (collumn == currentTemplate.getPrerequisitesCollumn()) {
                Pattern header = currentTemplate.getPrerequisitesHeaderRegex();
                Matcher matcher = header.matcher(cell);
                if (!matcher.find()) 
                    return false;
            }
        }
        return true;
    }
    
    private void extractSubjectsFromTable(Table table, short currentSemester) {
        List<List<RectangularTextContainer>> rows = table.getRows();
        for (List<RectangularTextContainer> row : rows) {
            extractSubjectFromRow(row, currentSemester);
        }
    }
    
    private void extractSubjectFromRow(List<RectangularTextContainer> row, short currentSemester) {
        String name = "";
        String code = "";
        int creditValue = -1;
        String[] prerequisites = new String[0];
        
        for (int i = 0; i < row.size(); i++) {
            String cell = convertCellToString(row.get(i));
            int collumn = i + 1;
            if (collumn == currentTemplate.getNameCollumn()) {
                Pattern nameRegex = currentTemplate.getNameRegex();
                Matcher matcher = nameRegex.matcher(cell);
                if (matcher.find()) {
                    int groupNum = matcher.groupCount() < 1 ? 0 : 1;
                    name = matcher.group(groupNum);
                } else return;
            } else if (collumn == currentTemplate.getCodeCollumn()) {
                Pattern nameRegex = currentTemplate.getCodeRegex();
                Matcher matcher = nameRegex.matcher(cell);
                if (matcher.find()) {
                    int groupNum = matcher.groupCount() < 1 ? 0 : 1;
                    code = matcher.group(groupNum);
                } else return;
            } else if (collumn == currentTemplate.getCreditsCollumn()) {
                Pattern nameRegex = currentTemplate.getCreditsRegex();
                Matcher matcher = nameRegex.matcher(cell);
                if (matcher.find()) {
                    int groupNum = matcher.groupCount() < 1 ? 0 : 1;
                    creditValue = Integer.parseInt(matcher.group(groupNum));
                } else return;
            } else if (collumn == currentTemplate.getPrerequisitesCollumn()) {
                Pattern nameRegex = currentTemplate.getPrerequisitesRegex();
                Matcher matcher = nameRegex.matcher(cell);
                if (matcher.find()) {
                    List<String> foundPrerequisites = new ArrayList<>();
                    int groupNum = matcher.groupCount() < 1 ? 0 : 1;
                    String currentGroup = matcher.group(groupNum);
                    if (currentGroup != null) foundPrerequisites.add(currentGroup);
                    while (matcher.find()) {
                        currentGroup = matcher.group(groupNum);
                        if (currentGroup != null) foundPrerequisites.add(currentGroup);
                    }
                    prerequisites = foundPrerequisites.toArray(new String[0]);
                } else return;
            }
        }
        
        Subject subject = new Subject(name, code, currentSemester, creditValue);
        subject.setPrerequisites(prerequisites);
        
        extractedSubjects.add(subject);
    }
    
    private String convertCellToString(RectangularTextContainer cell) {
        String text = cell.getText();
        text = text.replace("\r", "\n");
        return text;
    }
    
    @Override
    public Subject[] getExtractedSubjects() {
        return extractedSubjects.toArray(new Subject[0]);
    }
}
