import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

public class DataFileManager {
    public static ProjectData loadFromFile(File sourceFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(sourceFile);
        fileScanner.useLocale(Locale.US);
        ProjectData projectData = TextDataParser.parseScanner(fileScanner);
        fileScanner.close();
        return projectData;
    }

    public static void saveToFile(File targetFile, ProjectData projectData)
            throws FileNotFoundException {
        PrintWriter fileWriter = new PrintWriter(targetFile);
        writePointSection(fileWriter, projectData);
        writeCircleSection(fileWriter, projectData);
        fileWriter.close();
    }

    private static void writePointSection(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println("POINTS " + projectData.getPointCount());
        for (PlanePoint pointData : projectData.getPointList()) {
            fileWriter.printf(Locale.US, "%.2f %.2f%n",
                    pointData.getXCoordinate(), pointData.getYCoordinate());
        }
    }

    private static void writeCircleSection(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println("CIRCLES " + projectData.getCircleCount());
        for (PlaneCircle circleData : projectData.getCircleList()) {
            fileWriter.printf(Locale.US, "%.2f %.2f %.2f%n",
                    circleData.getCenterPoint().getXCoordinate(),
                    circleData.getCenterPoint().getYCoordinate(),
                    circleData.getRadiusValue());
        }
    }
}
