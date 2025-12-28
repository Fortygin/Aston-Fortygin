package com.studentapp.util;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.*;

 // Многопоточный подсчет вхождений элемента в коллекцию
public class MultithreadedCounter {
     // Подсчитывает количество вхождений элемента в коллекцию. Использует многопоточность
    public static <T> int countOccurrences(Collection<T> collection, T element) {
        // 1. Проверка входных данных
        if (collection == null || element == null) {
            System.out.println("Ошибка: параметры не могут быть null");
            return 0;
        }

        // 2. Если коллекция пустая
        if (collection.isEmpty()) {
            System.out.println("Коллекция пуста. Найдено: 0");
            return 0;
        }

        // 3. Создание пул потоков
        int threadCount = Math.min(4, collection.size()); // Простое правило
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        // 4. Потокобезопасный счетчик
        AtomicInteger totalCount = new AtomicInteger(0);

        try {
            // 5. Преобразуем в список для удобного разделения
            java.util.List<T> list = new java.util.ArrayList<>(collection);
            int size = list.size();
            int chunkSize = size / threadCount;

            // 6. Запускаем задачи в потоках
            for (int i = 0; i < threadCount; i++) {
                final int start = i * chunkSize;
                final int end = (i == threadCount - 1) ? size : (i + 1) * chunkSize;

                executor.submit(() -> {
                    int localCount = 0;
                    for (int j = start; j < end; j++) {
                        if (list.get(j).equals(element)) {
                            localCount++;
                        }
                    }
                    totalCount.addAndGet(localCount);
                });
            }

        } finally {
            // 7. Закрываем ExecutorService
            executor.shutdown();
            try {
                executor.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                System.out.println("Поиск был прерван");
            }
        }

        // 8. Выводим результат в консоль
        System.out.println("Найдено вхождений элемента: " + totalCount.get());

        return totalCount.get();
    }
}