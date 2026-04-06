import java.util.Locale;
import java.util.Scanner;

public class TextDataParser {
    public static ProjectData parseText(String sourceText) {
        Scanner textScanner = new Scanner(sourceText);
        textScanner.useLocale(Locale.US);
        ProjectData projectData = parseScanner(textScanner);
        textScanner.close();
        return projectData;
    }

    public static ProjectData parseScanner(Scanner textScanner) {
        ProjectData projectData = new ProjectData();
        readKeyword(textScanner, "POINTS");
        readPointList(textScanner, projectData);
        readKeyword(textScanner, "CIRCLES");
        readCircleList(textScanner, projectData);
        return projectData;
    }

    private static void readPointList(Scanner textScanner, ProjectData projectData) {
        int pointCount = readNonNegativeInt(textScanner, "количество точек");
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            double xCoordinate = readDoubleValue(textScanner, "x точки");
            double yCoordinate = readDoubleValue(textScanner, "y точки");
            projectData.addPoint(new PlanePoint(xCoordinate, yCoordinate));
        }
    }

    private static void readCircleList(Scanner textScanner, ProjectData projectData) {
        int circleCount = readNonNegativeInt(textScanner, "количество кругов");
        for (int circleIndex = 0; circleIndex < circleCount; circleIndex++) {
            double centerX = readDoubleValue(textScanner, "x центра круга");
            double centerY = readDoubleValue(textScanner, "y центра круга");
            double radiusValue = readDoubleValue(textScanner, "радиус круга");
            projectData.addCircle(new PlaneCircle(new PlanePoint(centerX, centerY), radiusValue));
        }
    }

    private static void readKeyword(Scanner textScanner, String expectedKeyword) {
        if (!textScanner.hasNext()) {
            throw new IllegalArgumentException("Ожидалось слово " + expectedKeyword + ".");
        }
        String actualKeyword = textScanner.next();
        if (!expectedKeyword.equalsIgnoreCase(actualKeyword)) {
            throw new IllegalArgumentException("Ожидалось слово " + expectedKeyword + ".");
        }
    }

    private static int readNonNegativeInt(Scanner textScanner, String valueName) {
        if (!textScanner.hasNextInt()) {
            throw new IllegalArgumentException("Некорректно задано: " + valueName + ".");
        }
        int integerValue = textScanner.nextInt();
        if (integerValue < 0) {
            throw new IllegalArgumentException("Значение не может быть отрицательным: " + valueName + ".");
        }
        return integerValue;
    }

    private static double readDoubleValue(Scanner textScanner, String valueName) {
        if (!textScanner.hasNextDouble()) {
            throw new IllegalArgumentException("Некорректно задано: " + valueName + ".");
        }
        return textScanner.nextDouble();
    }
}
