import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final DrawPanel drawPanel;

    public Main() {
        super("Треугольник и круги");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 680);
        setLocationRelativeTo(null);
        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);
        setJMenuBar(createMenuBar());
        setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createToolsMenu());
        return menuBar;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("Файл");
        menu.add(item("Открыть", e -> openFile()));
        menu.add(item("Сохранить", e -> saveFile()));
        menu.add(item("Пример 1", e -> drawPanel.setData(FileManager.example1())));
        menu.add(item("Пример 2", e -> drawPanel.setData(FileManager.example2())));
        menu.addSeparator();
        menu.add(item("Выход", e -> System.exit(0)));
        return menu;
    }

    private JMenu createToolsMenu() {
        JMenu menu = new JMenu("Инструменты");
        menu.add(item("Ввод с клавиатуры", e -> keyboardInput()));
        menu.add(item("Очистить", e -> drawPanel.clearAll()));
        menu.add(item("Найти треугольник", e -> findTriangle()));
        return menu;
    }

    private JMenuItem item(String title, java.awt.event.ActionListener action) {
        JMenuItem menuItem = new JMenuItem(title);
        menuItem.addActionListener(action);
        return menuItem;
    }

    private void keyboardInput() {
        String text = JOptionPane.showInputDialog(this,
                "Введите по строкам:\nточка: x y\nкруг: x y r");
        if (text == null || text.isBlank()) {
            return;
        }
        InputParser.applyLines(text, drawPanel.getPoints(), drawPanel.getCircles());
        drawPanel.dropSolution();
        drawPanel.repaint();
    }

    private void findTriangle() {
        TriangleSearchResult result = GeometryUtils.findBestTriangle(
                drawPanel.getPoints(), drawPanel.getCircles());
        drawPanel.applySearchResult(result);
        showResultDialog(result);
    }

    private void showResultDialog(TriangleSearchResult result) {
        if (result.triangle() == null) {
            JOptionPane.showMessageDialog(this, "Подходящий треугольник не найден.");
            return;
        }
        String message = String.format("Периметр: %.2f\nКругов снаружи: %d",
                result.perimeter(), result.outsideCount());
        JOptionPane.showMessageDialog(this, message);
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser(".");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        drawPanel.setData(FileManager.load(chooser.getSelectedFile()));
    }

    private void saveFile() {
        JFileChooser chooser = new JFileChooser(".");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        FileManager.save(chooser.getSelectedFile(), drawPanel.getPoints(), drawPanel.getCircles());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
