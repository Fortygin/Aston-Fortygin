package com.studentapp.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

 //Класс для записи данных студентов в файл в режиме добавления
public class FileDataWriter {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    /**
     * Записывает список студентов в файл в режиме добавления
     * @param students список студентов
     * @param fileName имя файла
     * @param description описание данных
     * @throws IOException если произошла ошибка записи
     */
    public static <T> void appendToFile(List<T> students, String fileName, String description)
            throws IOException {

        if (students == null || fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("Некорректные параметры для записи");
        }

        // Открываем файл в режиме добавления (append = true)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            // Записываем разделитель и заголовок
            writer.write("\n" + "=".repeat(60));
            writer.newLine();

            // Временная метка
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            writer.write("Запись от: " + timestamp);
            writer.newLine();

            // Описание
            if (description != null && !description.trim().isEmpty()) {
                writer.write("Описание: " + description);
                writer.newLine();
            }

            // Количество записей
            writer.write("Количество записей: " + students.size());
            writer.newLine();

            writer.write("-".repeat(60));
            writer.newLine();

            // Записываем данные
            int counter = 1;
            for (T student : students) {
                writer.write(String.format("%3d. %s", counter++, student.toString()));
                writer.newLine();
            }

            writer.write("=".repeat(60));
            writer.newLine();

            System.out.println("[FileDataWriter] Данные успешно добавлены в файл: " + fileName);

        } catch (IOException e) {
            System.err.println("[FileDataWriter] Ошибка записи в файл '" + fileName + "': " + e.getMessage());
            throw e; // Пробрасываем дальше
        }
    }

   // Перегрузка метода без описания
    public static <T> void appendToFile(List<T> data, String fileName) throws IOException {
        appendToFile(data, fileName, "Данные от " + LocalDateTime.now().format(TIMESTAMP_FORMATTER));
    }

    //Записывает результаты поиска в файл с указанием критерия
    public static <T> void appendSearchResults(List<T> results, String fileName,
                                               String searchCriteria) throws IOException {
        String description = "Результаты поиска: " + searchCriteria;
        appendToFile(results, fileName, description);
    }

    // Создает файл, если он не существует.
    public static void createFileIfNotExists(String fileName) throws IOException {
        java.io.File file = new java.io.File(fileName);
        if (!file.exists()) {
            // Создаем родительские директории при необходимости
            java.io.File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
            System.out.println("[FileDataWriter] Создан новый файл: " + fileName);
        }
    }

    // Проверяет, существует ли файл
    public static boolean fileExists(String fileName) {
        return new java.io.File(fileName).exists();
    }

    // Возвращает размер файла в байтах
    public static long getFileSize(String fileName) {
        java.io.File file = new java.io.File(fileName);
        return file.exists() ? file.length() : 0;
    }

    // Генерирует имя файла с датой
    public static String generateFileName(String baseName) {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        return baseName + "_" + date + ".txt";
    }
}