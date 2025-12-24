package ProjectStudents.Strategy;

import ProjectStudents.Builder.Student;

import java.util.Arrays;

public class StudentCollection {
    private Student[] students;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public StudentCollection() {
        this.students = new Student[DEFAULT_CAPACITY];
        this.size = 0;
    }

    // Добавление студента
    public void add(Student student) {
        if (size >= students.length) {
            students = Arrays.copyOf(students, students.length * 2);
        }
        students[size++] = student;
    }

    // Получение размера
    public int size() {
        return size;
    }

    // Получение студента по индексу
    public Student get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return students[index];
    }

    public void sort(StudentSortStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия сортировки не может быть null");
        }
        strategy.sort(students, size); // Передаем оригинал и размер
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Преобразование в массив (для внешнего использования)
    public Student[] toArray() {
        return Arrays.copyOfRange(students, 0, size);
    }

    @Override
    public String toString() {
        return "ProjectStudents.Strategy.StudentCollection{" +
                "students=" + Arrays.toString(Arrays.copyOfRange(students, 0, size)) +
                ", size=" + size +
                '}';
    }
}