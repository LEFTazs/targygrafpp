package spring_interface;

import pdfreaders.FakePDFReaderImplementation;
import pdfreaders.PDFReader;
import TargygrafPP.Subject;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pdfreaders.TemplateReader;

@RestController
public class Controller {
    Subject[] subjects;
    
    @Autowired
    private ExternalProperties properties;
    
    @PostConstruct
    public void readSubjects() {
        TemplateReader.read(properties.getTemplatePath());
        subjects = new PDFReader().readSubjects(properties.getPdfPath());
    }
    
    @GetMapping("/getsubjects")
    Subject[] getSubjects() {
        return subjects;
    }
}
