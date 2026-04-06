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
            showError("Failed to save file: " + exception.getMessage());
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
            return parseDataSet(scanner);
        } catch (Exception exception) {
            showError("Failed to load file: " + exception.getMessage());
            return new DataSet(new ArrayList<>(), new ArrayList<>());
        }
    }

    private static DataSet parseDataSet(Scanner scanner) {
        List<PointData> points = readPoints(scanner);
        List<CircleData> circles = readCircles(scanner);
        return new DataSet(points, circles);
    }

    private static List<PointData> readPoints(Scanner scanner) {
        int pointCount = safeNextInt(scanner);
        List<PointData> points = new ArrayList<>();
        for (int index = 0; index < pointCount; index++) {
            points.add(new PointData(safeNextInt(scanner), safeNextInt(scanner)));
        }
        return points;
    }

    private static List<CircleData> readCircles(Scanner scanner) {
        int circleCount = safeNextInt(scanner);
        List<CircleData> circles = new ArrayList<>();
        for (int index = 0; index < circleCount; index++) {
            circles.add(new CircleData(safeNextDouble(scanner), safeNextDouble(scanner), safeNextDouble(scanner)));
        }
        return circles;
    }

    private static int safeNextInt(Scanner scanner) {
        return scanner.hasNextInt() ? scanner.nextInt() : 0;
    }

    private static double safeNextDouble(Scanner scanner) {
        return scanner.hasNextDouble() ? scanner.nextDouble() : 0;
    }

    public static DataSet example1() {
        return fromText("3\n100 100\n300 120\n180 260\n2\n500 120 50\n580 330 70\n");
    }

    public static DataSet example2() {
        return fromText("5\n90 80\n200 60\n310 140\n260 260\n120 240\n3\n420 80 45\n450 250 55\n70 320 40\n");
    }

    private static DataSet fromText(String text) {
        Scanner scanner = new Scanner(text);
        DataSet dataSet = parseDataSet(scanner);
        scanner.close();
        return dataSet;
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}
