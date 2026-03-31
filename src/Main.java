import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] commandLineArguments) {
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
