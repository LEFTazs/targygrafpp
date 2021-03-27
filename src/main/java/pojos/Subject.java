package pojos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Information about a given subject is stored in this class.
 * After the extraction from the curriculum files is done, the extracted
 * information about one subject will be stored in this class.
 * To represent differential subjects, give the semester field a value of 0.
 * The prerequisites field stores a list of strings, which are the codes of
 * other Subject objects. These are the prerequisites of this subject.
 */
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
