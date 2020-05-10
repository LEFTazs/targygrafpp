package spring_interface;

import TargygrafPP.FakePDFReaderImplementation;
import TargygrafPP.PDFReader;
import TargygrafPP.Subject;
import TargygrafPP.UserSubject;
import TargygrafPP.XLSXReader;
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
                new PDFReader().readSubjects(properties.getPdfPath());

        subjects = List.of(readSubjects); //TODO: PDFReader returns Null elements!!!
        readUserSubjects();
    }
    
    public void readUserSubjects(){
        Subject[] subjectList = subjects.toArray(new Subject[0]);
        String filePath = "C:\\Users\\kamul\\Documents\\programozas\\java\\targygrafpp_source_files\\export.xlsx";
        UserSubject[] userSubjects = new XLSXReader().readSubjects(filePath, subjectList);
    }
    
    @GetMapping("/getsubjects")
    Subject[] getSubjects() {
        return subjects.toArray(new Subject[0]);
    }
}
