package ru.vsu.vinyukov.demo;

import ru.vsu.vinyukov.demo.model.Range;
import ru.vsu.vinyukov.demo.rangeset.RangeSet;
import ru.vsu.vinyukov.demo.rangeset.impl.TreeRangeSet;
import ru.vsu.vinyukov.demo.rangemap.RangeMap;
import ru.vsu.vinyukov.demo.rangemap.impl.TreeRangeMap;

public class UniversityGradeDemo {
    public static void main(String[] args) {
        System.out.println("=== СИСТЕМА ОЦЕНОК УНИВЕРСИТЕТА ===\n");

        demonstrateGradeSystem();

        System.out.println("\n" + "=".repeat(50) + "\n");

        demonstrateScholarshipSystem();

        System.out.println("\n" + "=".repeat(50) + "\n");

        demonstrateExamEligibility();
    }

    private static void demonstrateGradeSystem() {
        System.out.println("1. СИСТЕМА ОЦЕНОК ПО БАЛЛАМ:");

        RangeMap<Integer, String> gradeSystem = new TreeRangeMap<>();

        gradeSystem.put(new Range<>(0, 59), "Неудовлетворительно (2)");
        gradeSystem.put(new Range<>(60, 74), "Удовлетворительно (3)");
        gradeSystem.put(new Range<>(75, 89), "Хорошо (4)");
        gradeSystem.put(new Range<>(90, 100), "Отлично (5)");

        int[] testScores = {45, 62, 78, 95, 100, 55, 85, 91, 30, 67};

        System.out.println("Баллы -> Оценки:");
        for (int score : testScores) {
            String grade = gradeSystem.get(score);
            System.out.printf("  %3d баллов → %s\n", score, grade);
        }
        System.out.println("\nПроверка граничных значений:");
        System.out.printf("  59 баллов → %s\n", gradeSystem.get(59));
        System.out.printf("  60 баллов → %s\n", gradeSystem.get(60));
        System.out.printf("  74 балла → %s\n", gradeSystem.get(74));
        System.out.printf("  75 баллов → %s\n", gradeSystem.get(75));
        System.out.printf("  89 баллов → %s\n", gradeSystem.get(89));
        System.out.printf("  90 баллов → %s\n", gradeSystem.get(90));
    }

    private static void demonstrateScholarshipSystem() {
        System.out.println("2. СИСТЕМА СТИПЕНДИЙ:");

        RangeMap<Double, String> scholarshipSystem = new TreeRangeMap<>();

        scholarshipSystem.put(new Range<>(0.0, 3.49), "Без стипендии");
        scholarshipSystem.put(new Range<>(3.5, 4.49), "Обычная стипендия");
        scholarshipSystem.put(new Range<>(4.5, 4.79), "Повышенная стипендия");
        scholarshipSystem.put(new Range<>(4.8, 5.0), "Именная стипендия");
        double[] studentGPAs = {2.8, 3.6, 4.2, 4.6, 4.9, 3.49, 3.5, 4.49, 4.5, 5.0};

        System.out.println("Средний балл -> Стипендия:");
        for (double gpa : studentGPAs) {
            String scholarship = scholarshipSystem.get(gpa);
            System.out.printf("  %.2f → %s\n", gpa, scholarship);
        }

        System.out.println("\nАнализ группы из 100 студентов:");
        int[] gpaDistribution = {25, 40, 20, 15};
        String[] levels = {"Без стипендии", "Обычная", "Повышенная", "Именная"};

        for (int i = 0; i < levels.length; i++) {
            System.out.printf("  %s: %d студентов\n", levels[i], gpaDistribution[i]);
        }
    }

    private static void demonstrateExamEligibility() {
        System.out.println("3. СИСТЕМА ДОПУСКА К ЭКЗАМЕНАМ:");

        RangeSet<Integer> attendanceRanges = new TreeRangeSet<>();

        attendanceRanges.add(new Range<>(0, 49));
        attendanceRanges.add(new Range<>(80, 100));

        int[] attendancePercentages = {35, 65, 85, 42, 92, 78, 55, 95, 28, 72};

        System.out.println("Посещаемость -> Статус:");
        for (int attendance : attendancePercentages) {
            String status;
            if (attendanceRanges.contains(attendance)) {
                if (attendance >= 80) {
                    status = "АВТОМАТ (освобожден от экзамена)";
                } else {
                    status = "НЕ ДОПУЩЕН (мало посещений)";
                }
            } else {
                status = "ДОПУЩЕН к экзамену";
            }
            System.out.printf("  %3d%% посещений → %s\n", attendance, status);
        }

        System.out.println("\nПроверка для деканата:");
        int checkAttendance = 75;
        boolean hasAutomat = attendanceRanges.contains(checkAttendance) && checkAttendance >= 80;
        boolean notAllowed = attendanceRanges.contains(checkAttendance) && checkAttendance < 80;

        if (hasAutomat) {
            System.out.printf("Студент с %d%% посещений: Автомат\n", checkAttendance);
        } else if (notAllowed) {
            System.out.printf("Студент с %d%% посещений: Не допущен\n", checkAttendance);
        } else {
            System.out.printf("Студент с %d%% посещений: Допущен к экзамену\n", checkAttendance);
        }
    }
}