package studentapp.collection;

import studentapp.model.Student;
import studentapp.strategy.EvenSortStrategy;
import studentapp.strategy.StudentComparator;

import java.util.Arrays;

public class StudentCollection {
    private Student[] students;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public StudentCollection() {
        this.students = new Student[DEFAULT_CAPACITY];
        this.size = 0;
    }

    public void add(Student student) {
        if (size >= students.length) {
            students = Arrays.copyOf(students, students.length * 2);
        }
        students[size++] = student;
    }

    public int size() {
        return size;
    }

    public Student get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return students[index];
    }

    // обычная сортировка с помощью компаратора
    public void sortWithComparator(StudentComparator comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Компаратор не может быть null");
        }
        if (size <= 1) return;

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (comparator.compare(students[j], students[j + 1]) > 0) {
                    swap(j, j + 1);
                }
            }
        }
    }

    private void swap(int i, int j) {
        Student temp = students[i];
        students[i] = students[j];
        students[j] = temp;
    }

    public void sort(StudentComparator comparator) {
        sortWithComparator(comparator);
    }

    // сортировка чётных полей
    public void sort(EvenSortStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия сортировки не может быть null");
        }
        strategy.sort(students, size);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Student[] toArray() {
        return Arrays.copyOfRange(students, 0, size);
    }

    @Override
    public String toString() {
        return "Коллекция студентов: " +
                "Студенты " + Arrays.toString(Arrays.copyOfRange(students, 0, size)) +
                ", размер коллекции: " + size;
    }
}