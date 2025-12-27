package studentapp.strategy;

import studentapp.model.Student;

public class GroupNumberSortStrategy implements StudentComparator {
    @Override
    public int compare(Student s1, Student s2) {
        return Integer.compare(s1.getGroupNumber(), s2.getGroupNumber());
    }

    @Override
    public String getDescription() {
        return "Сортировка по номеру группы (по возрастанию)";
    }
}