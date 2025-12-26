package studentapp.collection;

import studentapp.model.Student;
import studentapp.strategy.SortStrategy;

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

    public void sort(SortStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия сортировки не может быть null");
        }

        Student[] arrayToSort = this.toArray();
        strategy.sort(arrayToSort, size);

        for (int i = 0; i < size; i++) {
            this.students[i] = arrayToSort[i];
        }
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