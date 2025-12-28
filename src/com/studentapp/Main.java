package studentapp;

import studentapp.model.Student;
import studentapp.collection.StudentCollection;
import studentapp.strategy.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.logging.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
            System.out.println("5. Подсчитать вхождения по критерию");
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
                case "5" -> performCountOccurrences(collection);
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

        Stream<Student> studentStream = Stream.generate(() -> {
                    System.out.println("\nСтудент №" + (collection.size() + 1));


                    int group = getValidInt("Группа (число > 0): ", val -> val > 0, "Группа должна быть > 0");
                    double grade = getValidDouble("Балл (0–5): ", val -> val >= 0 && val <= 5, "Балл должен быть от 0.0 до 5.0");
                    String book = getValidString("Зачётка (6 цифр): ", str -> str.matches("\\d{6}"), "Нужно ровно 6 цифр!");


                    return new Student.StudentBuilder(group, grade, book).build();
                })
                .limit(count);

        collection.addAll(studentStream);
        logger.log(Level.INFO, "Вручную добавлено студентов (через Stream): {0}", collection.size());
        return collection;
    }

    // Вспомогательные методы для валидации ввода
    private static int getValidInt(String prompt, IntPredicate validator, String errorMsg) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scanner.nextLine());
                if (validator.test(val)) return val;
                logger.warning(errorMsg);
            } catch (NumberFormatException e) {
                logger.warning("Введите целое число.");
            }
        }
    }

    private static double getValidDouble(String prompt, DoublePredicate validator, String errorMsg) {
        while (true) {
            try {
                System.out.print(prompt);
                double val = Double.parseDouble(scanner.nextLine());
                if (validator.test(val)) return val;
                logger.warning(errorMsg);
            } catch (NumberFormatException e) {
                logger.warning("Введите число.");
            }
        }
    }

    private static String getValidString(String prompt, Predicate<String> validator, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String str = scanner.nextLine();
            if (validator.test(str)) return str;
            logger.warning(errorMsg);
        }
    }


    private static StudentCollection fillFromFile(int count) {
        StudentCollection collection = new StudentCollection();

        try (Stream<String> lines = Files.lines(Paths.get("students.txt"))) {
            Stream<Student> studentStream = lines
                    .limit(count)  // берём не более count строк
                    .map(line -> {
                        try {
                            String[] d = line.split(",");
                            return new Student.StudentBuilder(
                                    Integer.parseInt(d[0].trim()),
                                    Double.parseDouble(d[1].trim()),
                                    d[2].trim()
                            ).build();
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Битые данные в строке: {0}", line);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull);  // убираем null из-за ошибок парсинга

            collection.addAll(studentStream);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Ошибка чтения файла: {0}", e.getMessage());
        }

        logger.info("Данные из файла загружены (через Stream).");
        return collection;
    }

    private static StudentCollection fillRandom(int count) {
        StudentCollection collection = new StudentCollection();


        Stream<Student> studentStream = IntStream.range(0, count)
                .mapToObj(i -> {
                    double rawGrade = 2.0 + 3.0 * random.nextDouble();
                    double roundedGrade = Math.round(rawGrade * 10) / 10.0;


                    return new Student.StudentBuilder(
                            random.nextInt(50) + 1,
                            roundedGrade,
                            String.format("%06d", random.nextInt(1000000))
                    ).build();
                });

        collection.addAll(studentStream);
        logger.log(Level.INFO, "Сгенерировано случайных студентов (через Stream): {0}", count);
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

    // многопоточный метод, подсчитывающий количество вхождений
    private static void performCountOccurrences(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста, подсчёт невозможен.");
            return;
        }

        System.out.println("\nВыберите критерий для подсчёта:");
        System.out.println("1. По номеру группы");
        System.out.println("2. По среднему баллу");
        System.out.println("3. По номеру зачётной книжки");
        System.out.print("Ваш выбор: ");

        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> countByGroup(collection);
            case "2" -> countByGrade(collection);
            case "3" -> countByBookNumber(collection);
            default -> System.out.println("Неверный выбор.");
        }
    }

    private static void countByGroup(StudentCollection collection) {
        System.out.print("Введите номер группы: ");
        try {
            int group = Integer.parseInt(scanner.nextLine());
            long count = collection.countOccurrences(s -> s.getGroupNumber() == group);
            System.out.printf("Найдено студентов в группе %d: %d%n", group, count);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите целое число.");
        }
    }

    private static void countByGrade(StudentCollection collection) {
        System.out.print("Введите средний балл: ");
        try {
            double grade = Double.parseDouble(scanner.nextLine());
            long count = collection.countOccurrences(s -> s.getAverageGrade() == grade);
            System.out.printf("Найдено студентов с баллом %.1f: %d%n", grade, count);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введите число.");
        }
    }

    private static void countByBookNumber(StudentCollection collection) {
        System.out.print("Введите номер зачётной книжки: ");
        String bookNumber = scanner.nextLine();
        long count = collection.countOccurrences(s -> s.getRecordBookNumber().equals(bookNumber));
        System.out.printf("Найдено студентов с зачёткой %s: %d%n", bookNumber, count);
    }
}