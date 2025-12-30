package studentapp;

import studentapp.collection.StudentCollection;
import studentapp.model.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.logging.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class InputHandler {

    private static final Scanner scanner = new Scanner(System.in);
    static final Random random = new Random();
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
                logger.info("Выбран неверный способ заполнения.");
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
                logger.log(Level.INFO, "Валидация: {0}", e.getMessage());
            }
        }
    }

    private static StudentCollection fillManual(int count) {
        StudentCollection collection = new StudentCollection();
        // Используем IntStream, чтобы видеть номер текущего студента
        List<Student> students = IntStream.range(0, count)
                .mapToObj(i -> {
                    System.out.println("\nСтудент №" + (i + 1));
                    int group = getValidInt("Группа (число > 0): ", val -> val > 0, "Группа должна быть > 0");
                    double grade = getValidDouble("Балл (0–5): ", val -> val >= 0 && val <= 5, "Балл должен быть от 0.0 до 5.0");
                    String book = getValidString("Зачётка (6 цифр): ", str -> str.matches("\\d{6}"), "Нужно ровно 6 цифр!");
                    return new Student.StudentBuilder(group, grade, book).build();
                })
                .toList();

        collection.addAll(students.stream());
        logger.log(Level.INFO, "Вручную добавлено студентов: {0}", collection.size());
        return collection;
    }

    private static int getValidInt(String prompt, IntPredicate validator, String errorMsg) {
        while (true) {
            try {
                System.out.print(prompt);
                int val = Integer.parseInt(scanner.nextLine());
                if (validator.test(val)) return val;
                logger.info(errorMsg);
            } catch (NumberFormatException e) {
                logger.info("Введите целое число.");
            }
        }
    }

    private static double getValidDouble(String prompt, DoublePredicate validator, String errorMsg) {
        while (true) {
            try {
                System.out.print(prompt);
                double val = Double.parseDouble(scanner.nextLine());
                if (validator.test(val)) return val;
                logger.info(errorMsg);
            } catch (NumberFormatException e) {
                logger.info("Введите число.");
            }
        }
    }

    private static String getValidString(String prompt, Predicate<String> validator, String errorMsg) {
        while (true) {
            System.out.print(prompt);
            String str = scanner.nextLine();
            if (validator.test(str)) return str;
            logger.info(errorMsg);
        }
    }

    private static StudentCollection fillFromFile(int count) {
        StudentCollection collection = new StudentCollection();
        String fileName = "src/students_valid.txt"; // Проверьте, что файл в папке src!
        java.nio.file.Path path = java.nio.file.Paths.get(fileName);

        try (Stream<String> lines = Files.lines(path)) {
            List<Student> studentList = lines
                    .limit(count)
                    .map(line -> {
                        try {
                            String[] d = line.split(",");
                            return new Student.StudentBuilder(
                                    Integer.parseInt(d[0].trim()),
                                    Double.parseDouble(d[1].trim()),
                                    d[2].trim()
                            ).build();
                        } catch (Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

            collection.addAll(studentList.stream());
            logger.info("Данные загружены из " + fileName);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Файл не найден! Искал тут: {0}", path.toAbsolutePath());
        }
        return collection;
    }

    private static StudentCollection fillRandom(int count) {
        StudentCollection collection = new StudentCollection();
        List<Student> students = IntStream.range(0, count)
                .mapToObj(i -> {
                    double rawGrade = 2.0 + 3.0 * random.nextDouble();
                    double roundedGrade = Math.round(rawGrade * 10) / 10.0;
                    return new Student.StudentBuilder(
                            random.nextInt(50) + 1,
                            roundedGrade,
                            String.format("%06d", random.nextInt(1000000))
                    ).build();
                })
                .toList();

        collection.addAll(students.stream());
        logger.log(Level.INFO, "Сгенерировано случайных студентов: {0}", count);
        return collection;
    }
}