package studentapp.model;
/*
 * Конструктор с обязательными полями.
 *    groupNumber номер группы (> 0)
 *    averageGrade средний балл (0 – 5)
 *    recordBookNumber номер зачётной книжки (6 цифр)
 */
public class Student {
    // Обязательные поля (final — не меняются после создания)
    private final int groupNumber;
    private final double averageGrade;
    private final String recordBookNumber;

    // Необязательные поля (могут быть null)
    private final String fullName;
    private final String email;

    // Частный конструктор — доступен только через Builder
    private Student(StudentBuilder builder) {
        this.groupNumber = builder.groupNumber;
        this.averageGrade = builder.averageGrade;
        this.recordBookNumber = builder.recordBookNumber;
        this.fullName = builder.fullName;
        this.email = builder.email;
    }

    // Геттеры для всех полей
    public int getGroupNumber() {
        return groupNumber;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Студент: " +
                "Номер группы: " + groupNumber +
                ", Средний балл: " + String.format("%.2f", averageGrade)  +
                ", Номер зачетки: " + recordBookNumber +
                ", Полное имя: " + (fullName != null ? "'" + fullName + "'" : "Отсутствует") +
                ", email: " + (email != null ? "'" + email + "'" : "Отсутствует");
    }

    // Вложенный класс-билдер
    public static class StudentBuilder {
        // Обязательные поля (должны быть заданы в конструкторе)
        private final int groupNumber;
        private final double averageGrade;
        private final String recordBookNumber;

        // Необязательные поля (инициализированы как null)
        private String fullName = null;
        private String email = null;

        public StudentBuilder(int groupNumber, double averageGrade, String recordBookNumber) {
            // Валидация обязательных полей
            if (groupNumber <= 0) {
                throw new IllegalArgumentException("Номер группы должен быть > 0");
            }
            if (averageGrade < 0.0 || averageGrade > 5.0) {
                throw new IllegalArgumentException("Средний балл должен быть от 0.0 до 5.0");
            }
            if (recordBookNumber == null || recordBookNumber.trim().isEmpty()) {
                throw new IllegalArgumentException("Номер зачётной книжки не может быть пустым");
            }
            if (!recordBookNumber.matches("\\d{6}")) {
                throw new IllegalArgumentException("Номер зачётной книжки должен содержать ровно 6 цифр");
            }

            this.groupNumber = groupNumber;
            this.averageGrade = averageGrade;
            this.recordBookNumber = recordBookNumber.trim();
        }

        // Сеттеры для необязательных полей (возвращают this для цепочки вызовов)
        public StudentBuilder setFullName(String fullName) {
            this.fullName = (fullName != null) ? fullName.trim() : null;
            return this;
        }

        public StudentBuilder setEmail(String email) {
            if (email != null && !email.trim().isEmpty()) {
                // Простая проверка формата email
                if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    throw new IllegalArgumentException("Некорректный формат email");
                }
                this.email = email.trim();
            } else {
                this.email = null;
            }
            return this;
        }

        // Метод для создания объекта Student
        public Student build() {
            return new Student(this);
        }
    }
}