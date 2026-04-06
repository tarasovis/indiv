import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Main extends JFrame {
    private final DrawPanel drawPanel;

    public Main() {
        super("Triangle and circles");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        drawPanel = new DrawPanel();
        add(drawPanel, BorderLayout.CENTER);
        setJMenuBar(buildMenuBar());
        setVisible(true);
    }

    private JMenuBar buildMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(buildFileMenu());
        menuBar.add(buildKeyboardInputItem());
        menuBar.add(buildClearItem());
        menuBar.add(buildFindItem());
        return menuBar;
    }

    private JMenu buildFileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(menuItem("Open", e -> openFromFile()));
        fileMenu.add(menuItem("Save", e -> saveToFile()));
        fileMenu.add(menuItem("Example 1", e -> loadExample(1)));
        fileMenu.add(menuItem("Example 2", e -> loadExample(2)));
        fileMenu.addSeparator();
        fileMenu.add(menuItem("Exit", e -> System.exit(0)));
        return fileMenu;
    }

    private JMenuItem buildKeyboardInputItem() {
        return menuItem("Keyboard input", e -> processKeyboardInput());
    }

    private JMenuItem buildClearItem() {
        return menuItem("Clear", e -> drawPanel.clearAll());
    }

    private JMenuItem buildFindItem() {
        return menuItem("Find triangle", e -> findTriangle());
    }

    private JMenuItem menuItem(String title, java.awt.event.ActionListener action) {
        JMenuItem item = new JMenuItem(title);
        item.addActionListener(action);
        return item;
    }

    private void processKeyboardInput() {
        String text = JOptionPane.showInputDialog(this,
                "Enter data line by line:\npoint: x y\ncircle: x y r");
        if (text == null || text.isBlank()) {
            return;
        }
        InputParser.applyLines(text, drawPanel.getPoints(), drawPanel.getCircles());
        drawPanel.clearResult();
        drawPanel.repaint();
    }

    private void findTriangle() {
        TriangleSearchResult result = GeometryUtils.findBestTriangle(
                drawPanel.getPoints(), drawPanel.getCircles());
        drawPanel.setResultTriangle(result.triangle());
        showResultDialog(result);
        drawPanel.repaint();
    }

    private void showResultDialog(TriangleSearchResult result) {
        if (result.triangle() == null) {
            JOptionPane.showMessageDialog(this, "No valid triangle found.");
            return;
        }
        String message = String.format("Perimeter: %.2f\nOutside circles: %d",
                result.perimeter(), result.outsideCount());
        JOptionPane.showMessageDialog(this, message);
    }

    private void openFromFile() {
        JFileChooser chooser = new JFileChooser(".");
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        DataSet data = FileManager.load(chooser.getSelectedFile());
        drawPanel.setData(data);
    }

    private void saveToFile() {
        JFileChooser chooser = new JFileChooser(".");
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File target = chooser.getSelectedFile();
        FileManager.save(target, drawPanel.getPoints(), drawPanel.getCircles());
    }

    private void loadExample(int index) {
        DataSet data = index == 1 ? FileManager.example1() : FileManager.example2();
        drawPanel.setData(data);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
