package TargygrafPP;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Curriculum {
    private String id;
    private String name;
    private Subject[] subjects;
}
