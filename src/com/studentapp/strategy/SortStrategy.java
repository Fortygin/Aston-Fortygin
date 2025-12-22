package studentapp.strategy;
import studentapp.model.Student;
import java.util.List;

public interface SortStrategy {
    void sort(List<Student> students);
    String getStrategyName();
}



