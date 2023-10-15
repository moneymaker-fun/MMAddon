package de.timuuuu.moneymaker.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class InstallDialog extends JFrame {

  public InstallDialog() {
    setTitle("MM-Addon installer");
    setSize(300, 100);

    // Disable resizing
    setResizable(false);

    // Hide the window decorations (title bar)
    setUndecorated(false);

    // Set the default close operation
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JLabel label = new JLabel("Möchtest du das MoneyMaker Addon installieren?");
    JButton installButton = new JButton("Ja");
    JButton cancelButton = new JButton("Nein");

    installButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // The user clicked "Install," so copy the JAR file to a specific folder.
        try {
          Path source = Paths.get("your-application.jar");  // Replace with your JAR file path
          Path target = Paths.get("installation-folder/your-application.jar");  // Replace with the target folder

          Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

          JOptionPane.showMessageDialog(null, "Installation complete.");
        } catch (IOException ex) {
          JOptionPane.showMessageDialog(null, "Installation failed: " + ex.getMessage());
        }

        // Close the dialog after installation
        dispose();
      }
    });

    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // The user clicked "Cancel," so exit the application.
        System.exit(0);
      }
    });

    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout());
    panel.add(label);
    panel.add(installButton);
    panel.add(cancelButton);

    add(panel);
    setLocationRelativeTo(null);

    // Load the icon from resources and set it for the frame
    ImageIcon customIcon = new ImageIcon(
        Objects.requireNonNull(getClass().getResource("/assets/moneymaker/textures/icon.png")));
    setIconImage(customIcon.getImage());
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        InstallDialog dialog = new InstallDialog();
        dialog.setVisible(true);
      }
    });
  }
}
