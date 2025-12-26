package studentapp.strategy;

import studentapp.model.Student;

public interface StudentSortStrategy {
    void sort(Student[] students, int size);
    String getStrategyName();
}