package studentapp.strategy;

import studentapp.model.Student;

public interface StudentComparator extends SortStrategy {
    int compare(Student s1, Student s2);
}