import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;

public class MainFrame extends JFrame {
    private final DrawingPanel drawingPanel;
    private ProjectData projectData = new ProjectData();
    private TriangleSearchResult searchResult = TriangleSearchResult.emptyResult();

    public MainFrame() {
        super("Типовой проект: точки, круги и лучший треугольник");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(120, 80, 1100, 750);
        setLayout(new BorderLayout());
        drawingPanel = new DrawingPanel(this, projectData);
        add(drawingPanel, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createBarActionItem("Keyboard input", this::readDataFromKeyboard));
        menuBar.add(createBarActionItem("Clear", this::clearProjectData));
        menuBar.add(createBarActionItem("Find triangle", this::findBestTriangle));
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createMenuItem("Open", this::openDataFile));
        fileMenu.add(createMenuItem("Save", this::saveDataFile));
        fileMenu.add(createMenuItem("Example 1", () -> loadExampleFile("data/example_1.txt")));
        fileMenu.add(createMenuItem("Example 2", () -> loadExampleFile("data/example_2.txt")));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", this::dispose));
        return fileMenu;
    }

    private JMenuItem createBarActionItem(String text, Runnable action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionEvent -> action.run());
        return menuItem;
    }

    private JMenuItem createMenuItem(String text, Runnable action) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionEvent -> action.run());
        return menuItem;
    }

    private void openDataFile() {
        JFileChooser fileChooser = createFileChooser("Choose data file");
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            applyProjectData(DataFileManager.loadFromFile(fileChooser.getSelectedFile()));
        } catch (FileNotFoundException | IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private void saveDataFile() {
        JFileChooser fileChooser = createFileChooser("Choose output file");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            DataFileManager.saveToFile(fileChooser.getSelectedFile(), projectData);
        } catch (FileNotFoundException exception) {
            showErrorMessage("Failed to save file.");
        }
    }

    private JFileChooser createFileChooser(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle(dialogTitle);
        return fileChooser;
    }

    private void readDataFromKeyboard() {
        JTextArea inputArea = new JTextArea(createInputTemplate(), 12, 40);
        int dialogResult = JOptionPane.showConfirmDialog(this, new JScrollPane(inputArea),
                "Enter one object per line", JOptionPane.OK_CANCEL_OPTION);
        if (dialogResult != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            applyProjectData(TextDataParser.parseKeyboardText(inputArea.getText()));
        } catch (IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private String createInputTemplate() {
        return "100 100\n200 100\n150 200\n340 170 40\n560 360 55\n";
    }

    private void loadExampleFile(String filePath) {
        try {
            applyProjectData(DataFileManager.loadFromFile(new File(filePath)));
        } catch (FileNotFoundException | IllegalArgumentException exception) {
            showErrorMessage("Failed to load example file.");
        }
    }

    private void clearProjectData() {
        projectData = new ProjectData();
        searchResult = TriangleSearchResult.emptyResult();
        drawingPanel.setProjectData(projectData);
        drawingPanel.setSearchResult(searchResult);
    }

    private void applyProjectData(ProjectData loadedProjectData) {
        projectData = loadedProjectData;
        searchResult = TriangleSearchResult.emptyResult();
        drawingPanel.setProjectData(projectData);
        drawingPanel.setSearchResult(searchResult);
    }

    private void findBestTriangle() {
        if (projectData.getPointCount() < 3) {
            showErrorMessage("Need at least 3 points.");
            return;
        }
        searchResult = TriangleSearcher.findBestTriangle(projectData);
        drawingPanel.setSearchResult(searchResult);
        showSearchResultDialog();
    }

    private void showSearchResultDialog() {
        if (!searchResult.hasTriangle()) {
            showErrorMessage("No valid triangle found.");
            return;
        }
        JOptionPane.showMessageDialog(this, buildResultText(),
                "Triangle result", JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildResultText() {
        return "Triangle perimeter: " + String.format("%.2f", searchResult.getPerimeterValue())
                + "\nOutside circles: " + searchResult.getOutsideCircleCount();
    }

    public void notifyAboutDataChange() {
        searchResult = TriangleSearchResult.emptyResult();
        drawingPanel.setSearchResult(searchResult);
    }

    private void showErrorMessage(String messageText) {
        JOptionPane.showMessageDialog(this, messageText, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
