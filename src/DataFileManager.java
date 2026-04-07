import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class DataFileManager {
    public static ProjectData loadFromFile(File sourceFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(sourceFile);
        fileScanner.useLocale(Locale.US);
        ProjectData projectData = TextDataParser.parseFileScanner(fileScanner);
        fileScanner.close();
        return projectData;
    }

    public static void saveToFile(File targetFile, ProjectData projectData)
            throws FileNotFoundException {
        PrintWriter fileWriter = new PrintWriter(targetFile);
        writePoints(fileWriter, projectData);
        writeCircles(fileWriter, projectData);
        fileWriter.close();
    }

    private static void writePoints(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println(projectData.getPointCount());
        for (int pointIndex = 0; pointIndex < projectData.getPointCount(); pointIndex++) {
            PlanePoint pointData = projectData.getPointAt(pointIndex);
            fileWriter.printf(Locale.US, "%.2f %.2f%n", pointData.getXCoordinate(), pointData.getYCoordinate());
        }
    }

    private static void writeCircles(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println(projectData.getCircleCount());
        for (int circleIndex = 0; circleIndex < projectData.getCircleCount(); circleIndex++) {
            PlaneCircle circleData = projectData.getCircleAt(circleIndex);
            fileWriter.printf(Locale.US, "%.2f %.2f %.2f%n",
                    circleData.getCenterPoint().getXCoordinate(),
                    circleData.getCenterPoint().getYCoordinate(),
                    circleData.getRadiusValue());
        }
    }
}
