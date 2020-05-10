package TargygrafPP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Calculator {
    
    private int userCredits;
    private List<Subject> completedSubjects;
    private List<Subject> remainingSubjects;
    
    private List<UserSubject> userSubjects;
    private List<Subject> departmentSubjects;
    
    private float finalWeightedAverage;
    private float finalCreditIndex;
    
    private List<Float> weightedAveragePerSemester;
    private List<Float> creditIndexPerSemester;
    
    private Map<Integer, List<Integer>> marksPerSemester;
    
    public Calculator(UserSubject[] userSubjects, Subject[] departmentSubjects){
        this.userSubjects = Arrays.asList(userSubjects);
        this.departmentSubjects = Arrays.asList(departmentSubjects);
    }
   
    
    public void init(){
        initVariables();
        calculateAverages();
    }

    private void initVariables() {
        for(UserSubject s : userSubjects){
            if(s.getMark() != 0)
                completedSubjects.add(s.getSubject());
        }
        
        for(Subject s : departmentSubjects){
            if(!completedSubjects.contains(s)){
                remainingSubjects.add(s);
            }
        }
        userCredits = 0;
        for(Subject s : departmentSubjects){
            userCredits += s.getCreditValue();
        }
        weightedAveragePerSemester = new ArrayList<>();
        creditIndexPerSemester = new ArrayList<>();
        marksPerSemester = new HashMap<>();
        
    }
    
    private void calculateAverages(){
        findSemesterFromDate
        for(UserSubject us : userSubjects){
            int semester = findSemesterFromDate(us.getAttendedSemester());
            marksPerSemester.put(us.getAttendedSemester(), us.getMark());
            
        }
        
        
    }

    private void findSemesterFromDate(String attendedSemester) {
        
    }
    
}
