package studentapp.strategy;

import studentapp.model.Student;

public interface EvenSortStrategy extends SortStrategy{
    void sort(Student[] students, int size);
}