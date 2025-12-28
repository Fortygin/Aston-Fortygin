package studentapp;

import studentapp.collection.StudentCollection;
import studentapp.strategy.*;

import java.util.Scanner;
import java.util.logging.*;

import static studentapp.DisplayHandler.*;

// сортировка
public class SortHandler {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getLogger(SortHandler.class.getName());
    static void handleSortSelection(StudentCollection collection) {
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
                DisplayHandler.saveCollectionToFile(collection, strategyDescription);
                break;
            } else if (saveChoice.equals("n")) {
                logger.info("Сохранение отменено.");
                break;
            } else {
                logger.warning("Неверный ввод. Введите 'y' или 'n'.");
            }
        }
    }
}