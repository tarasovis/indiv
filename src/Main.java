import javax.swing.SwingUtilities;

/**
 * Точка входа приложения.
 * <p>
 * Класс максимально маленький: его задача только безопасно запустить
 * создание Swing-окна в EDT-потоке.
 */
public class Main {
    /**
     * Запускает создание главного окна в EDT-потоке Swing.
     */
    public static void main(String[] commandLineArguments) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
