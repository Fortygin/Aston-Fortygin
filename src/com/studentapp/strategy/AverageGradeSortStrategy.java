package studentapp.strategy;
import studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class AverageGradeSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        // Сортировка по убыванию
        students.sort(Comparator.comparingDouble(Student::getAverageGrade).reversed());
    }

    @Override
    public String getStrategyName() {
        return "По среднему баллу";
    }
}
