import javax.swing.*;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InputParser {
    private InputParser() {
    }

    public static void applyLines(String text, List<PointData> points, List<CircleData> circles) {
        Scanner scanner = new Scanner(text);
        int lineNumber = 0;
        while (scanner.hasNextLine()) {
            lineNumber++;
            parseLine(scanner.nextLine(), lineNumber, points, circles);
        }
        scanner.close();
    }

    private static void parseLine(String line, int lineNumber, List<PointData> points, List<CircleData> circles) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) {
            return;
        }
        Scanner lineScanner = new Scanner(trimmed);
        lineScanner.useLocale(Locale.US);
        readObject(lineScanner, lineNumber, points, circles);
        lineScanner.close();
    }

    private static void readObject(Scanner lineScanner, int lineNumber,
                                   List<PointData> points, List<CircleData> circles) {
        if (!lineScanner.hasNextDouble()) {
            showWarning(lineNumber);
            return;
        }
        double first = lineScanner.nextDouble();
        if (!lineScanner.hasNextDouble()) {
            showWarning(lineNumber);
            return;
        }
        double second = lineScanner.nextDouble();
        if (lineScanner.hasNextDouble()) {
            circles.add(new CircleData(first, second, Math.max(0, lineScanner.nextDouble())));
            return;
        }
        points.add(new PointData((int) Math.round(first), (int) Math.round(second)));
    }

    private static void showWarning(int lineNumber) {
        JOptionPane.showMessageDialog(null, "Строка " + lineNumber + " пропущена: нужно 2 или 3 числа.");
    }
}
