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
        menuBar.add(createActionMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("Файл");
        fileMenu.add(createMenuItem("Открыть...", this::openDataFile));
        fileMenu.add(createMenuItem("Сохранить...", this::saveDataFile));
        fileMenu.add(createMenuItem("Пример 1", () -> loadExampleFile("data/example_1.txt")));
        fileMenu.add(createMenuItem("Пример 2", () -> loadExampleFile("data/example_2.txt")));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Выход", this::dispose));
        return fileMenu;
    }

    private JMenu createActionMenu() {
        JMenu actionMenu = new JMenu("Действия");
        actionMenu.add(createMenuItem("Ввод с клавиатуры", this::readDataFromKeyboard));
        actionMenu.add(createMenuItem("Очистить", this::clearProjectData));
        actionMenu.add(createMenuItem("Найти лучший треугольник", this::findBestTriangle));
        return actionMenu;
    }

    private JMenuItem createMenuItem(String itemText, Runnable action) {
        JMenuItem menuItem = new JMenuItem(itemText);
        menuItem.addActionListener(actionEvent -> action.run());
        return menuItem;
    }

    private void openDataFile() {
        JFileChooser fileChooser = createFileChooser("Выберите файл с данными");
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
        JFileChooser fileChooser = createFileChooser("Укажите файл для сохранения");
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            DataFileManager.saveToFile(fileChooser.getSelectedFile(), projectData);
        } catch (FileNotFoundException exception) {
            showErrorMessage("Не удалось сохранить файл.");
        }
    }

    private JFileChooser createFileChooser(String dialogTitle) {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle(dialogTitle);
        return fileChooser;
    }

    private void readDataFromKeyboard() {
        JTextArea inputArea = new JTextArea(createInputTemplate(), 14, 40);
        JScrollPane scrollPane = new JScrollPane(inputArea);
        int dialogResult = JOptionPane.showConfirmDialog(
                this,
                scrollPane,
                "Введите данные в текстовом виде",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (dialogResult != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            applyProjectData(TextDataParser.parseText(inputArea.getText()));
        } catch (IllegalArgumentException exception) {
            showErrorMessage(exception.getMessage());
        }
    }

    private String createInputTemplate() {
        return "POINTS 3\n"
                + "-150 40\n"
                + "40 140\n"
                + "120 -80\n"
                + "CIRCLES 2\n"
                + "-200 -60 30\n"
                + "170 100 35\n";
    }

    private void loadExampleFile(String filePath) {
        try {
            applyProjectData(DataFileManager.loadFromFile(new File(filePath)));
        } catch (FileNotFoundException | IllegalArgumentException exception) {
            showErrorMessage("Не удалось загрузить пример: " + filePath);
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
            showErrorMessage("Для поиска треугольника нужно минимум 3 точки.");
            return;
        }
        searchResult = TriangleSearcher.findBestTriangle(projectData);
        drawingPanel.setSearchResult(searchResult);
        SwingUtilities.invokeLater(this::showSearchResultDialog);
    }

    private void showSearchResultDialog() {
        if (!searchResult.hasTriangle()) {
            showErrorMessage("Не удалось найти корректный треугольник.");
            return;
        }
        JOptionPane.showMessageDialog(this, buildResultText(), "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
    }

    private String buildResultText() {
        return "Снаружи кругов: " + searchResult.getOutsideCircleCount()
                + "\nПериметр: " + String.format("%.2f", searchResult.getPerimeterValue());
    }

    public void notifyAboutDataChange() {
        searchResult = TriangleSearchResult.emptyResult();
        drawingPanel.setSearchResult(searchResult);
    }

    public void updateMousePoint(PlanePoint mousePoint) {
        // Строка состояния удалена по требованию интерфейса.
    }

    private void showErrorMessage(String messageText) {
        JOptionPane.showMessageDialog(this, messageText, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
