package studentapp.strategy;

import studentapp.model.Student;

public class EvenAverageGradeSortStrategy extends EvenFieldSortStrategy {
    @Override
    protected int getSortField(Student s) { return (int) Math.round(s.getAverageGrade()); }
    @Override
    public String getStrategyName() { return "Четные средние баллы"; }
}