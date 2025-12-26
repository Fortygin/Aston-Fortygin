package studentapp.strategy;

import studentapp.model.Student;

public class EvenGroupNumberSortStrategy extends EvenFieldSortStrategy{
    @Override
    protected int getSortField(Student student) {
        return student.getGroupNumber();
    }
    @Override
    public String getStrategyName() {
        return "По номеру группы\nСортируем только чётные значения";
    }
}