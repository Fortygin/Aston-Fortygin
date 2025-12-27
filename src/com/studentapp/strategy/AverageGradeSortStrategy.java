package studentapp.strategy;

import studentapp.model.Student;

public class AverageGradeSortStrategy implements StudentComparator {
    @Override
    public int compare(Student s1, Student s2) {
        return Double.compare(s1.getAverageGrade(), s2.getAverageGrade());
    }

    @Override
    public String getDescription() {
        return "Сортировка по среднему баллу (по возрастанию)";
    }
}