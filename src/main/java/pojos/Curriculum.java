package pojos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * This class encloses the collected subjects and also stores metadata
 * about the curriculum (like "name" for example).
 */
@AllArgsConstructor
@Getter
public class Curriculum {
    private String id;
    private String name;
    private Subject[] subjects;
}
