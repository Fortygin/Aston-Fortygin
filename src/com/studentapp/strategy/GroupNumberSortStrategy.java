package studentapp.strategy;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;
import java.util.*;

public class GroupNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> s) {
        s.sort(Comparator.comparingInt(Student::getGroupNumber));
    }

    @Override
    public void sort(StudentCollection c) {
        // ВМЕСТО прямой сортировки массива здесь,
        // мы вызываем метод sort САМОЙ коллекции.
        // Это передает управление в StudentCollection.sort(strategy)
        c.sort(this);
    }

    @Override
    public void sort(Student[] s, int sz) {
        Arrays.sort(s, 0, sz, Comparator.comparingInt(Student::getGroupNumber));
    }

    @Override
    public String getStrategyName() { return "По номеру группы"; }
}