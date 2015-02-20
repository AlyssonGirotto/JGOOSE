package br.unioeste.jgoose.view;

import com.mxgraph.util.mxResources;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

/**
 * @author Diego Peliser
 * @author Leonardo Merlin
 */
public class AboutDialogView extends JDialog {

    private static final long serialVersionUID = -3378029138434324390L;

    /**
     *
     * @param owner parent
     */
    public AboutDialogView(Frame owner) {
        super(owner);
        setTitle(mxResources.get("aboutEditor"));
        setLayout(new BorderLayout());

        // Creates the gradient panel
        JPanel panel = new JPanel(new BorderLayout()) {
            private static final long serialVersionUID = -5062895855016210947L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Paint gradient background
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new GradientPaint(0, 0, Color.WHITE, getWidth(),
                        0, getBackground()));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(8, 8, 12, 8)));

        // Adds title
        JLabel titleLabel = new JLabel("JGOOSE");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Adds optional subtitle
        JLabel subtitleLabel = new JLabel("Java Goal Into Object Oriented Standard Extension - Versão 2013");
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        subtitleLabel.setOpaque(false);
        panel.add(subtitleLabel, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        content.add(new JLabel("Unioeste - Universidade Estadual do Oeste do Paraná"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Center of Exact Sciences and Technology"));
        content.add(new JLabel("College of Computer Science"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Development of the 2013 Version by:"));
        content.add(new JLabel("   Diego Peliser"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Development of the 2011 Version by:"));
        content.add(new JLabel("   Mauro Brischke"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Development by:"));
        content.add(new JLabel("   André Abe Vicente"));
        content.add(new JLabel(" "));
        content.add(new JLabel("Leader:"));
        content.add(new JLabel("   Prof. Dr. Victor Francisco Araya Santander"));

        getContentPane().add(content, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        // Adds OK button to close window
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        buttonPanel.add(closeButton);

        // Sets default button for enter key
        getRootPane().setDefaultButton(closeButton);

        setResizable(false);
        setSize(400, 400);
    }

    /**
     * Overrides {@link JDialog#createRootPane()} to return a root pane that
     * hides the window when the user presses the ESCAPE key.O
     *
     * @return
     */
    @Override
    protected JRootPane createRootPane() {
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }
}
