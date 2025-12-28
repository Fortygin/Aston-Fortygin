package studentapp;

import studentapp.collection.StudentCollection;

import java.util.Scanner;

public class SearchHandler {
    private static final Scanner scanner = new Scanner(System.in);

    // многопоточный метод, подсчитывающий количество вхождений
    static void performCountOccurrences(StudentCollection collection) {
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