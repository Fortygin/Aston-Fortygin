package studentapp.strategy;

import studentapp.model.Student;

public class RecordBookNumberSortStrategy implements StudentComparator {
    @Override
    public int compare(Student s1, Student s2) {
        // Сначала сравниваем по длине
        int lenDiff = s1.getRecordBookNumber().length() - s2.getRecordBookNumber().length();
        if (lenDiff != 0) {
            return lenDiff;
        }
        // Затем лексикографически
        return s1.getRecordBookNumber().compareTo(s2.getRecordBookNumber());
    }

    @Override
    public String getDescription() {
        return "Сортировка по номеру зачётной книжки";
    }
}