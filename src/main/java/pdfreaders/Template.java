package pdfreaders;

import java.util.List;
import java.util.regex.Pattern;
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
        private Pattern regex;
        private Pattern headerRegex;

        public Property(int collumn, String regex, String headerRegex) {
            this.collumn = collumn;
            this.regex = Pattern.compile(regex);
            this.headerRegex = Pattern.compile(headerRegex);
        }
    }
    
    public enum SemesterMode {
        INCREMENT {
            final private String errorMessage = "INCREMENT has no semester field";
            @Override
            public short getSemester() {
                throw new UnsupportedOperationException(errorMessage);
            }
            @Override
            public void setSemester(short semester) {
                throw new UnsupportedOperationException(errorMessage);
            }
        },
        CONSTANT {
            private short semester;

            @Override
            public short getSemester() {
                return semester;
            }
            @Override
            public void setSemester(short semester) {
                this.semester = semester;
            }
        };
        abstract public short getSemester();
        abstract public void setSemester(short semester);
    }
    
    public void setName(int collumn, String regex, String headerRegex) {
        this.name = new Property(collumn, regex, headerRegex);
    }
    
    public void setCode(int collumn, String regex, String headerRegex) {
        this.code = new Property(collumn, regex, headerRegex);
    }
    
    public void setCredits(int collumn, String regex, String headerRegex) {
        this.credits = new Property(collumn, regex, headerRegex);
    }
    
    public void setPrerequisites(int collumn, String regex, String headerRegex) {
        this.prerequisites = new Property(collumn, regex, headerRegex);
    }
    
    public int getNameCollumn() {
        return this.name.getCollumn();
    }
    
    public int getCodeCollumn() {
        return this.code.getCollumn();
    }
    
    public int getCreditsCollumn() {
        return this.credits.getCollumn();
    }
    
    public int getPrerequisitesCollumn() {
        return this.prerequisites.getCollumn();
    }
    
    public Pattern getNameRegex() {
        return this.name.getRegex();
    }
    
    public Pattern getCodeRegex() {
        return this.code.getRegex();
    }
    
    public Pattern getCreditsRegex() {
        return this.credits.getRegex();
    }
    
    public Pattern getPrerequisitesRegex() {
        return this.prerequisites.getRegex();
    }
    
    public Pattern getNameHeaderRegex() {
        return this.name.getHeaderRegex();
    }
    
    public Pattern getCodeHeaderRegex() {
        return this.code.getHeaderRegex();
    }
    
    public Pattern getCreditsHeaderRegex() {
        return this.credits.getHeaderRegex();
    }
    
    public Pattern getPrerequisitesHeaderRegex() {
        return this.prerequisites.getHeaderRegex();
    }
}
