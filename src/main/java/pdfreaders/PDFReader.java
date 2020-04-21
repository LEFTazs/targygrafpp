package pdfreaders;

import TargygrafPP.Subject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;

@Slf4j
public class PDFReader implements PDFReaderInterface {
    private TableExtractor extractor;
    private Template currentTemplate;
    private int currentTable;
    
    private List<Subject> extractedSubjects;
    
    @Override
    public Subject[] readSubjects(String filePath, Template[] templates) {
        this.extractor = new TableExtractor();
        this.extractor.extractTablesFromPDF(filePath);
                
        this.extractedSubjects = new ArrayList<>();
        for (Template template : templates) {
            this.currentTemplate = template;
            extractSubjectsWithCurrentTemplate();
        }
        return extractedSubjects.toArray(new Subject[0]);
    }
    
    
    private void extractSubjectsWithCurrentTemplate() {
        Map<Integer, Table> tables = extractor.getTables();
        this.currentTable = 0;
        int pageInLastIteration = -1;
        for (Map.Entry<Integer, Table> entry : tables.entrySet()) {
            int page = entry.getKey();
            Table table = entry.getValue();
            if (isPageNeeded(page)) {
                short currentSemester = chooseSemester(page, pageInLastIteration);
                extractSubjectsFromTable(table, currentSemester);
            }
            pageInLastIteration = page;
        }
    }
    
    private boolean isPageNeeded(int page) {
        return currentTemplate.getPages().contains(page);
    }
    
    private short chooseSemester(int page, int pageInLastIteration) {
        Template.SemesterMode semesterMode = currentTemplate.getSemesterMode();
        if (semesterMode == Template.SemesterMode.CONSTANT) {
            return semesterMode.getSemester();
        } else if (semesterMode == Template.SemesterMode.INCREMENT) {
            short semester = (short) currentTable;
            
            if (page == pageInLastIteration)
                currentTable++;
            
            return semester;
        }
        
        throw new RuntimeException("SemesterMode is unknown to PDFReader");
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
            String cell = row.get(i).getText();
            cell = cell.replace("\r", "\n");
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
}
