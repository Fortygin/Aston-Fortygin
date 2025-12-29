package studentapp.collection;

import studentapp.model.Student;
import studentapp.strategy.EvenSortStrategy;
import studentapp.strategy.StudentComparator;

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

    // Метод для получения стрима из коллекции
    public java.util.stream.Stream<Student> stream() {
        return java.util.Arrays.stream(students, 0, size);
    }
    
    // Метод для добавления всех элементов из коллекции
    public void addAll(java.util.Collection<Student> studentsToAdd) {
        for (Student student : studentsToAdd) {
            add(student);
        }
    }

    // Асинхронный многопоточный метод подсчёта вхождений элемента N
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
        
        // Вычисляем hashCode целевого элемента один раз для оптимизации
        final int targetHashCode = target.hashCode();
        
        // Массив для хранения результатов из каждого потока
        final long[] results = new long[numThreads];
        final Thread[] threads = new Thread[numThreads];
        final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(numThreads);

        // Создаём и запускаем потоки для параллельного подсчёта
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;
            final int start = i * chunkSize;
            final int end = (i == numThreads - 1) ? size : (i + 1) * chunkSize;
            
            threads[i] = new Thread(() -> {
                long count = 0;
                for (int j = start; j < end && j < size; j++) {
                    Student current = students[j];
                    // Проверяем на null и сравниваем
                    if (current != null && current.hashCode() == targetHashCode && current.equals(target)) {
                        count++;
                    }
                }
                results[threadIndex] = count;
                latch.countDown();
            });
            threads[i].start();
        }

        // Запускаем поток для ожидания завершения всех потоков и вызова callback
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
                "Студенты " + Arrays.toString(Arrays.copyOfRange(students, 0, size)) +
                ", размер коллекции: " + size;
    }
}