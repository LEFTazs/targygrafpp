package spring_interface;

import TargygrafPP.Curriculum;
import pdfreaders.PDFReader;
import TargygrafPP.Subject;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pdfreaders.CurriculumReader;

@RestController
public class Controller {
    private Curriculum[] curriculums;
    
    @Autowired
    private ExternalProperties properties;
    
    @PostConstruct
    public void readSubjects() {
        String pdfAndTemplateFolder = properties.getPdfAndTemplateFolder();
        CurriculumReader curriculumReader = new CurriculumReader();
        curriculumReader.read(pdfAndTemplateFolder);
        curriculums = curriculumReader.getCurriculums();
    }
    
    @GetMapping("/getcurriculum/{id}")
    Curriculum getCurriculum(@PathVariable("id") String id) {
        for (Curriculum curriculum : curriculums)
            if (curriculum.getId().equals(id))
                return curriculum;
        throw new IllegalArgumentException("Curriculum with given id not found.");
    }
    
    @GetMapping("/getallcurriculumids")
    String[] getAllCurriculumIds() {
        String[] curriculumIds = new String[curriculums.length];
        for (int i = 0; i < curriculumIds.length; i++)
            curriculumIds[i] = curriculums[i].getId();
        return curriculumIds;
    }
    
    @GetMapping("/getallcurriculumnames")
    String[] getAllCurriculumNames() {
        String[] curriculumNames = new String[curriculums.length];
        for (int i = 0; i < curriculumNames.length; i++)
            curriculumNames[i] = curriculums[i].getName();
        return curriculumNames;
    }
}
