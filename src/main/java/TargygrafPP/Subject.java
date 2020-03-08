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
    private int creditValue;
    private short semester;
    private List<Subject> prerequisites;
    
    public Subject(String name, String code, short semester, int creditValue) {
        this.name = name;
        this.code = code;
        this.creditValue = creditValue;
        this.semester = semester;
        this.prerequisites = new ArrayList<>();
    }
    
    public void addPrerequisite(Subject prerequisite) {
        prerequisites.add(prerequisite);
    }
    
    public Subject[] getPrerequisites() {
        return prerequisites.toArray(new Subject[0]);
    }
}
