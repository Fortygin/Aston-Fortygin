package studentapp.strategy;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;
import java.util.List;

public abstract class EvenFieldSortStrategy implements SortStrategy {
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

    @Override
    public void sort(List<Student> students) {
        if (students == null || students.isEmpty()) return;

        Student[] arr = students.toArray(new Student[0]);
        sort(arr, arr.length);

        for (int i = 0; i < arr.length; i++) {
            students.set(i, arr[i]);
        }
    }

    @Override
    public void sort(StudentCollection collection) {
        if (collection == null) return;
        sort(collection.toArray(), collection.size());
    }

    private boolean isEven(int val) {
        return val % 2 == 0;
    }
}