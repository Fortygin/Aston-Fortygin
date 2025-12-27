package studentapp;

import studentapp.model.Student;
import studentapp.collection.StudentCollection;
import studentapp.strategy.*;

public class StudentTests {

    public static void main(String[] args) {
        System.out.println("Тесты: ");
        testStudentBuilderAndValidation();
        testCollectionFunctionality();
        //тесты сортировок
        testSortingByGroupNumber();
        testSortingByAverageGrade();
        testSortingByRecordBook();
        testEvenGroupNumberSorting();
        testEvenAverageGradeSorting();
        testEvenRecordBookSorting();
        testEdgeCases();
        System.out.println("\n=== Все тесты завершены ===");

        System.out.println("Тесты завершены");
    }

    //Тест паттерна Builder и валидацию
    private static void testStudentBuilderAndValidation() {
        System.out.print("1. Тест валидации Builder: ");
        try {
            new Student.StudentBuilder(10, 9.9, "123456").build();
            System.out.println("Тест не пройден - ошибка не поймана");
        } catch (IllegalArgumentException e) {
            System.out.println("ОК (валидация балла сработала: " + e.getMessage() + ")");
        }

        System.out.print("2. Тест корректного создания: ");
        try {
            Student s = new Student.StudentBuilder(1, 4.5, "111222").build();
            if (s != null && s.getGroupNumber() == 1) {
                System.out.println("ОК");
            } else {
                System.out.println("Тест не пройден - объект пуст");
            }
        } catch (Exception e) {
            System.out.println("Тест не пройден - ошибка при верных данных: " + e.getMessage() + ")");
        }
    }

    //Тест StudentCollection
    private static void testCollectionFunctionality() {
        System.out.print("3. Тест StudentCollection: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(10, 4.0, "111111").build());
        col.add(new Student.StudentBuilder(20, 5.0, "222222").build());

        if (col.size() == 2 && col.get(1).getGroupNumber() == 20) {
            System.out.println("ОК");
        } else {
            System.out.println("Тест не пройден - ошибка в размере или получении данных");
        }
    }

    private static void testSortingByGroupNumber() {
        System.out.print("4. Тест сортировки по номеру группы: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(50, 3.0, "555555").build());
        col.add(new Student.StudentBuilder(10, 4.0, "111111").build());
        col.add(new Student.StudentBuilder(30, 2.0, "333333").build());

        col.sort(new GroupNumberSortStrategy());

        boolean isSorted = (col.get(0).getGroupNumber() == 10) &&
                (col.get(1).getGroupNumber() == 30) &&
                (col.get(2).getGroupNumber() == 50);

        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — порядок нарушен");
            System.out.println("Фактический порядок: " +
                    col.get(0).getGroupNumber() + ", " +
                    col.get(1).getGroupNumber() + ", " +
                    col.get(2).getGroupNumber());
        }
    }

    // 4. Сортировка по среднему баллу
    private static void testSortingByAverageGrade() {
        System.out.print("5. Тест сортировки по среднему баллу: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(10, 4.5, "111111").build());
        col.add(new Student.StudentBuilder(20, 3.0, "222222").build());
        col.add(new Student.StudentBuilder(30, 5.0, "333333").build());

        col.sort(new AverageGradeSortStrategy());

        boolean isSorted = (col.get(0).getAverageGrade() == 3.0) &&
                (col.get(1).getAverageGrade() == 4.5) &&
                (col.get(2).getAverageGrade() == 5.0);

        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — порядок нарушен");
        }
    }

    // 5. Сортировка по номеру зачётной книжки
    private static void testSortingByRecordBook() {
        System.out.print("6. Тест сортировки по номеру зачётной книжки: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(10, 4.0, "888888").build());
        col.add(new Student.StudentBuilder(20, 3.5, "111111").build());
        col.add(new Student.StudentBuilder(30, 4.2, "222222").build());

        col.sort(new RecordBookNumberSortStrategy());


        boolean isSorted = col.get(0).getRecordBookNumber().equals("111111") &&
                col.get(1).getRecordBookNumber().equals("222222") &&
                col.get(2).getRecordBookNumber().equals("888888");

        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — порядок нарушен");
        }
    }

    // 6. Чётные номера групп
    private static void testEvenGroupNumberSorting() {
        System.out.print("7. Тест чётных номеров групп: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(11, 4.0, "111111").build()); // нечётная
        col.add(new Student.StudentBuilder(20, 3.5, "222222").build()); // чётная
        col.add(new Student.StudentBuilder(13, 4.2, "333333").build()); // нечётная
        col.add(new Student.StudentBuilder(40, 3.8, "444444").build()); // чётная

        col.sort(new EvenGroupNumberSortStrategy());

        // После сортировки чётные группы должны быть в порядке возрастания: 20, 40
        // Нечётные остаются на своих местах
        boolean isSorted = (col.get(1).getGroupNumber() == 20) &&
                (col.get(3).getGroupNumber() == 40);

        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — чётные группы не отсортированы");
        }
    }

    private static void testEvenAverageGradeSorting() {
        System.out.print("8. Тест чётных средних баллов: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(10, 4.1, "111111").build()); // ~4 → чётный
        col.add(new Student.StudentBuilder(20, 2.9, "222222").build()); // ~3 → нечётный
        col.add(new Student.StudentBuilder(30, 2.3, "333333").build()); // ~2 → чётный
        col.add(new Student.StudentBuilder(40, 5.0, "444444").build()); // 5 → нечётный


        col.sort(new EvenAverageGradeSortStrategy());

        // После сортировки чётные баллы (2.3 и 4.1) должны быть в порядке возрастания: 2.3, 4.1
        // Нечётные остаются на своих местах
        boolean isSorted = (col.get(0).getAverageGrade() == 2.3) &&
                (col.get(2).getAverageGrade() == 4.1);

        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — чётные баллы не отсортированы");
            System.out.println("Фактический порядок: " +
                    col.get(0).getAverageGrade() + ", " +
                    col.get(1).getAverageGrade());
        }
    }

    private static void testEvenRecordBookSorting() {
        System.out.print("9. Тест чётных номеров зачётной книжки: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(10, 4.0, "123456").build()); // чётный (последняя цифра 6)
        col.add(new Student.StudentBuilder(20, 3.5, "987654").build()); // чётный (4)
        col.add(new Student.StudentBuilder(30, 4.2, "135791").build()); // нечётный (1)
        col.add(new Student.StudentBuilder(40, 3.8, "246802").build()); // чётный (2)

        col.sort(new EvenRecordBookSortStrategy());

        // Чётные номера должны быть отсортированы по возрастанию: 123456, 246802, 987654
        // Нечётные остаются на месте
        boolean isSorted = col.get(0).getRecordBookNumber().equals("123456") &&
                col.get(1).getRecordBookNumber().equals("246802") &&
                col.get(3).getRecordBookNumber().equals("987654");


        if (isSorted) {
            System.out.println("ОК");
        } else {
            System.out.println("НЕ ПРОЙДЕН — чётные номера не отсортированы");
        }
    }

    private static void testEdgeCases() {
        System.out.print("10. Тест граничных случаев: ");

        // Пустая коллекция
        StudentCollection emptyCol = new StudentCollection();
        emptyCol.sort(new GroupNumberSortStrategy());
        if (emptyCol.size() == 0) {
            System.out.print("пустая → ОК, ");
        } else {
            System.out.print("пустая → НЕ ПРОЙДЕН, ");
        }

        // Коллекция из 1 элемента
        StudentCollection singleCol = new StudentCollection();
        singleCol.add(new Student.StudentBuilder(10, 4.0, "111111").build());
        singleCol.sort(new AverageGradeSortStrategy());
        if (singleCol.size() == 1 &&
                singleCol.get(0).getGroupNumber() == 10) {
            System.out.print("1 элемент → ОК");
        } else {
            System.out.print("1 элемент → НЕ ПРОЙДЕН");
        }
        System.out.println();
    }
}