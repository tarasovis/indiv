import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class FileManager {
    private FileManager() {
    }

    public static void save(File file, List<PointData> points, List<CircleData> circles) {
        try (PrintWriter out = new PrintWriter(file)) {
            writePoints(out, points);
            writeCircles(out, circles);
        } catch (FileNotFoundException exception) {
            showError("Ошибка сохранения: " + exception.getMessage());
        }
    }

    private static void writePoints(PrintWriter out, List<PointData> points) {
        out.println(points.size());
        for (PointData point : points) {
            out.println(point.x() + " " + point.y());
        }
    }

    private static void writeCircles(PrintWriter out, List<CircleData> circles) {
        out.println(circles.size());
        for (CircleData circle : circles) {
            out.printf(Locale.US, "%.4f %.4f %.4f%n", circle.centerX(), circle.centerY(), circle.radius());
        }
    }

    public static DataSet load(File file) {
        try (Scanner scanner = new Scanner(file)) {
            scanner.useLocale(Locale.US);
            return readData(scanner);
        } catch (Exception exception) {
            showError("Ошибка чтения: " + exception.getMessage());
            return new DataSet(new ArrayList<>(), new ArrayList<>());
        }
    }

    private static DataSet readData(Scanner scanner) {
        int pointCount = nextIntRequired(scanner, "Количество точек");
        List<PointData> points = readPoints(scanner, pointCount);
        int circleCount = nextIntRequired(scanner, "Количество кругов");
        List<CircleData> circles = readCircles(scanner, circleCount);
        return new DataSet(points, circles);
    }

    private static List<PointData> readPoints(Scanner scanner, int count) {
        List<PointData> points = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            int x = nextIntRequired(scanner, "x точки");
            int y = nextIntRequired(scanner, "y точки");
            points.add(new PointData(x, y));
        }
        return points;
    }

    private static List<CircleData> readCircles(Scanner scanner, int count) {
        List<CircleData> circles = new ArrayList<>();
        for (int index = 0; index < count; index++) {
            double x = nextDoubleRequired(scanner, "x центра");
            double y = nextDoubleRequired(scanner, "y центра");
            double radius = nextDoubleRequired(scanner, "радиус");
            circles.add(new CircleData(x, y, Math.max(0, radius)));
        }
        return circles;
    }

    private static int nextIntRequired(Scanner scanner, String name) {
        if (!scanner.hasNextInt()) {
            throw new IllegalArgumentException("Не найдено значение: " + name);
        }
        return scanner.nextInt();
    }

    private static double nextDoubleRequired(Scanner scanner, String name) {
        if (!scanner.hasNextDouble()) {
            throw new IllegalArgumentException("Не найдено значение: " + name);
        }
        return scanner.nextDouble();
    }

    public static DataSet example1() {
        return fromText("4\n100 100\n280 110\n200 270\n120 240\n3\n500 120 60\n580 320 70\n420 260 40\n");
    }

    public static DataSet example2() {
        return fromText("5\n80 90\n220 80\n340 170\n250 300\n90 260\n4\n420 90 50\n460 250 65\n70 340 45\n360 360 35\n");
    }

    private static DataSet fromText(String text) {
        Scanner scanner = new Scanner(text);
        scanner.useLocale(Locale.US);
        DataSet dataSet = readData(scanner);
        scanner.close();
        return dataSet;
    }

    private static void showError(String text) {
        JOptionPane.showMessageDialog(null, text);
    }
}
