package TargygrafPP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Subject {
    private String name;
    private String code;
    private int creditValue;
    private short semester; // A value of 0 would mean that it is a differential subject
    private List<String> prerequisites;
    
    public Subject(String name, String code, short semester, int creditValue) {
        this.name = name;
        this.code = code;
        this.creditValue = creditValue;
        this.semester = semester;
        this.prerequisites = new ArrayList<>();
    }
    
    public Subject(){
        this.name = "Default";
        this.code = null;
        this.creditValue = 0;
        this.semester = 0;
        this.prerequisites = null;
    }
    
    public void addPrerequisite(String prerequisite) {
        prerequisites.add(prerequisite);
    }
    
    public String[] getPrerequisites() {
        return prerequisites.toArray(new String[0]);
    }
    
    public void setPrerequisites(String[] prerequisites) {
        this.prerequisites = Arrays.asList(prerequisites);
    }
}
