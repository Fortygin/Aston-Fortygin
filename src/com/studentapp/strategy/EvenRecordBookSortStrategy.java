package studentapp.strategy;

import studentapp.model.Student;


public class EvenRecordBookSortStrategy extends EvenFieldSortStrategy {

    @Override
    protected int getSortField(Student student) {
        if (student == null || student.getRecordBookNumber() == null) {
            return 0;
        }
        try {
            // Преобразуем строку зачетки в число для проверки на четность
            return Integer.parseInt(student.getRecordBookNumber());
        } catch (NumberFormatException e) {
            // В случае ошибки формата (если в зачетке не только цифры)
            return 0;
        }
    }

    @Override
    public String getDescription() {
        return "По номеру зачетной книжки\nСортируем только чётные значения";
    }
}