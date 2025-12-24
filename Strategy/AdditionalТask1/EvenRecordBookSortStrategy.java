package ProjectStudents.Strategy.AdditionalТask1;

import ProjectStudents.Builder.Student;
import ProjectStudents.Strategy.StudentSortStrategy;


public class EvenRecordBookSortStrategy extends EvenFieldSortStrategy implements StudentSortStrategy {

    @Override
    protected int getSortField(Student student) {
        try {
            // Преобразуем строку в число
            return Integer.parseInt(student.getRecordBookNumber());
        } catch (NumberFormatException e) {
            // Если строка не числовая — возвращаем 0 (можно выбрать другое значение по умолчанию)
            System.err.println("Некорректный номер зачётной книжки: " + student.getRecordBookNumber());
            return 0;
        }
    }
    @Override
    public String getStrategyName() {
        return "По номеру зачетной книжки\nСортируем только чётные значения";
    }
}
