package studentapp;

import studentapp.model.Student;
import studentapp.collection.StudentCollection;
import studentapp.strategy.GroupNumberSortStrategy;

public class StudentTests {

    public static void main(String[] args) {
        System.out.println("Тесты: ");
        testStudentBuilderAndValidation();
        testCollectionFunctionality();
        testSortingLogic();

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

    //Тест стратегии
    private static void testSortingLogic() {
        System.out.print("4. Тест стратегии сортировки: ");
        StudentCollection col = new StudentCollection();
        col.add(new Student.StudentBuilder(50, 3.0, "555555").build());
        col.add(new Student.StudentBuilder(10, 4.0, "111111").build());

        // Используем базовую стратегию сортировки по группе
        new GroupNumberSortStrategy().sort(col);

        if (col.get(0).getGroupNumber() == 10 && col.get(1).getGroupNumber() == 50) {
            System.out.println("ОК (сортировка по возрастанию выполнена)");
        } else {
            System.out.println("Тест не пройден - порядок не изменился)");
        }
    }
}