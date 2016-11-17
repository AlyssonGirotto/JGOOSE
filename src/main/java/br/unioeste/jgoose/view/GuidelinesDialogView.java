package br.unioeste.jgoose.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

/**
 *
 * @author Leonardo Merlin - leonardo.merlin at unioeste.br
 */
public class GuidelinesDialogView extends JDialog {

    public GuidelinesDialogView(Frame frame) throws BadLocationException {
        super(frame);
        //@TODO: internationalize the title of guidelines dialog.
        setTitle("JOOSE - Guidelines");
        setLayout(new BorderLayout());
        // Creates the gradient panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(8, 8, 12, 8)));
        // Add title
        //@TODO: internationalize this.
        JLabel titleLabel = new JLabel("JGOOSE - Guidelines");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);
        // Adds optional subtitle
        // @TODO: address to be defined or removed.
        //JLabel subtitleLabel = new JLabel(
        //"For more information visit http://www.unioeste.br/jgoose");
        //subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        //subtitleLabel.setOpaque(false);
        //panel.add(subtitleLabel, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.NORTH);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(new JLabel("JGOOSE - Guidelines"));
        //@TODO: organize the software version. Version Management?
        content.add(new JLabel("Version: 0.4.1-2013"));
        content.add(new JLabel(" "));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(40);
        textArea.setWrapStyleWord(true);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        Document document = new DefaultStyledDocument();
        int offset;
        offset = document.getEndPosition().getOffset();
        document.insertString(offset, "STEP 1 - DISCOVERING SYSTEM ACTORS\n\n"
                + " - Guideline 1: every i* actor is a candidate to be mapped to a use case actor.\n"
                + " - Guideline 2: the candidate i* actor should be external to the intended software system, otherwise, it cannot be mapped to a use case actor.\n"
                + " - Guideline 3: the candidate i* actor should have least one dependency with the intended software system actor, otherwise, it cannot be mapped to a use case actor:\n"
                + " - Guideline 4: actors in i*, related through the ISA mechanism in the organizational diagrams and mapped individually to actors in use cases (applying guidelines 1, 2 and 3), will be related in the use case diagrams through the <<generalization>> relationship.\n"
                + "\nSTEP 2 - DISCOVERING USE CASES FOR THE ACTORS\n\n"
                + " - Guideline 5: for each discovered actor of the system (step 1), we should observe all the dependencies between the system-to-be and the actor in which the discovered actor is a dependee, looking for use cases for the actor.\n"
                + "   - Guideline 5.1: goal dependencies – goals in i* can be mapped to use case goals.\n"
                + "   - Guideline 5.2: task dependencies – if an actor depends on another actor for the accomplishment of a task, it should be investigated if this task needs to be decomposed into subtasks.\n"
                + "   - Guideline 5.3: resources dependencies – if an actor depends on another actor for obtaining a resource(s), why is it required? If there is a more abstract goal, this goal will be a candidate to be the goal of the use case for the actor.\n"
                + "   - Guideline 5.4: softgoal dependencies – typically, the softgoal dependency in i* is a nonfunctional requirement for the intended system. Hence, a softgoal does not represent a use case of the system, but a nunfunctional requirement associated with a specific use case of the system or with the system as a whole.\n"
                + " - Guideline 6: analyze special situations in which an actor (discovered by following step 1) possesses dependencies in relation to an i* actor representing the system-to-be or part of it. There dependencies usually generate use cases.\n"
                + "\nSTEP 3 - DISCOVERING AND DESCRIBING SCENARIOS OF USE CASES\n\n"
                + " - Guideline 8: analyze each actor and its relationships in the SR diagram to extract information that can lead to the description of main and alternate flows as well as pre-conditions and post-conditions of the Use Cases discovered for the actor.\n"
                + "   - Guideline 8.1: analyze the sub-components in a task decomposition link in a possible mapping for the steps in the description of the primary scenario (main stream) of Use Case.\n"
                + "   - Guideline 8.2: analyze the means-end links in a possible mapping for alternative steps in the description of Use Cases.\n"
                + "   - Guideline 8.3: analyze the relationships of sub-system actors dependencies. These dependencies can cause pre-conditions and post-conditions for use cases discovered.\n"
                + " - Guideline 10: create the use case diagram using the discovered use cases and actors, as well as the three UML structuring mechanisms: <<include>>, <<extend>> and <<generalization>>.\n", null);

        textArea.setDocument(document);
        //getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(jScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        // Adds OK button to close window
        // @TODO: internationalize close button name.
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
        this.setResizable(false);
        this.setSize(840, 620);
    }

    public GuidelinesDialogView(Frame frame, boolean bpmn) throws BadLocationException {
        super(frame);
        //@TODO: internationalize the title of guidelines dialog.
        setTitle("BP2UC - Guidelines");
        setLayout(new BorderLayout());
        // Creates the gradient panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(0, 0, 1, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(8, 8, 12, 8)));
        // Add title
        //@TODO: internationalize this.
        JLabel titleLabel = new JLabel("BP2UC - Guidelines");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        titleLabel.setOpaque(false);
        panel.add(titleLabel, BorderLayout.NORTH);
        // Adds optional subtitle
        // @TODO: address to be defined or removed.
        //JLabel subtitleLabel = new JLabel(
        //"For more information visit http://www.unioeste.br/jgoose");
        //subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 18, 0, 0));
        //subtitleLabel.setOpaque(false);
        //panel.add(subtitleLabel, BorderLayout.CENTER);

        getContentPane().add(panel, BorderLayout.NORTH);
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        content.add(new JLabel("JGOOSE - Guidelines"));
        //@TODO: organize the software version. Version Management?
        content.add(new JLabel("Version: 0.4.1-2013"));
        content.add(new JLabel(" "));
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setColumns(40);
        textArea.setWrapStyleWord(true);
        JScrollPane jScrollPane = new JScrollPane(textArea);
        Document document = new DefaultStyledDocument();
        int offset;
        offset = document.getEndPosition().getOffset();
        document.insertString(offset, "STEP 1 - DISCOVERING ACTORS\n\n"
                + " - DRD1: every pool of the BPMN model is mapped to a Use Case actor.\n"
                + " - DRD2: every lane is mapped to a Use Case actor. The actor will be a specialization of the actor obtained from the pool.\n"                
                + "\nSTEP 2 - DISCOVERING USE CASES\n\n"
                + " - DRD3: each start event that does not have a message flow and which belongs to the main process will result in an execution instance of the derivation process.\n"
                + " - DRD4: if the currently analyzed element has been evaluated and is not an activity that has message flow receipt, the current instance must be terminated.\n"
                + " - DRD5: if the currently analyzed element is a task that has not been evaluated and the currently analyzed instance has not been originated by a message flow, a Use Case will be generated. The Use Case name will be the name of the task.\n"                
                + " - DRD6: if the currently analyzed element is a task that has not been evaluated and the instance has been originated by a message flow, a Use Case with the same task name will be created. In the Included Use Cases field must be entered the name of the Use Case obtained from the originator activity of the current instance.\n"
                + " - DRD7: if the currently analyzed element is a subprocess that has not been evaluated, a Use Case will be generated in the Use Case Diagram. The use case name will be the name of the subprocess.\n"
                + " - DRD8: if the currently analyzed element is an activity (A1) that has been evaluated and that comes from an instance originated from the message flow, you must enter in the field Included Use Cases the name of the Use Case generated by the originating activity of the current instance.\n"
                + " - DRD9: if the currently analyzed element is an end event, the execution of the current instance must be finished.\n"
                + " - DRD10: if there are multiple options to continue the execution flow, you must end the current instance and insert one analysis instance for each output.\n"
                + " - DRD11: if the currently analyzed element does not connect within the guidelines specified above, you should analyze the next element.\n"
                + "\nSTEP 3 - ASSOCIATING ACTORS AND USE CASES\n\n"                
                + " - DRD12: a Use Case will be associated with the actor representing the Lane or Pool in which the activity that originated it is inserted.\n"
                + " - DRD13: if an activity (A1) has message flow for an activity of another participant (P1), the Use Case representing A1 will be associated with the actor originated from P1.\n"
                + " - DRD14: each Use Case originated from an instance (I1) that has the SP marker, will be included in the Use Case obtained by the activity that originated I1.", null);

        textArea.setDocument(document);
        //getContentPane().add(textArea, BorderLayout.CENTER);
        getContentPane().add(jScrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createMatteBorder(1, 0, 0, 0, Color.GRAY), BorderFactory
                .createEmptyBorder(16, 8, 8, 8)));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        // Adds OK button to close window
        // @TODO: internationalize close button name.
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
        this.setResizable(false);
        this.setSize(840, 620);
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
        JRootPane resultRootPane = new JRootPane();
        resultRootPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setVisible(false);
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return resultRootPane;
    }
}
