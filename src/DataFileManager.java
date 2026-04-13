import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;

/**
 * Менеджер чтения и записи данных проекта в текстовый файл.
 * <p>
 * Работает в паре с {@link TextDataParser}: этот класс отвечает за I/O,
 * а парсер отвечает за разбор формата.
 */
public class DataFileManager {
    /**
     * Читает файл и возвращает распарсенные данные проекта.
     */
    public static ProjectData loadFromFile(File sourceFile) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(sourceFile);
        fileScanner.useLocale(Locale.US);
        ProjectData projectData = TextDataParser.parseScanner(fileScanner);
        fileScanner.close();
        return projectData;
    }

    /**
     * Сохраняет текущие точки и круги в текстовый файл.
     */
    public static void saveToFile(File targetFile, ProjectData projectData)
            throws FileNotFoundException {
        PrintWriter fileWriter = new PrintWriter(targetFile);
        writePointSection(fileWriter, projectData);
        writeCircleSection(fileWriter, projectData);
        fileWriter.close();
    }

    private static void writePointSection(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println("POINTS " + projectData.getPointCount());
        PlanePoint[] pointArray = projectData.getPointArray();
        for (int i = 0; i < pointArray.length; i++) {
            fileWriter.printf(Locale.US, "%.2f %.2f%n",
                    pointArray[i].getXCoordinate(), pointArray[i].getYCoordinate());
        }
    }

    private static void writeCircleSection(PrintWriter fileWriter, ProjectData projectData) {
        fileWriter.println("CIRCLES " + projectData.getCircleCount());
        PlaneCircle[] circleArray = projectData.getCircleArray();
        for (int i = 0; i < circleArray.length; i++) {
            fileWriter.printf(Locale.US, "%.2f %.2f %.2f%n",
                    circleArray[i].getCenterPoint().getXCoordinate(),
                    circleArray[i].getCenterPoint().getYCoordinate(),
                    circleArray[i].getRadiusValue());
        }
    }
}
