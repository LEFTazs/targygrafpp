package pdfreaders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

public class TemplateReader {
    public static Template[] read(String filePath) {
        Yaml yaml = new Yaml();
        List<Template> templates = new ArrayList<>();
        try {
            Iterable<Object> objects = yaml.loadAll(new FileReader(filePath));
            for (Object object : objects) {
                Template template = castToTemplate(object);
                templates.add(template);
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("YAML file not found");
        }
        
        return templates.toArray(new Template[0]);
    }
    
    private static Template castToTemplate(Object object) {
        Map map = (Map) object;
        List<Integer> pageNumbers = castEntryToNumberList(map.get("pages"));
        int collumnsize = castEntryToInt(map.get("collumnsize"));
        Template.SemesterMode semesterMode = castEntryToSemesterMode(map.get("semestermode"));
        Template template = new Template(pageNumbers, collumnsize, semesterMode);
        template = setTemplateProperties(template, map);
        return template;
    }
    
    private static List<Integer> castEntryToNumberList(Object entry) {
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
    
    private static int castEntryToInt(Object entry) {
        return Integer.valueOf(entry.toString());
    }
    
    private static Template.SemesterMode castEntryToSemesterMode(Object entry) {
        String mode = entry.toString();
        if (mode.equals("pertable"))
            return Template.SemesterMode.INCREMENT;
        else {
            int constant = Integer.valueOf(mode);
            Template.SemesterMode semesterMode = Template.SemesterMode.CONSTANT;
            semesterMode.setSemester(constant);
            return semesterMode;
        }
    }

    private static Template setTemplateProperties(Template template, Map map) {
        Map attributes;
        int col;
        String regex;
        
        attributes = (Map) map.get("name");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        template.setName(col, regex);
        
        attributes = (Map) map.get("code");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        template.setCode(col, regex);
        
        attributes = (Map) map.get("credits");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        template.setCredits(col, regex);
        
        attributes = (Map) map.get("prerequisites");
        col = castEntryToInt(attributes.get("col"));
        regex = attributes.get("regex").toString();
        template.setPrerequisites(col, regex);
        
        return template;
    }
}
