package pdfreaders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.yaml.snakeyaml.Yaml;

/**
 * This class extracts all templates from a given file.
 */
public class TemplateReader {
    private List<Template> templates;
    @Getter private String curriculumName;
    
    final String MATCH_ALL_PATTERN = ".*";
    
    /**
     * Given a yaml file, extract all found templates.
     * @param filePath Filepath to the YAML file
     * @throws IllegalArgumentException YAML was not found in provided path
     */
    public void read(String filePath) {
        Yaml yaml = new Yaml();
        templates = new ArrayList<>();
        try {
            Iterable<Object> objects = yaml.loadAll(new FileReader(filePath));
            Iterator<Object> objectsIterator = objects.iterator();
            
            Object metadata = objectsIterator.next();
            getCurriculumMetadata(metadata);
            
            while (objectsIterator.hasNext()) {
                Object object = objectsIterator.next();
                Template template = castToTemplate(object);
                templates.add(template);
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("YAML file not found");
        }
    }
    
    private void getCurriculumMetadata(Object object) {
        Map map = (Map) object;
        curriculumName = map.get("name").toString();
    }
    
    private Template castToTemplate(Object object) {
        Map map = (Map) object;
        List<Integer> pageNumbers = castEntryToNumberList(map.get("pages"));
        int collumnsize = castEntryToInt(map.get("collumnsize"));
        Template.SemesterMode semesterMode = castEntryToSemesterMode(map.get("semestermode"));
        
        Template template = new Template(pageNumbers, collumnsize, semesterMode);
        template = setTemplateProperties(template, map);
        return template;
    }
    
    private List<Integer> castEntryToNumberList(Object entry) {
        String ranges = entry.toString();
        List<Integer> numberList = new ArrayList<>();
        for (String range : ranges.split(",")) {
            String trimmedRange = range.trim();
            if (trimmedRange.contains("-")) {
                String[] parts = trimmedRange.split("-");
                int startOfRange = Integer.valueOf(parts[0]);
                int endOfRange = Integer.valueOf(parts[1]);
                for (int i = startOfRange; i <= endOfRange; i++)
                    numberList.add(i);
            } else {
                int number = Integer.valueOf(trimmedRange);
                numberList.add(number);
            }
        }
        return numberList;
    }
    
    private int castEntryToInt(Object entry) {
        return Integer.valueOf(entry.toString());
    }
    
    private Template.SemesterMode castEntryToSemesterMode(Object entry) {
        String mode = entry.toString();
        if (mode.equals("increment"))
            return Template.SemesterMode.INCREMENT;
        else {
            short constant = Short.valueOf(mode);
            Template.SemesterMode semesterMode = Template.SemesterMode.CONSTANT;
            semesterMode.setSemester(constant);
            return semesterMode;
        }
    }

    private Template setTemplateProperties(Template template, Map map) {
        Map attributes;
        int col;
        String regex;
        String headerRegex;
        
        attributes = (Map) map.get("name");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        if (attributes.containsKey("header"))
            headerRegex = attributes.get("header").toString();
        else headerRegex = MATCH_ALL_PATTERN;
        template.setName(col, regex, headerRegex);
        
        attributes = (Map) map.get("code");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        if (attributes.containsKey("header"))
            headerRegex = attributes.get("header").toString();
        else headerRegex = MATCH_ALL_PATTERN;
        template.setCode(col, regex, headerRegex);
        
        attributes = (Map) map.get("credits");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        if (attributes.containsKey("header"))
            headerRegex = attributes.get("header").toString();
        else headerRegex = MATCH_ALL_PATTERN;
        template.setCredits(col, regex, headerRegex);
        
        attributes = (Map) map.get("prerequisites");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        if (attributes.containsKey("header"))
            headerRegex = attributes.get("header").toString();
        else headerRegex = MATCH_ALL_PATTERN;
        template.setPrerequisites(col, regex, headerRegex);
        
        return template;
    }
    
    public Template[] getTemplates() {
        return templates.toArray(new Template[0]);
    }
}
