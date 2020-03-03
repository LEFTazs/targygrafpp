package TargygrafPP;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSubject {
    private Subject subject;
    private String[] lecturers;
    private String semester;
    private RequirementType requirement;
    private int lectureHours;
    private int practiceHours;
    private int labHours;
    private boolean hasSignature;
    private short mark;
        
}

enum RequirementType {
    Exam,
    Colloquium,
    ContinuosExams
}