package studentapp.collection;

import studentapp.model.Student;
import studentapp.strategy.EvenSortStrategy;
import studentapp.strategy.StudentComparator;
import java.util.function.Predicate;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.stream.Stream;

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

    public void addAll(Stream<Student> studentStream) {
        if (studentStream != null) {
            studentStream.forEach(this::add);
        }
    }

    public void addAll(java.util.Collection<Student> studentsToAdd) {
        if (studentsToAdd != null) {
            for (Student student : studentsToAdd) {
                add(student);
            }
        }
    }

    public long countOccurrences(Predicate<Student> predicate) {
        if (predicate == null || isEmpty()) return 0;
        return stream().filter(predicate).count();
    }

    public long countOccurrences(Object o) {
        if (o == null || isEmpty()) return 0;
        return stream().filter(s -> s.equals(o)).count();
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Student get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс вне диапазона: " + index);
        }
        return students[index];
    }

    public Stream<Student> stream() {
        return Arrays.stream(students, 0, size);
    }

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

    public void sort(EvenSortStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Стратегия сортировки не может быть null");
        }
        strategy.sort(students, size);
    }

    public Student[] toArray() {
        return Arrays.copyOfRange(students, 0, size);
    }

    /**
     * Асинхронный многопоточный метод подсчёта вхождений
     */
    public void countOccurrencesMultithreadedAsync(Student target, java.util.function.Consumer<Long> callback) {
        if (target == null) {
            throw new IllegalArgumentException("Целевой элемент не может быть null");
        }
        if (isEmpty()) {
            callback.accept(0L);
            return;
        }

        int numThreads = Runtime.getRuntime().availableProcessors();
        int chunkSize = Math.max(1, size / numThreads);

        final int targetHashCode = target.hashCode();
        final long[] results = new long[numThreads];
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? size : (i + 1) * chunkSize;

            new Thread(() -> {
                long count = 0;
                for (int j = start; j < end && j < size; j++) {
                    Student current = students[j];
                    if (current != null && current.hashCode() == targetHashCode && current.equals(target)) {
                        count++;
                    }
                }
                results[threadIndex] = count;
                latch.countDown();
            }).start();
        }

        new Thread(() -> {
            try {
                latch.await();
                long totalCount = 0;
                for (long result : results) {
                    totalCount += result;
                }
                callback.accept(totalCount);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                callback.accept(0L);
            }
        }).start();
    }

    @Override
    public String toString() {
        return "Коллекция студентов: " +
                Arrays.toString(Arrays.copyOfRange(students, 0, size)) +
                ", размер: " + size;
    }
}