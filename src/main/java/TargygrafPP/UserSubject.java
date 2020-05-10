package TargygrafPP;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserSubject {
    private Subject subject;
    private String[] lecturers;
    private String attendedSemester;
    private boolean hasSignature;
    private int mark;
        
}

enum RequirementType {
    END_OF_SEMESTER_EXAM,
    MID_TERM_EXAMS,
    COLLOQUIUM,
    SIGNATURE
}