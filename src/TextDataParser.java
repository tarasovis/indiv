import java.util.Locale;
import java.util.Scanner;

public class TextDataParser {
    public static ProjectData parseKeyboardText(String sourceText) {
        Scanner textScanner = new Scanner(sourceText);
        textScanner.useLocale(Locale.US);
        ProjectData projectData = new ProjectData();
        while (textScanner.hasNextLine()) {
            parseKeyboardLine(textScanner.nextLine(), projectData);
        }
        textScanner.close();
        return projectData;
    }

    public static ProjectData parseFileScanner(Scanner fileScanner) {
        ProjectData projectData = new ProjectData();
        int pointCount = readNonNegativeInt(fileScanner, "point count");
        readPoints(fileScanner, projectData, pointCount);
        int circleCount = readNonNegativeInt(fileScanner, "circle count");
        readCircles(fileScanner, projectData, circleCount);
        return projectData;
    }

    private static void parseKeyboardLine(String lineText, ProjectData projectData) {
        String trimmedLine = lineText.trim();
        if (trimmedLine.isEmpty()) {
            return;
        }
        String[] tokenArray = trimmedLine.split("\\s+");
        if (tokenArray.length == 2) {
            addPointFromTokens(tokenArray, projectData);
            return;
        }
        if (tokenArray.length == 3) {
            addCircleFromTokens(tokenArray, projectData);
            return;
        }
        throw new IllegalArgumentException("Each line must contain two or three numbers.");
    }

    private static void addPointFromTokens(String[] tokenArray, ProjectData projectData) {
        double xValue = parseDoubleToken(tokenArray[0], "point x");
        double yValue = parseDoubleToken(tokenArray[1], "point y");
        projectData.addPoint(new PlanePoint(xValue, yValue));
    }

    private static void addCircleFromTokens(String[] tokenArray, ProjectData projectData) {
        double xValue = parseDoubleToken(tokenArray[0], "circle center x");
        double yValue = parseDoubleToken(tokenArray[1], "circle center y");
        double radiusValue = parseDoubleToken(tokenArray[2], "circle radius");
        projectData.addCircle(new PlaneCircle(new PlanePoint(xValue, yValue), radiusValue));
    }

    private static void readPoints(Scanner fileScanner, ProjectData projectData, int pointCount) {
        for (int pointIndex = 0; pointIndex < pointCount; pointIndex++) {
            double xValue = readDoubleValue(fileScanner, "point x");
            double yValue = readDoubleValue(fileScanner, "point y");
            projectData.addPoint(new PlanePoint(xValue, yValue));
        }
    }

    private static void readCircles(Scanner fileScanner, ProjectData projectData, int circleCount) {
        for (int circleIndex = 0; circleIndex < circleCount; circleIndex++) {
            double centerX = readDoubleValue(fileScanner, "circle center x");
            double centerY = readDoubleValue(fileScanner, "circle center y");
            double radiusValue = readDoubleValue(fileScanner, "circle radius");
            projectData.addCircle(new PlaneCircle(new PlanePoint(centerX, centerY), radiusValue));
        }
    }

    private static int readNonNegativeInt(Scanner fileScanner, String valueName) {
        if (!fileScanner.hasNextInt()) {
            throw new IllegalArgumentException("Invalid " + valueName + ".");
        }
        int integerValue = fileScanner.nextInt();
        if (integerValue < 0) {
            throw new IllegalArgumentException("Negative " + valueName + " is forbidden.");
        }
        return integerValue;
    }

    private static double readDoubleValue(Scanner fileScanner, String valueName) {
        if (!fileScanner.hasNextDouble()) {
            throw new IllegalArgumentException("Invalid " + valueName + ".");
        }
        return fileScanner.nextDouble();
    }

    private static double parseDoubleToken(String tokenText, String valueName) {
        try {
            return Double.parseDouble(tokenText);
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Invalid " + valueName + ".");
        }
    }
}
