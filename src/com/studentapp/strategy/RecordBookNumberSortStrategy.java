package studentapp.strategy;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;
import java.util.*;

public class RecordBookNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> s) { s.sort(Comparator.comparing(Student::getRecordBookNumber)); }
    @Override
    public void sort(StudentCollection c) { sort(Arrays.asList(c.toArray())); }
    @Override
    public void sort(Student[] s, int sz) { Arrays.sort(s, 0, sz, Comparator.comparing(Student::getRecordBookNumber)); }
    @Override
    public String getStrategyName() { return "По номеру зачетки"; }
}