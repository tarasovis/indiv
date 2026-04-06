import javax.swing.*;
import java.util.List;
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
        String trimmedLine = line.trim();
        if (trimmedLine.isEmpty()) {
            return;
        }
        String[] tokens = trimmedLine.split("\\s+");
        if (tokens.length == 2) {
            points.add(parsePoint(tokens));
            return;
        }
        if (tokens.length == 3) {
            circles.add(parseCircle(tokens));
            return;
        }
        showWarning(lineNumber);
    }

    private static PointData parsePoint(String[] tokens) {
        int x = parseInt(tokens[0]);
        int y = parseInt(tokens[1]);
        return new PointData(x, y);
    }

    private static CircleData parseCircle(String[] tokens) {
        double x = parseDouble(tokens[0]);
        double y = parseDouble(tokens[1]);
        double radius = Math.max(0, parseDouble(tokens[2]));
        return new CircleData(x, y, radius);
    }

    private static int parseInt(String value) {
        return (int) Math.round(parseDouble(value));
    }

    private static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    private static void showWarning(int lineNumber) {
        JOptionPane.showMessageDialog(null, "Skipped line " + lineNumber + ": expected 2 or 3 numbers.");
    }
}
