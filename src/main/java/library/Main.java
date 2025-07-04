package library;

import library.ui.LoginFrame;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Iterate through the installed look and feels
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                // If "Nimbus" is found, set it as the look and feel
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break; // Exit the loop once Nimbus is set
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, the application will use the default look and feel.
            // You can handle the exception here, for instance, by logging it.
            e.printStackTrace();
        }

        // Schedule the creation of the GUI on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}