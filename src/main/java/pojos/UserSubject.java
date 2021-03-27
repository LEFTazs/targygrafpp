package pojos;

import lombok.Getter;
import lombok.Setter;

/**
 * This class holds the subject element and adds more information to it.
 * When curriculum is extended with data from Neptun, this class can be
 * created with the additional information, that is specific to the user.
 */
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

/**
 * The types of subject requirements.
 */
enum RequirementType {
    Exam,
    Colloquium,
    ContinuosExams
}