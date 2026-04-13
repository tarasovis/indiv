import javax.swing.SwingUtilities;

/**
 * Точка входа приложения.
 */
public class Main {
    public static void main(String[] commandLineArguments) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
