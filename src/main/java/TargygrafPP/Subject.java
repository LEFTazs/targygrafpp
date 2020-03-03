package TargygrafPP;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subject {
    private String name;
    private String code;
    private short creditValue;
    private List<Subject> prerequisites;
    
    public Subject(String name, String code, short creditValue) {
        this.name = name;
        this.code = code;
        this.creditValue = creditValue;
        this.prerequisites = new ArrayList<>();
    }
    
    public Subject[] getPrerequisites() {
        return prerequisites.toArray(new Subject[0]);
    }
}
