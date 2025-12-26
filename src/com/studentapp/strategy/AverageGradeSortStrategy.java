package studentapp.strategy;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;
import java.util.*;

public class AverageGradeSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> s) { s.sort(Comparator.comparingDouble(Student::getAverageGrade).reversed()); }
    @Override
    public void sort(StudentCollection c) { sort(Arrays.asList(c.toArray())); }
    @Override
    public void sort(Student[] s, int sz) { Arrays.sort(s, 0, sz, Comparator.comparingDouble(Student::getAverageGrade).reversed()); }
    @Override
    public String getStrategyName() { return "По среднему баллу"; }
}