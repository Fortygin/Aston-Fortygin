package studentapp;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.logging.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// создание коллекции разными способами
public class InputHandler {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static final Logger logger = Logger.getLogger(InputHandler.class.getName());

    public static StudentCollection handleInputSelection() {
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

        try (Stream<String> lines = Files.lines(Paths.get("students_valid.txt"))) {
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
}