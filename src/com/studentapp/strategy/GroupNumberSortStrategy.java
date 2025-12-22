package studentapp.strategy;
import studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class GroupNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        students.sort(Comparator.comparing(Student::getGroupNumber));
    }

    @Override
    public String getStrategyName() {
        return "По номеру группы";
    }
}
