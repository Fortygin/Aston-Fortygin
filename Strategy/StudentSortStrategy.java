package ProjectStudents.Strategy;

import ProjectStudents.Builder.Student;

public interface StudentSortStrategy {
    public void sort(Student[] students, int size);
    String getStrategyName();
}