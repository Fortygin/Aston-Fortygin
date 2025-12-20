import java.io.*;
import java.util.*;

public class StudentManager {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    public static void main(String[] args) {
        Student[] students = null;

        while (true) {
            System.out.println("\nМеню: ");
            System.out.println("1. Заполнить данные студентов");
            System.out.println("2. Вывести данные");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("Программа завершена.");
                break;
            }

            switch (choice) {
                case "1":
                    students = handleInputSelection();
                    break;
                case "2":
                    if (students == null) {
                        System.out.println("Ошибка: Сначала надо заполнить данные!");
                    } else {
                        System.out.println("\nСписок студентов:");
                        for (Student s : students) {
                            if (s != null) System.out.println(s);
                        }
                    }
                    break;
                default:
                    System.out.println("Неверный выбор, попробуйте снова.");
            }
        }
    }

    private static Student[] handleInputSelection() {
        System.out.println("\nВыберите способ заполнения:");
        System.out.println("1. Вручную");
        System.out.println("2. Случайные данные (Random)");
        System.out.println("3. Из файла (students.txt)");

        String mode = scanner.nextLine();
        int count = getValidCount();

        switch (mode) {
            case "1": return fillManual(count);
            case "2": return fillRandom(count);
            case "3": return fillFromFile(count);
            default:
                System.out.println("Неверный вариант. Возврат в меню.");
                return null;
        }
    }

    private static int getValidCount() {
        while (true) {
            try {
                System.out.print("Введите количество студентов (не может быть меньше 1!): ");
                int count = Integer.parseInt(scanner.nextLine());
                if (count > 0) return count;
                System.out.println("Ошибка: Число должно быть больше 0!");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: Введите количество цифрой!");
            }
        }
    }

    private static Student[] fillManual(int count) {
        Student[] arr = new Student[count];
        for (int i = 0; i < count; i++) {
            System.out.println("\nВвод данных студента №" + (i + 1) + " ---");
            while (true) {
                try {
                    System.out.print("Введите номер группы: ");
                    int group = Integer.parseInt(scanner.nextLine());

                    System.out.print("Введите средний балл (0-5): ");
                    double grade = Double.parseDouble(scanner.nextLine());

                    System.out.print("Введите номер зачетки (6 цифр): ");
                    String book = scanner.nextLine();

                    arr[i] = new Student.StudentBuilder(group, grade, book).build();
                    break; // Если ошибок нет, выходим из while и переходим к следующему студенту
                } catch (Exception e) {
                    System.out.println("ОШИБКА ВВОДА: " + e.getMessage() + ". Попробуйте снова.");
                }
            }
        }
        return arr;
    }

    private static Student[] fillRandom(int count) {
        Student[] arr = new Student[count];
        for (int i = 0; i < count; i++) {
            int group = random.nextInt(100) + 1;
            double grade = 2.0 + (3.0 * random.nextDouble());
            String book = String.format("%06d", random.nextInt(1000000));

            arr[i] = new Student.StudentBuilder(group, grade, book).build();
        }
        System.out.println("Сгенерировано " + count + " студентов.");
        return arr;
    }

    private static Student[] fillFromFile(int count) {
        Student[] arr = new Student[count];
        String fileName = "students.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            for (int i = 0; i < count; i++) {
                String line = br.readLine();
                if (line == null) break;

                String[] data = line.split(","); // Формат в файле: группа,балл,зачетка
                if (data.length >= 3) {
                    int group = Integer.parseInt(data[0].trim());
                    double grade = Double.parseDouble(data[1].trim());
                    String book = data[2].trim();

                    arr[i] = new Student.StudentBuilder(group, grade, book).build();
                }
            }
            System.out.println("Данные успешно считаны из " + fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка: Файл " + fileName + " не найден!");
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return arr;
    }
}