package studentapp;

import studentapp.collection.StudentCollection;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.*;

// показ данных и сохранение в файлы
public class DisplayHandler {
    private static final Logger logger = Logger.getLogger(DisplayHandler.class.getName());

    static void displayCollection(StudentCollection collection) {
        if (collection == null || collection.isEmpty()) {
            System.out.println("Коллекция пуста.");
            return;
        }
        System.out.println("\nСодержимое коллекции:");
        for (int i = 0; i < collection.size(); i++) {
            System.out.println(collection.get(i));
        }
    }

    static void saveCollectionToFile(StudentCollection collection, String strategyName) {
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
    static void saveCollectionToFile(StudentCollection collection) {
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
}