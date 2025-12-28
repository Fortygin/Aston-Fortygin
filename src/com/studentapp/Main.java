package studentapp;

import studentapp.collection.StudentCollection;

import java.util.*;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final Scanner scanner = new Scanner(System.in);

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
                case "1" -> collection = InputHandler.handleInputSelection();
                case "2" -> DisplayHandler.displayCollection(collection);
                case "3" -> SortHandler.handleSortSelection(collection);
                case "4" -> DisplayHandler.saveCollectionToFile(collection);
                case "5" -> SearchHandler.performCountOccurrences(collection);
                default -> logger.log(Level.WARNING, "Неверный ввод в меню: {0}", choice);
            }
        }
    }
}