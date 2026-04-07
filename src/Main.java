import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] commandLineArguments) {
        // Создаем окно в EDT, чтобы все Swing-обработчики работали корректно.
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
