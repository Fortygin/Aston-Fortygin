package ProjectStudents.Strategy.AdditionalТask1;

import ProjectStudents.Builder.Student;

public class EvenAverageGradeSortStrategy extends EvenFieldSortStrategy{
    @Override
    protected int getSortField(Student student) {
        return (int) Math.round(student.getAverageGrade());
    }
    @Override
    public String getStrategyName() {
        return "По среднему баллу\nСортируем только чётные значения";
    }
}
