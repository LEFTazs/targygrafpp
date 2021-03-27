package spring_interface;

import pojos.Curriculum;
import pdfreaders.PDFReader;
import pojos.Subject;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pdfreaders.CurriculumReader;

/**
 * Spring controller, that handles REST requests.
 */
@RestController
public class Controller {
    private Curriculum[] curriculums;
    
    @Autowired
    private ExternalProperties properties;
    
    /**
     * Using the configured folderpath, this extracts all found curriculums.
     * The folderpath can be configured in application.yaml, in the resources
     * folder. This is method runs immidietly after construction.
     */
    @PostConstruct
    public void readSubjects() {
        String pdfAndTemplateFolder = properties.getPdfAndTemplateFolder();
        CurriculumReader curriculumReader = new CurriculumReader();
        curriculumReader.read(pdfAndTemplateFolder);
        curriculums = curriculumReader.getCurriculums();
    }
    
    /**
     * GET endpoint, returns curriculum with provided id.
     * @param id Curriculum id to get.
     * @return Curriculum with provided id
     * @throws IllegalArgumentException Curriculum with given id not found
     */
    @GetMapping("/getcurriculum/{id}")
    Curriculum getCurriculum(@PathVariable("id") String id) {
        for (Curriculum curriculum : curriculums)
            if (curriculum.getId().equals(id))
                return curriculum;
        throw new IllegalArgumentException("Curriculum with given id not found.");
    }
    
    /**
     * GET endpoint, retrieves all avalaible curriculum ids.
     * @return List of avalaible curriculum ids
     */
    @GetMapping("/getallcurriculumids")
    String[] getAllCurriculumIds() {
        String[] curriculumIds = new String[curriculums.length];
        for (int i = 0; i < curriculumIds.length; i++)
            curriculumIds[i] = curriculums[i].getId();
        return curriculumIds;
    }
    
    /**
     * GET endpoint, retrieves every curriculum's pretty name.
     * @return List of curriculum pretty names
     */
    @GetMapping("/getallcurriculumnames")
    String[] getAllCurriculumNames() {
        String[] curriculumNames = new String[curriculums.length];
        for (int i = 0; i < curriculumNames.length; i++)
            curriculumNames[i] = curriculums[i].getName();
        return curriculumNames;
    }
}
