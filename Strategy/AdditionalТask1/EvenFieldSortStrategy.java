package ProjectStudents.Strategy.AdditionalТask1;

import ProjectStudents.Builder.Student;
import ProjectStudents.Strategy.StudentSortStrategy;

public abstract class EvenFieldSortStrategy implements StudentSortStrategy {

    protected abstract int getSortField(Student student);

    @Override
    public final void sort(Student[] students, int size) {
        if (students == null || size <= 1) return;

        // Двойной цикл: внешний — «первый» элемент, внутренний — «второй» для сравнения
        for (int first = 0; first < size; first++) {
            for (int second = first + 1; second < size; second++) {
                // Проверяем, что оба студента существуют и имеют ЧЁТНЫЕ значения поля
                if (students[first] != null && students[second] != null &&
                        isEven(getSortField(students[first])) && isEven(getSortField(students[second]))) {

                    // Если значение у «первого» больше, чем у «второго» — меняем их местами
                    if (getSortField(students[first]) > getSortField(students[second])) {
                        Student temp = students[first];
                        students[first] = students[second];
                        students[second] = temp;
                    }
                }
            }
        }
    }

    private boolean isEven(int value) {
        return (value % 2) == 0;
    }
}
