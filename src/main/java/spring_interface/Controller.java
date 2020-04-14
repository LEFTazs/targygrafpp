package spring_interface;

import pdfreaders.PDFReader;
import TargygrafPP.Subject;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pdfreaders.Template;
import pdfreaders.TemplateReader;

@RestController
public class Controller {
    Subject[] subjects;
    
    @Autowired
    private ExternalProperties properties;
    
    @PostConstruct
    public void readSubjects() {
        Template[] templates = TemplateReader.read(properties.getTemplatePath());
        subjects = new PDFReader().readSubjects(properties.getPdfPath(), templates);
    }
    
    @GetMapping("/getsubjects")
    Subject[] getSubjects() {
        return subjects;
    }
}
