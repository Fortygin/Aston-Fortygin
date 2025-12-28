package studentapp;

import studentapp.model.Student;
import studentapp.collection.StudentCollection;
import studentapp.strategy.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;
import java.nio.file.*;
import java.util.concurrent.CountDownLatch;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$s] %5$s%n");
    }

    public static void main(String[] args) {
        StudentCollection collection = null;

        while (true) {
            System.out.println("\nМеню (выберите из предложенных вариантов)");
            System.out.println("1. Заполнить данные студентов");
            System.out.println("2. Вывести данные на экран");
            System.out.println("3. Отсортировать список");
            System.out.println("4. Записать коллекцию в файл");
            System.out.println("5. Подсчитать вхождения элементов");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                logger.info("Завершение работы программы.");
                break;
            }

            switch (choice) {
                case "1" -> collection = handleInputSelection();
                case "2" -> displayCollection(collection);
                case "3" -> handleSortSelection(collection);
                case "4" -> saveCollectionToFile(collection);
                case "5" -> handleCountOccurrences(collection);
                default -> logger.log(Level.WARNING, "Неверный ввод в меню: {0}", choice);
            }
        }
    }

    private static StudentCollection handleInputSelection() {
        System.out.println("\n1. Вручную\n2. Автоматическое заполнение (рандом)\n3. Из файла");
        System.out.print("Ваш выбор: ");

        String mode = scanner.nextLine();
        int count = getValidCount();

        StudentCollection collection = switch (mode) {
            case "1" -> fillManual(count);
            case "2" -> fillRandom(count);
            case "3" -> fillFromFile(count);
            default -> {
                logger.warning("Выбран неверный способ заполнения.");
                yield null;
            }
        };

        if (collection != null) {
            logger.log(Level.INFO, "Создана коллекция из {0} студентов.", collection.size());
        }
        return collection;
    }

    private static int getValidCount() {
        while (true) {
            try {
                System.out.print("Количество студентов: ");
                int count = Integer.parseInt(scanner.nextLine());
                if (count <= 0) throw new IllegalArgumentException("Число должно быть > 0");
                return count;
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "Ошибка формата: введено не число.");
            } catch (IllegalArgumentException e) {
                logger.log(Level.WARNING, "Валидация: {0}", e.getMessage());
            }
        }
    }

    private static StudentCollection fillManual(int count) {
        StudentCollection collection = new StudentCollection();
        for (int i = 0; i < count; i++) {
            System.out.println("\nСтудент №" + (i + 1));
            int group;
            double grade;
            String book;

            // Группа
            while (true) {
                try {
                    System.out.print("Группа (число больше 0): ");
                    group = Integer.parseInt(scanner.nextLine());
                    if (group <= 0) throw new IllegalArgumentException("Группа должна быть > 0");
                    break;
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Ошибка ввода группы: {0}", e.getMessage());
                }
            }

            // Балл
            while (true) {
                try {
                    System.out.print("Балл (0–5): ");
                    grade = Double.parseDouble(scanner.nextLine());
                    if (grade < 0 || grade > 5) throw new IllegalArgumentException("Балл должен быть от 0.0 до 5.0");
                    break;
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Ошибка ввода балла: {0}", e.getMessage());
                }
            }

            // Зачётка
            while (true) {
                try {
                    System.out.print("Зачётка (6 цифр): ");
                    book = scanner.nextLine();
                    if (!book.matches("\\d{6}")) throw new IllegalArgumentException("Нужно ровно 6 цифр!");
                    break;
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Ошибка ввода зачётной книжки: {0}", e.getMessage());
                }
            }

            Student student = new Student.StudentBuilder(group, grade, book).build();
            collection.add(student);
        }
        logger.log(Level.INFO, "Вручную добавлено студентов: {0}", collection.size());
        
        return collection;
    }

    private static StudentCollection fillFromFile(int count) {
        StudentCollection collection = new StudentCollection();
        try {
            List<Student> students = Files.lines(Paths.get("students.txt"))
                    .limit(count)
                    .map(line -> {
                        try {
                            String[] d = line.split(",");
                            return new Student.StudentBuilder(
                                    Integer.parseInt(d[0].trim()),
                                    Double.parseDouble(d[1].trim()),
                                    d[2].trim()).build();
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Битые данные в файле: {0}", line);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            
            collection.addAll(students);
            logger.info("Данные из файла успешно загружены через стримы.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Файл не найден или недоступен!", e);
        }
        return collection;
    }

    private static StudentCollection fillRandom(int count) {
        StudentCollection collection = new StudentCollection();
        List<Student> students = IntStream.range(0, count)
                .mapToObj(i -> new Student.StudentBuilder(
                        random.nextInt(50) + 1,
                        2.0 + 3.0 * random.nextDouble(),
                        String.format("%06d", random.nextInt(1000000))
                ).build())
                .collect(Collectors.toList());
        
        collection.addAll(students);
        logger.log(Level.INFO, "Сгенерировано случайных студентов через стримы: {0}", count);
        return collection;
    }

    private static void handleSortSelection(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            logger.warning("Попытка сортировки пустой коллекции.");
            return;
        }

        System.out.println("\nВыберите стратегию сортировки:");
        System.out.println("1. По номеру группы");
        System.out.println("2. По среднему баллу");
        System.out.println("3. По номеру зачётной книжки");
        System.out.println("4. По номеру группы (только чётные)");
        System.out.println("5. По среднему баллу (только чётные)");
        System.out.println("6. По номеру зачётной книжки (только чётные)");
        System.out.print("Ваш выбор: ");


        String strategy = scanner.nextLine();
        StudentComparator comparator = null;
        EvenSortStrategy evenStrategy = null;
        switch (strategy) {
            case "1" -> comparator = new GroupNumberSortStrategy();
            case "2" -> comparator = new AverageGradeSortStrategy();
            case "3" -> comparator = new RecordBookNumberSortStrategy();
            case "4" -> evenStrategy = new EvenGroupNumberSortStrategy();
            case "5" -> evenStrategy = new EvenAverageGradeSortStrategy();
            case "6" -> evenStrategy = new EvenRecordBookSortStrategy();
            default -> {
                System.out.println("Неверная стратегия.");
                return;
            }
        }

        if (comparator != null) {
            collection.sort(comparator);
            logger.log(Level.INFO, "Сортировка выполнена: {0}", comparator.getDescription());

            // Небольшая пауза для читаемости вывода
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            displayCollection(collection);
            promptAndSaveToFile(collection, comparator.getDescription());
        } else if (evenStrategy != null) {
            collection.sort(evenStrategy);
            logger.log(Level.INFO, "Сортировка чётных значений выполнена: {0}", evenStrategy.getDescription());

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            displayCollection(collection);
            promptAndSaveToFile(collection, evenStrategy.getDescription());
        } else {
            logger.warning("Стратегия не выбрана.");
        }
    }
    // Блок сохранения в файл для метода handleSortSelection
    private static void promptAndSaveToFile(StudentCollection collection, String strategyDescription) {
        while (true) {
            System.out.print("\nСохранить отсортированный результат в файл? (y/n): ");
            String saveChoice = scanner.nextLine().trim().toLowerCase();

            if (saveChoice.equals("y")) {
                saveCollectionToFile(collection, strategyDescription);
                break;
            } else if (saveChoice.equals("n")) {
                logger.info("Сохранение отменено.");
                break;
            } else {
                logger.warning("Неверный ввод. Введите 'y' или 'n'.");
            }
        }
    }

    private static void displayCollection(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("\nСодержимое коллекции:");
        for (int i = 0; i < collection.size(); i++) {
            System.out.println(collection.get(i));
        }
    }

    private static void saveCollectionToFile(StudentCollection collection, String strategyName) {
        if (collection == null || collection.isEmpty()) {
            logger.warning("Попытка сохранения пустой коллекции.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sorted_results.txt", true))) {
            writer.write("Запись от " + new java.util.Date() + " ---\n");
            writer.write("Стратегия: " + strategyName.replace("\n", " ") + "\n");
            for (int i = 0; i < collection.size(); i++) {
                writer.write(collection.get(i).toString() + "\n");
            }
            writer.write("\n\n");
            logger.info("Результаты успешно дописаны в файл sorted_results.txt");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при записи в файл: {0}", e.getMessage());
        }
    }

    // Обработка запроса подсчёта вхождений из меню
    private static void handleCountOccurrences(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        
        // Получаем все уникальные элементы
        List<Student> uniqueStudents = collection.stream()
                .distinct()
                .collect(Collectors.toList());
        
        System.out.println("\n=== Подсчёт вхождений элементов ===");
        System.out.println("Найдено уникальных элементов: " + uniqueStudents.size());
        
        long startTime = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(uniqueStudents.size());
        
        // Map для хранения результатов (Student -> количество вхождений)
        Map<Student, Long> results = new java.util.concurrent.ConcurrentHashMap<>();
        
        // Запускаем асинхронный многопоточный подсчёт для каждого элемента
        for (Student target : uniqueStudents) {
            collection.countOccurrencesMultithreadedAsync(target, count -> {
                results.put(target, count);
                latch.countDown();
            });
        }
        
        // Ждём завершения всех подсчётов
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Выводим все результаты после завершения всех потоков
        System.out.println("\nРезультаты подсчёта:");
        for (Map.Entry<Student, Long> entry : results.entrySet()) {
            System.out.println("Элемент: " + entry.getKey());
            System.out.println("Количество вхождений: " + entry.getValue());
            System.out.println();
        }
        
        System.out.println("==========================================\n");
    }

    // Дополнительный метод для сохранения без указания стратегии (например, из пункта меню 4)
    private static void saveCollectionToFile(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            logger.warning("Попытка сохранения пустой коллекции.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("collection_dump.txt", true))) {
            writer.write("Сохранение коллекции от " + new java.util.Date() + " ---\n");
            for (int i = 0; i < collection.size(); i++) {
                writer.write(collection.get(i).toString() + "\n");
            }
            writer.write("\n");
            logger.info("Коллекция успешно сохранена в файл collection_dump.txt");
            System.out.println("Данные сохранены в файл collection_dump.txt");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка при записи в файл: {0}", e.getMessage());
            System.out.println("Ошибка сохранения в файл.");
        }
    }
}