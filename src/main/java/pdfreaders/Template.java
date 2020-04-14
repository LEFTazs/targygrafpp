package pdfreaders;

import java.util.List;
import jj2000.j2k.NotImplementedError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class Template {
    @Getter @Setter private List<Integer> pages;
    @Getter @Setter private int collumnSize;
    @Getter @Setter private SemesterMode semesterMode;
    private Property name;
    private Property code;
    private Property credits;
    private Property prerequisites;
    
    public Template(List<Integer> pages, int collumnSize, SemesterMode semesterMode) {
        this.pages = pages;
        this.collumnSize = collumnSize;
        this.semesterMode = semesterMode;
    }
    
    @Getter
    @Setter
    @AllArgsConstructor
    @ToString
    private class Property {
        private int collumn;
        private String regex;
    }
    
    public enum SemesterMode {
        INCREMENT {
            final private String errorMessage = "INCREMENT has no semester field";
            @Override
            public int getSemester() {
                throw new NotImplementedError(errorMessage);
            }
            @Override
            public void setSemester(int semester) {
                throw new NotImplementedError(errorMessage);
            }
        },
        CONSTANT {
            private int semester;

            @Override
            public int getSemester() {
                return semester;
            }
            @Override
            public void setSemester(int semester) {
                this.semester = semester;
            }
        };
        abstract public int getSemester();
        abstract public void setSemester(int semester);
    }
    
    public void setName(int page, String regex) {
        this.name = new Property(page, regex);
    }
    
    public void setCode(int page, String regex) {
        this.code = new Property(page, regex);
    }
    
    public void setCredits(int page, String regex) {
        this.credits = new Property(page, regex);
    }
    
    public void setPrerequisites(int page, String regex) {
        this.prerequisites = new Property(page, regex);
    }
    
    public int getNamePage() {
        return this.name.getCollumn();
    }
    
    public int getCodePage() {
        return this.code.getCollumn();
    }
    
    public int getCreditsPage() {
        return this.credits.getCollumn();
    }
    
    public int getPrerequisitesPage() {
        return this.prerequisites.getCollumn();
    }
    
    public String getNameRegex() {
        return this.name.getRegex();
    }
    
    public String getCodeRegex() {
        return this.code.getRegex();
    }
    
    public String getCreditsRegex() {
        return this.credits.getRegex();
    }
    
    public String getPrerequisitesRegex() {
        return this.prerequisites.getRegex();
    }
}
