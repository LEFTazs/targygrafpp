package spring_interface;

import TargygrafPP.FakePDFReaderImplementation;
import TargygrafPP.Subject;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    List<Subject> subjects;
    String pdfPath;
    
    @PostConstruct
    public void readSubjects() {
        Subject[] readSubjects = 
                new FakePDFReaderImplementation().readSubjects(pdfPath);
        subjects = List.of(readSubjects);
    }
    
    @GetMapping("/getsubjects")
    Subject[] getSubjects() {
        return subjects.toArray(new Subject[0]);
    }
}
