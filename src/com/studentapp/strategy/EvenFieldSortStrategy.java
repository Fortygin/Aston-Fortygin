package studentapp.strategy;

import studentapp.model.Student;

public abstract class EvenFieldSortStrategy implements EvenSortStrategy {
    protected abstract int getSortField(Student student);

    @Override
    public final void sort(Student[] students, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (isEven(getSortField(students[i])) && isEven(getSortField(students[j]))) {
                    if (getSortField(students[i]) > getSortField(students[j])) {
                        Student temp = students[i];
                        students[i] = students[j];
                        students[j] = temp;
                    }
                }
            }
        }
    }

    private boolean isEven(int val) {
        return val % 2 == 0;
    }
}