package com.studentapp.strategy;

import com.studentapp.model.Student;
import java.util.List;

public interface SortStrategy {
    void sort(List<Student> students);
    String getStrategyName();
}

// 2. Стратегия 1: По номеру группы
package com.studentapp.strategy;

import com.studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class GroupNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        students.sort(Comparator.comparing(Student::getGroupNumber));
    }

    @Override
    public String getStrategyName() {
        return "По номеру группы";
    }
}

// 3. Стратегия 2: По среднему баллу
package com.studentapp.strategy;

import com.studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class AverageGradeSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        // Сортировка по убыванию
        students.sort(Comparator.comparingDouble(Student::getAverageGrade).reversed());
    }

    @Override
    public String getStrategyName() {
        return "По среднему баллу";
    }
}

// 4. Стратегия 3: По номеру зачетки
package com.studentapp.strategy;

import com.studentapp.model.Student;
import java.util.Comparator;
import java.util.List;

public class RecordBookNumberSortStrategy implements SortStrategy {
    @Override
    public void sort(List<Student> students) {
        if (students == null) return;
        students.sort(Comparator.comparing(Student::getRecordBookNumber));
    }

    @Override
    public String getStrategyName() {
        return "По номеру зачетной книжки";
    }
}