package studentapp.strategy;
import studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class RecordBookNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        students.sort(Comparator.comparing(Student::getRecordBookNumber));
    }

    @Override
    public String getStrategyName() {
        return "По номеру зачетной книжки";
    }
}
