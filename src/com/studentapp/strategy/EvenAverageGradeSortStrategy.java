package studentapp.strategy;

import studentapp.model.Student;

public class EvenAverageGradeSortStrategy extends EvenFieldSortStrategy {
    @Override
    protected int getSortField(Student s) {
        return (int) Math.round(s.getAverageGrade());
    }

    @Override
    public String getDescription() {
        return "Четные средние баллы";
    }

    @Override
    public void sort(Student[] students, int size) {
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (isEven(getSortField(students[i])) && isEven(getSortField(students[j]))) {
                    if ((students[i]).getAverageGrade() > (students[j]).getAverageGrade()) {
                        Student temp = students[i];
                        students[i] = students[j];
                        students[j] = temp;
                    }
                }
            }
        }
    }

    private boolean isEven(int val) {
        return val % 2 == 0;
    }
}