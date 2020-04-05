package spring_interface;

import TargygrafPP.FakePDFReaderImplementation;
import TargygrafPP.PDFReader;
import TargygrafPP.Subject;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    List<Subject> subjects;
    
    @Autowired
    private ExternalProperties properties;
    
    @PostConstruct
    public void readSubjects() {
        Subject[] readSubjects = 
                new FakePDFReaderImplementation().readSubjects(properties.getPdfPath());

        subjects = List.of(readSubjects); //TODO: PDFReader returns Null elements!!!
    }
    
    @GetMapping("/getsubjects")
    Subject[] getSubjects() {
        return subjects.toArray(new Subject[0]);
    }
}
