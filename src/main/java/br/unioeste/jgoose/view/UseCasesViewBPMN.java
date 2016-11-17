package br.unioeste.jgoose.view;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.controller.BPMNController;
import br.unioeste.jgoose.controller.Controller;
import br.unioeste.jgoose.controller.EditorWindowListener;
import br.unioeste.jgoose.e4j.filters.ShapeFilenameFilter;
import br.unioeste.jgoose.e4j.swing.BasicUseCasesEditor;
import br.unioeste.jgoose.e4j.swing.EditorJFrame;
import br.unioeste.jgoose.model.FiltroDOC;
import br.unioeste.jgoose.model.UCActor;
import br.unioeste.jgoose.model.UCUseCase;
import br.unioeste.jgoose.util.IStarUtils;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.shape.mxStencilShape;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.w3c.dom.Element;

/**
 *
 * @author Alysson Girotto
 */

public class UseCasesViewBPMN extends javax.swing.JFrame {

    private Actor selectedActor = null;
    private String selectedCase = "";
    private EditorJFrame e4jInstance = null;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("console");

    /**
     * Creates new form UseCasesView
     */
    public UseCasesViewBPMN() {
        initComponents();
        setLocationRelativeTo(null);
        buttonDelete.setEnabled(false);
        Image Icone;
        Icone = Toolkit.getDefaultToolkit().getImage("./src/main/resources/icons/usecases_32x32.png");
        setIconImage(Icone);
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
    }

    /*
     * Método que atualiza a Tabela de Casos de Uso
     */
    protected void updateTable() {
        DefaultTableModel tabCasosDeUso = new DefaultTableModel();
        tabCasosDeUso.addColumn("ID"); // 0
        tabCasosDeUso.addColumn("Use Case");
        tabCasosDeUso.addColumn("Primary actor"); // 2
        tabCasosDeUso.addColumn("Secondary actors"); // 3       
        tabCasosDeUso.addColumn("Included");
        tabCasosDeUso.addColumn("Guideline"); // 5
        String vetCasosDeUso[] = new String[6];
        int cont = 1;
        
        // adiciona os dados dos casos de uso no modelo da tabela
        for(UCUseCase useCase : BPMNController.getUseCases()){
            vetCasosDeUso[0] = "" + cont++;
            vetCasosDeUso[1] = useCase.getName();
                                    
            vetCasosDeUso[2] = useCase.getPrimaryActor().getName();
            
            String actors = "";
            // Percorre os atores, identificando atores associados ao atual
            for(UCActor actor : useCase.getSecondaryActors()){
                actors += actor.getName() + "; ";
            }
            vetCasosDeUso[3] = actors;
            
            String includedUseCases = "";
            // Percorre os casos de usos incluídos
            for(UCUseCase useCaseIncluded : useCase.getIncludedUseCases()){
                includedUseCases += (useCaseIncluded.getCode()+1) + " - " + useCaseIncluded.getName() + "; ";
            }
            
            vetCasosDeUso[4] = includedUseCases;
            
            vetCasosDeUso[5] = useCase.getGuidelineUsed();
            
            tabCasosDeUso.addRow(vetCasosDeUso);
        }                       
        
        tabelUseCases.setModel(tabCasosDeUso);
        // seta a largura das colunas da tabela
        tabelUseCases.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelUseCases.getColumnModel().getColumn(1).setPreferredWidth(170);
        tabelUseCases.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabelUseCases.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelUseCases.getColumnModel().getColumn(4).setPreferredWidth(140);
        tabelUseCases.getColumnModel().getColumn(5).setPreferredWidth(90);
    }

    /*
     * Método que insere um texto na área de texto de Casos de Uso com formatação
     */
    private void insertStyle(String text, SimpleAttributeSet style) {
        Document doc = textUseCases.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void insertStyleSave(String text, SimpleAttributeSet style) {
        Document doc = textUseCasesSave.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void tabelUseCasesValueChanged(ListSelectionEvent evt) {

        buttonDelete.setEnabled(true);
        buttonDiagram.setEnabled(true);
        
        int linha = tabelUseCases.getSelectedRow();
        if (linha != -1) {
            labelCasosDeUso.setText("Use Case Specification");
            textUseCases.setText(""); // limpa o painel de texto
            
            // define o objeto com formatação negrito negrito
            SimpleAttributeSet negrito = new SimpleAttributeSet();
            StyleConstants.setBold(negrito, true);
            
            // insere os dados do caso de uso selecionado
            UCUseCase useCase = BPMNController.getUseCases().get(linha);
            
            insertStyle("Use Case", negrito);
            insertStyle(": " + useCase.getDescription().getName() +  "\n\n", null);
            
            insertStyle("CHARACTERISTIC INFORMATION\n", negrito);
            insertStyle("	Goal in Context", negrito);
            insertStyle(": " + useCase.getDescription().getGoal() + "\n", null);
            insertStyle("	Scope", negrito);
            insertStyle(": " + useCase.getDescription().getScope() + "\n", null);
            insertStyle("	Preconditions", negrito);
            insertStyle(": " + useCase.getDescription().getPreConditions() +  "\n", null);
            insertStyle("	Success End Condition", negrito);
            insertStyle(": " + useCase.getDescription().getEndSucess() +  "\n", null);
            insertStyle("	Failed End Condition", negrito);
            insertStyle(": " + useCase.getDescription().getEndFailure() + "\n", null);
            insertStyle("	Trigger", negrito);
            insertStyle(": " + useCase.getDescription().getTrigger() + "\n", null);
            insertStyle("	Primary actor", negrito);
            insertStyle(": " + useCase.getDescription().getPrimaryActor() + "\n", null);
            
            insertStyle("\nMAIN SUCCESS SCENARIO\n", negrito);  
            int i = 1;
            for(String sentence : useCase.getDescription().getScenario()){
                insertStyle("<passo " + i++ + "> - " + sentence + "\n", null);
            }
            insertStyle("\n", null);
            
            insertStyle("EXTENSIONS\n", negrito);              
            insertStyle("\n", null);
            
            insertStyle("RELATED INFORMATION\n", negrito);                        
            insertStyle("	Priority", negrito);
            insertStyle(": " + useCase.getDescription().getPriority()+ "\n", null);
            insertStyle("	Target performance", negrito);
            insertStyle(": " + useCase.getDescription().getPerformance()+ "\n", null);
            insertStyle("	Frequency", negrito);
            insertStyle(": " + useCase.getDescription().getFrequency()+ "\n", null);
            insertStyle("	Use Case father", negrito);
            insertStyle(": " + useCase.getDescription().getUseCaseFather() + "\n", null);
            insertStyle("	Included Use Cases", negrito);
            insertStyle(": " + useCase.getDescription().getIncludedUseCases()+ "\n", null);
            insertStyle("	Secondary actors", negrito);
            insertStyle(": " + useCase.getDescription().getSecondaryActors()+ "\n", null);
            insertStyle("	Additional information", negrito);
            insertStyle(": " + useCase.getDescription().getAdditionalInformation()+ "\n", null);                                  
        } else {
            textUseCases.setText("");
        }
    }

    /**
     * Abre uma janela GuidelinesDialogView
     */
    private void showGuidelinesDialog() {        
        GuidelinesDialogView diretrizes;
        try {
            diretrizes = new GuidelinesDialogView(this, true);
            diretrizes.setModal(true);

            int x = this.getX() + (this.getWidth() - diretrizes.getWidth()) / 2;
            int y = this.getY() + (this.getHeight() - diretrizes.getHeight()) / 2;
            diretrizes.setLocation(x, y);

            diretrizes.setVisible(true);
        } catch (BadLocationException ex) {
            LOG.error(ex);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CasosDeUsoMapeados = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelUseCases = new javax.swing.JTable(){
            @Override
            public boolean isCellEditable(int row, int column){
                return false;
            }
        };
        labelCasosDeUso = new javax.swing.JLabel();
        buttonDiagram = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        textUseCases = new javax.swing.JTextPane();
        buttonGuidelines = new javax.swing.JButton();
        buttonDelete = new javax.swing.JButton();
        buttonSaveUseCases = new javax.swing.JButton();
        buttonSaveDescription = new javax.swing.JButton();
        menuUseCases = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        fileSaveUseCases = new javax.swing.JMenuItem();
        fileSaveDescription = new javax.swing.JMenuItem();
        menuAjuda = new javax.swing.JMenu();
        helpGuidelines = new javax.swing.JMenuItem();
        helpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Use Cases");
        setResizable(false);

        CasosDeUsoMapeados.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        CasosDeUsoMapeados.setText("Use Cases Mapped");

        tabelUseCases.setAutoCreateRowSorter(true);
        tabelUseCases.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Actor", "Use Case", "Type"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelUseCases.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabelUseCases.getTableHeader().setReorderingAllowed(false);

        tabelUseCases.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent evt) {
                tabelUseCasesValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(tabelUseCases);
        if (tabelUseCases.getColumnModel().getColumnCount() > 0) {
            tabelUseCases.getColumnModel().getColumn(0).setResizable(false);
            tabelUseCases.getColumnModel().getColumn(1).setResizable(false);
            tabelUseCases.getColumnModel().getColumn(2).setResizable(false);
            tabelUseCases.getColumnModel().getColumn(3).setResizable(false);
        }

        labelCasosDeUso.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelCasosDeUso.setText("Specification of the Use Case");

        buttonDiagram.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonDiagram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/usecase_diagram_wiz.gif"))); // NOI18N
        buttonDiagram.setText("Diagram");
        buttonDiagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDiagramActionPerformed(evt);
            }
        });

        textUseCases.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jScrollPane3.setViewportView(textUseCases);

        buttonGuidelines.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonGuidelines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/guidelines_32x32.png"))); // NOI18N
        buttonGuidelines.setText("Guidelines");
        buttonGuidelines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonGuidelinesActionPerformed(evt);
            }
        });

        buttonDelete.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete_32x32.png"))); // NOI18N
        buttonDelete.setText("Delete");
        buttonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteActionPerformed(evt);
            }
        });

        buttonSaveUseCases.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonSaveUseCases.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save_32x32.png"))); // NOI18N
        buttonSaveUseCases.setText("Save Use Cases");
        buttonSaveUseCases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveUseCasesActionPerformed(evt);
            }
        });

        buttonSaveDescription.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonSaveDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save_32x32.png"))); // NOI18N
        buttonSaveDescription.setText("Save Description");
        buttonSaveDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveDescriptionActionPerformed(evt);
            }
        });

        menuFile.setText("File");

        fileSaveUseCases.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save_16x16.png"))); // NOI18N
        fileSaveUseCases.setText("Save Use Cases");
        fileSaveUseCases.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveUseCasesActionPerformed(evt);
            }
        });
        menuFile.add(fileSaveUseCases);

        fileSaveDescription.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/save_16x16.png"))); // NOI18N
        fileSaveDescription.setText("Save Description");
        fileSaveDescription.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileSaveDescriptionActionPerformed(evt);
            }
        });
        menuFile.add(fileSaveDescription);

        menuUseCases.add(menuFile);

        menuAjuda.setText("Help");

        helpGuidelines.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        helpGuidelines.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/guidelines_16x16.png"))); // NOI18N
        helpGuidelines.setText("Guidelines");
        helpGuidelines.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpGuidelinesActionPerformed(evt);
            }
        });
        menuAjuda.add(helpGuidelines);

        helpAbout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        helpAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/info_16x16.png"))); // NOI18N
        helpAbout.setText("About");
        helpAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpAboutActionPerformed(evt);
            }
        });
        menuAjuda.add(helpAbout);

        menuUseCases.add(menuAjuda);

        setJMenuBar(menuUseCases);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(CasosDeUsoMapeados)
                            .addComponent(labelCasosDeUso)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(buttonSaveUseCases, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(buttonSaveDescription, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonDiagram)
                                        .addComponent(buttonDelete, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(buttonGuidelines, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonDelete, buttonDiagram, buttonGuidelines});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(CasosDeUsoMapeados)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelCasosDeUso)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(buttonDiagram, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonGuidelines, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(144, 144, 144))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonSaveDescription)
                            .addComponent(buttonSaveUseCases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void helpGuidelinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpGuidelinesActionPerformed
        buttonDelete.setEnabled(false);
        this.showGuidelinesDialog();
    }//GEN-LAST:event_helpGuidelinesActionPerformed

    private void helpAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpAboutActionPerformed
        buttonDelete.setEnabled(false);
        AboutDialogView about;
        about = new AboutDialogView(this);
        about.setModal(true);
        int x = this.getX() + (this.getWidth() - about.getWidth()) / 2;
        int y = this.getY() + (this.getHeight() - about.getHeight()) / 2;
        about.setLocation(x, y);
        about.setVisible(true);
    }//GEN-LAST:event_helpAboutActionPerformed

    private void fileSaveDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveDescriptionActionPerformed
        this.saveDescription();
    }//GEN-LAST:event_fileSaveDescriptionActionPerformed

    private void buttonGuidelinesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonGuidelinesActionPerformed
        buttonDelete.setEnabled(false);
        this.showGuidelinesDialog();
    }//GEN-LAST:event_buttonGuidelinesActionPerformed

    private void fileSaveUseCasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileSaveUseCasesActionPerformed
        this.showSaveUseCases();
    }//GEN-LAST:event_fileSaveUseCasesActionPerformed

    private void buttonDiagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDiagramActionPerformed
        buttonDelete.setEnabled(false);
        try {
            this.showUseCasesDiagram();
        } catch (HeadlessException ex) {
            Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDiagramActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
        JOptionPane.showMessageDialog(null, "Not implemented yet");
        /*
        buttonDiagram.setEnabled(false);
        int linha = tabelUseCases.getSelectedRow();
        Actor actor = Controller.getActor("" + tabelUseCases.getValueAt(linha, 1));
        String casoSelecionado = "" + tabelUseCases.getValueAt(linha, 2);
        int c = JOptionPane.showConfirmDialog(null, "Confirm the deletion of the Use Case " + casoSelecionado + "?", "Deletion of Use Case", JOptionPane.YES_NO_OPTION);
        if (c == 0) {
            int n = actor.getUseCases().size();
            UseCase caso;
            System.out.println(" N: " + n);
            for (int i = 0; i < n; i++) {
                System.out.println("i = " + i);
                caso = actor.getUseCases().get(i);
                if (caso.getName().equals(casoSelecionado)) {
                    actor.getUseCases().remove(i);
                    updateTable();
                    buttonDelete.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Use Case deleted!", "Use Case deleted!", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        }*/
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonSaveUseCasesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveUseCasesActionPerformed
        this.showSaveUseCases();
    }//GEN-LAST:event_buttonSaveUseCasesActionPerformed

    private void buttonSaveDescriptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveDescriptionActionPerformed
        this.saveDescription();
    }//GEN-LAST:event_buttonSaveDescriptionActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CasosDeUsoMapeados;
    private javax.swing.JButton buttonDelete;
    private javax.swing.JButton buttonDiagram;
    private javax.swing.JButton buttonGuidelines;
    private javax.swing.JButton buttonSaveDescription;
    private javax.swing.JButton buttonSaveUseCases;
    private javax.swing.JMenuItem fileSaveDescription;
    private javax.swing.JMenuItem fileSaveUseCases;
    private javax.swing.JMenuItem helpAbout;
    private javax.swing.JMenuItem helpGuidelines;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel labelCasosDeUso;
    private javax.swing.JMenu menuAjuda;
    private javax.swing.JMenu menuFile;
    private javax.swing.JMenuBar menuUseCases;
    private javax.swing.JTable tabelUseCases;
    private javax.swing.JTextPane textUseCases;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JTextPane textUseCasesSave;
    
    // Clicou em Mostrar Diagrama de Casos de Uso
    private void showUseCasesDiagram() throws HeadlessException, IOException {
        if (e4jInstance == null) {
            e4jInstance = new EditorJFrame(1);
            e4jInstance.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            EditorWindowListener windowListener = new EditorWindowListener(this, e4jInstance);
            this.addWindowListener(windowListener);
            e4jInstance.addWindowListener(windowListener);
            this.addWindowListener(windowListener);
        }
        mxGraph graph = generateDiagram();

        e4jInstance.setVisible(true);
        this.setVisible(false);
    }

    // Gera e exibe o diagrama de Casos de Uso UML com base nas informações obtidas
    private mxGraph generateDiagram() throws IOException {
        
        List<UCActor> actorsList = BPMNController.getActors();
        List<UCUseCase> useCasesList = BPMNController.getUseCases();
        
        mxGraph graph = ((BasicUseCasesEditor) e4jInstance.getEditor()).getGraphComponent().getGraph();
        graph.removeCells(graph.getChildVertices(graph.getDefaultParent()));
        graph.removeCells(graph.getChildEdges(graph.getDefaultParent()));
        graph.getModel().beginUpdate();
        Element value;
        File shapesFolder = new File("resources/shapes/use cases diagram/");
        File[] files = shapesFolder.listFiles(ShapeFilenameFilter.instance);
        String nodeXml = mxUtils.readFile(files[0].getAbsolutePath());
        mxStencilShape newShape = new mxStencilShape(nodeXml);
        String styleActor = "shape=" + newShape.getName() + ";";
        nodeXml = mxUtils.readFile(files[1].getAbsolutePath());
        newShape = new mxStencilShape(nodeXml);
        String styleCase = "shape=" + newShape.getName() + ";";
        mxGeometry geo;
        
        // HashMaps para quardar as celular dos atores e casos de uso
        HashMap<String, mxCell> actors = new HashMap<>();
        HashMap<String, mxCell> cases = new HashMap<>();
        mxCell aresta;
        mxCell ext;
        
        // variaveis para controlar as posicoes X e Y dos elementos no diagrama
        int yActor = 110;
        int xActor = 80;
        int yCase = 30;
        int xCase = 400;
        String cod;
        
        // Seta atores e seus respectivos casos de uso
        for (UCActor actor : actorsList) {
            // cria ator e sua geometria e estilo
            value = IStarUtils.createActorUseCase();
            value.setAttribute("label", actor.getName());
            geo = new mxGeometry(50, yActor, 80, 80);
            
            if(actor.getChildren().isEmpty())
                geo.setX(xActor - 50);
            else geo.setX(xActor);
            
            geo.setY(yActor);
            mxCell cell = new mxCell(value, geo, styleActor);
            cell.setVertex(true);            
            actors.put(actor.getCode(), cell);
            graph.addCell(cell);
            
            // cria os casos de uso e arestas
            for (UCUseCase caso : actor.getUseCases()) {
                // cria caso de uso, sua geometria e estilo
                value = IStarUtils.createUseCase();
                value.setAttribute("label", caso.getName().replace("\"", ""));
                geo = new mxGeometry(xCase, yCase, 120, 60);
                xCase = xCase == 400 ? 700 : 400;
                geo.setX(xCase);
                geo.setY(yCase);
                yCase += 100;
                cod = String.valueOf(caso.getCode());
                
                // verifica se o caso de uso ainda não foi adicionado no grafo
                if (cases.get(cod) == null) {
                    cases.put(cod, new mxCell(value, geo, styleCase));
                    cases.get(cod).setVertex(true);
                    graph.addCell(cases.get(cod));
                }
                
                // cria uma aresta entre o ator e seu caso de uso
                value = IStarUtils.createAssociation();
                aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, actors.get(actor.getCode()), cases.get(cod), "straight;endArrow=none;noLabel=1;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setEdge(true);
                aresta.setStyle("straight;endArrow=none;noLabel=1;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setSource(actors.get(actor.getCode()));
                aresta.setTarget(cases.get(cod));
                graph.addCell(aresta);
            }
            yActor = yCase + 100;
        }
        
        // adiciona as ligações do tipo generalização entre atores
        for (UCActor actor : actorsList) {
            String codFather = actor.getCode();
            String nameFather = actor.getName();
            for (int i = 0; i < actor.getChildren().size(); i++) {                                                
                // cria a ligação generalization entre os atores
                value = IStarUtils.createGeneralization();
                aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, actors.get(codFather), actors.get(actor.getChildren().get(i).getCode()), "straight;noLabel=1;endArrow=diamond;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setEdge(true);
                
                // verifica se o ator está no diagrama
                if (actors.get(codFather) == null) {
                    // cria ator e sua geometria e estilo
                    value = IStarUtils.createActorUseCase();
                    value.setAttribute("label", nameFather);
                    geo = new mxGeometry(50, yActor, 80, 80);
                    geo.setX(xActor);
                    geo.setY(yActor);
                    actors.put(codFather, new mxCell(value, geo, styleActor));
                    actors.get(codFather).setVertex(true);
                    graph.addCell(actors.get(codFather));
                    yActor += 100;
                }
                
                aresta.setSource(actors.get(codFather));
                
                // verifica se o ator filho está no diagrama
                if (actor.getChildren().get(i) == null) {
                    // cria ator e sua geometria e estilo
                    value = IStarUtils.createActorUseCase();
                    value.setAttribute("label", actor.getChildren().get(i).getName());
                    geo = new mxGeometry(50, yActor, 80, 80);
                    geo.setX(50);
                    geo.setY(yActor);
                    actors.put(actor.getChildren().get(i).getCode(), new mxCell(value, geo, styleActor));
                    actors.get(actor.getChildren().get(i).getCode()).setVertex(true);
                    graph.addCell(actors.get(actor.getChildren().get(i).getCode()));
                    yActor += 100;
                }
                aresta.setTarget(actors.get(actor.getChildren().get(i).getCode()));
                aresta.setValue(value);
                aresta.setStyle("straight;noLabel=1;endArrow=block;endFill=0;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                graph.addCell(aresta);
            }
        }
        
        // Adiciona as ligacoes do tipo << include >>        
        for (UCUseCase useCase : useCasesList) {                                                            
            // verifica se o caso de uso ainda não foi adicionado no grafo
            if (cases.get(String.valueOf(useCase.getCode())) == null) {

                // Caso de uso ainda não inserido
                // cria caso de uso, sua geometria e estilo
                value = IStarUtils.createUseCase();
                value.setAttribute("label", useCase.getName().replace("\"", ""));
                geo = new mxGeometry(xCase, yCase, 120, 60);
                xCase = xCase == 400 ? 700 : 400;
                geo.setX(xCase);
                geo.setY(yCase);
                yCase += 100;
                cod = String.valueOf(useCase.getCode());

                cases.put(cod, new mxCell(value, geo, styleCase));
                cases.get(cod).setVertex(true);
                graph.addCell(cases.get(cod));
            }
                
            // cria as ligações do tipo << include >>
            for(int i = 0; i < useCase.getIncludedUseCases().size(); i++){
                // Verifica se o caso de uso já foi inserido
                // verifica se o caso de uso ainda não foi adicionado no grafo
                if (cases.get(String.valueOf(useCase.getIncludedUseCases().get(i).getCode())) == null) {

                    // Caso de uso ainda não inserido
                    // cria caso de uso, sua geometria e estilo
                    value = IStarUtils.createUseCase();
                    value.setAttribute("label", useCase.getIncludedUseCases().get(i).getName().replace("\"", ""));
                    geo = new mxGeometry(xCase, yCase, 120, 60);
                    xCase = xCase == 400 ? 700 : 400;
                    geo.setX(xCase);
                    geo.setY(yCase);
                    yCase += 100;
                    cod = String.valueOf(useCase.getIncludedUseCases().get(i).getCode());

                    cases.put(cod, new mxCell(value, geo, styleCase));
                    cases.get(cod).setVertex(true);
                    graph.addCell(cases.get(cod));                                                
                }
                
                // cria a ligação include entre os casos de uso
                value = IStarUtils.createInclude();
                aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, cases.get(String.valueOf(useCase.getCode())), cases.get(String.valueOf(useCase.getIncludedUseCases().get(i).getCode())), "straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setSource(cases.get(String.valueOf(useCase.getCode())));
                aresta.setTarget(cases.get(String.valueOf(useCase.getIncludedUseCases().get(i).getCode())));
                aresta.setEdge(true);
                aresta.setValue(value);
                aresta.setStyle("straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                graph.addCell(aresta);
                
            }                                                
        }
                
        graph.getModel().endUpdate();
        return graph;

    }

    private void setSelectedActor(Actor actor) {
        selectedActor = actor;
    }

    private Actor getSelectedActor() {
        return this.selectedActor;
    }

    public String getSelectedCase() {
        return selectedCase;
    }

    public void setSelectedCase(String selectedCase) {
        this.selectedCase = selectedCase;
    }

    private void showSaveUseCases() {
        if(textUseCasesSave == null)
            textUseCasesSave = new JTextPane();
        
        textUseCasesSave.setText("");
        buttonDelete.setEnabled(false);
        String path = Controller.loadProperties();
        JFileChooser fileChooser = new JFileChooser(path);
        fileChooser.setFileFilter(new FiltroDOC());
        int resultado = fileChooser.showSaveDialog(null);
        Controller.saveProperties(fileChooser.getSelectedFile().getParent());
        if (resultado == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        } else {
            try {
                File file = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file.getPath() + ".doc", true)) {
                    
                    // define o objeto com formatação negrito negrito
                    SimpleAttributeSet negrito = new SimpleAttributeSet();
                    StyleConstants.setBold(negrito, true);    
                    
                    // insere os dados dos casos de uso
                    for(UCUseCase useCase : BPMNController.getUseCases()){
                        textUseCasesSave.setText("");
                        insertStyleSave("\nUse Case", negrito);
                        insertStyleSave(": " + useCase.getDescription().getName() +  "\n\n", null);

                        insertStyleSave("CHARACTERISTIC INFORMATION\n", negrito);
                        insertStyleSave("	Goal in Context", negrito);
                        insertStyleSave(": " + useCase.getDescription().getGoal() + "\n", null);
                        insertStyleSave("	Scope", negrito);
                        insertStyleSave(": " + useCase.getDescription().getScope() + "\n", null);
                        insertStyleSave("	Preconditions", negrito);
                        insertStyleSave(": " + useCase.getDescription().getPreConditions() +  "\n", null);
                        insertStyleSave("	Success End Condition", negrito);
                        insertStyleSave(": " + useCase.getDescription().getEndSucess() +  "\n", null);
                        insertStyleSave("	Failed End Condition", negrito);
                        insertStyleSave(": " + useCase.getDescription().getEndFailure() + "\n", null);
                        insertStyleSave("	Trigger", negrito);
                        insertStyleSave(": " + useCase.getDescription().getTrigger() + "\n", null);
                        insertStyleSave("	Primary actor", negrito);
                        insertStyleSave(": " + useCase.getDescription().getPrimaryActor() + "\n", null);

                        insertStyleSave("\nMAIN SUCCESS SCENARIO\n", negrito);  
                        int i = 1;
                        for(String sentence : useCase.getDescription().getScenario()){
                            insertStyleSave("<passo " + i++ + "> - " + sentence + "\n", null);
                        }
                        insertStyleSave("\n", null);

                        insertStyleSave("EXTENSIONS\n", negrito);              
                        insertStyleSave("\n", null);
                        insertStyleSave("	Priority", negrito);
                        insertStyleSave(": " + useCase.getDescription().getPriority()+ "\n", null);
                        insertStyleSave("	Target performance", negrito);
                        insertStyleSave(": " + useCase.getDescription().getPerformance()+ "\n", null);
                        insertStyleSave("	Frequency", negrito);
                        insertStyleSave(": " + useCase.getDescription().getFrequency()+ "\n", null);
                        insertStyleSave("	Use Case father", negrito);
                        insertStyleSave(": " + useCase.getDescription().getUseCaseFather() + "\n", null);
                        insertStyleSave("	Included Use Cases", negrito);
                        insertStyleSave(": " + useCase.getDescription().getIncludedUseCases()+ "\n", null);
                        insertStyleSave("	Secondary actors", negrito);
                        insertStyleSave(": " + useCase.getDescription().getSecondaryActors()+ "\n", null);
                        insertStyleSave("	Additional information", negrito);
                        insertStyleSave(": " + useCase.getDescription().getAdditionalInformation()+ "\n", null);      
                        
                        writer.write(textUseCasesSave.getText());
                    }
                    
                    JOptionPane.showMessageDialog(null, "File generated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void saveDescription() {       
        buttonDelete.setEnabled(false);
        String path = Controller.loadProperties();
        JFileChooser fileChooser = new JFileChooser(path);
        fileChooser.setFileFilter(new FiltroDOC());
        int resultado = fileChooser.showSaveDialog(null);
        Controller.saveProperties(fileChooser.getSelectedFile().getParent());
        if (resultado == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        } else {
            try {
                File file = fileChooser.getSelectedFile();
                try (FileWriter writer = new FileWriter(file.getPath() + ".doc", true)) {
                    writer.write(textUseCases.getText());
                }
                JOptionPane.showMessageDialog(null, "File generated successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(UseCasesViewBPMN.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
