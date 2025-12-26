package studentapp.strategy;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;
import java.util.List;

public interface SortStrategy extends StudentSortStrategy {
    void sort(List<Student> students);

    void sort(StudentCollection collection);

    String getStrategyName();
}