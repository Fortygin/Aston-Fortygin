package studentapp;

import studentapp.model.Student;
import studentapp.strategy.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    static {
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$s] %5$s%n");
    }

    public static void main(String[] args) {
        List<Student> students = null;

        while (true) {
            System.out.println("\nМеню (выберите из предложенных вариантов)");
            System.out.println("1. Заполнить данные студентов");
            System.out.println("2. Вывести данные на экран");
            System.out.println("3. Отсортировать список");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine();
            if (choice.equals("0")) {
                logger.info("Завершение работы программы.");
                break;
            }

            switch (choice) {
                case "1" -> students = handleInputSelection();
                case "2" -> displayStudents(students);
                case "3" -> {
                    if (students == null || students.isEmpty()) {
                        logger.warning("Попытка сортировки пустого списка.");
                    } else {
                        handleSortSelection(students);
                    }
                }
                default -> logger.log(Level.WARNING, "Неверный ввод в меню: {0}", choice);
            }
        }
    }

    private static List<Student> handleInputSelection() {
        System.out.println("\n1. Вручную\n2. Автоматическое заполнение Рандом\n3. Файл");
        System.out.print("Ваш выбор: ");

        String mode = scanner.nextLine();
        int count = getValidCount();

        return switch (mode) {
            case "1" -> fillManual(count);
            case "2" -> fillRandom(count);
            case "3" -> fillFromFile(count);
            default -> {
                logger.warning("Выбран неверный способ заполнения.");
                yield null;
            }
        };
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

    private static List<Student> fillManual(int count) {
        List<Student> list = new ArrayList<>();
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
                } catch (Exception e) { logger.log(Level.WARNING, "Ошибка ввода группы: {0}", e.getMessage()); }
            }

            // Балл
            while (true) {
                try {
                    System.out.print("Балл (0-5): ");
                    grade = Double.parseDouble(scanner.nextLine());
                    if (grade < 0 || grade > 5) throw new IllegalArgumentException("Балл 0.0 - 5.0");
                    break;
                } catch (Exception e) { logger.log(Level.WARNING, "Ошибка ввода балла: {0}", e.getMessage()); }
            }

            // Зачетка
            while (true) {
                try {
                    System.out.print("Зачетка (6 цифр): ");
                    book = scanner.nextLine();
                    if (!book.matches("\\d{6}")) throw new IllegalArgumentException("Нужно 6 цифр!");
                    break;
                } catch (Exception e) { logger.log(Level.WARNING, "Ошибка ввода зачетки: {0}", e.getMessage()); }
            }

            list.add(new Student.StudentBuilder(group, grade, book).build());
        }
        logger.log(Level.INFO, "Вручную добавлено студентов: {0}", list.size());
        return list;
    }

    private static List<Student> fillFromFile(int count) {
        List<Student> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("students.txt"))) {
            for (int i = 0; i < count; i++) {
                String line = br.readLine();
                if (line == null) break;
                try {
                    String[] d = line.split(",");
                    list.add(new Student.StudentBuilder(
                            Integer.parseInt(d[0].trim()),
                            Double.parseDouble(d[1].trim()),
                            d[2].trim()).build());
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Битые данные в файле: {0}", line);
                }
            }
            logger.info("Данные из файла успешно загружены.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Файл не найден или недоступен!", e);
        }
        return list;
    }

    private static List<Student> fillRandom(int count) {
        List<Student> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new Student.StudentBuilder(random.nextInt(50) + 1,
                    2.0 + 3.0 * random.nextDouble(),
                    String.format("%06d", random.nextInt(1000000))).build());
        }
        logger.log(Level.INFO, "Сгенерировано случайных студентов: {0}", count);
        return list;
    }

    private static void handleSortSelection(List<Student> students) {
        System.out.println("\nВыберет стратегию сортировки: ");
        System.out.println("1. По номеру группы (все)");
        System.out.println("2. По среднему баллу (все)");
        System.out.println("3. По номеру зачетки (все)");
        System.out.println("4. По номеру группы (Только четные)");
        System.out.println("5. По среднему баллу (Только четные)");
        System.out.println("6. По номеру зачетки (Только четные)");
        System.out.print("Ваш выбор: ");

        String c = scanner.nextLine();
        SortStrategy s = switch (c) {
            case "1" -> new GroupNumberSortStrategy();
            case "2" -> new AverageGradeSortStrategy();
            case "3" -> new RecordBookNumberSortStrategy();
            case "4" -> new EvenGroupNumberSortStrategy();
            case "5" -> new EvenAverageGradeSortStrategy();
            case "6" -> new EvenRecordBookSortStrategy();
            default -> null;
        };

        if (s != null) {
            s.sort(students);
            System.out.flush();
            logger.log(Level.INFO, "Сортировка выполнена: {0}", s.getStrategyName());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e){}
            displayStudents(students);
        } else {
            logger.warning("Сортировка не выбрана.");
        }
    }

    private static void displayStudents(List<Student> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("Список пуст.");
        } else {
            list.forEach(System.out::println);
        }
    }
}