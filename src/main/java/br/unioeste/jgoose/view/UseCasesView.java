package br.unioeste.jgoose.view;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.UseCases.ActorISA;
import br.unioeste.jgoose.UseCases.Extend;
import br.unioeste.jgoose.UseCases.NFR;
import br.unioeste.jgoose.UseCases.Step;
import br.unioeste.jgoose.UseCases.UseCase;
import br.unioeste.jgoose.controller.Controller;
import br.unioeste.jgoose.controller.EditorWindowListener;
import br.unioeste.jgoose.e4j.filters.ShapeFilenameFilter;
import br.unioeste.jgoose.e4j.swing.BasicUseCasesEditor;
import br.unioeste.jgoose.e4j.swing.EditorJFrame;
import br.unioeste.jgoose.model.FiltroDOC;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
 * @author Diego Peliser
 */
public class UseCasesView extends javax.swing.JFrame {

    private Actor selectedActor = null;
    private String selectedCase = "";
    private EditorJFrame e4jInstace = null;
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger("console");

    /**
     * Creates new form UseCasesView
     */
    public UseCasesView() {
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
    protected void updateTabel() {
        DefaultTableModel tabCasosDeUso = new DefaultTableModel();
        tabCasosDeUso.addColumn("ID");
        tabCasosDeUso.addColumn("Actor");
        tabCasosDeUso.addColumn("Use Case");
        tabCasosDeUso.addColumn("Type");
        String vetCasosDeUso[] = new String[4];
        int cont = 1;
        // adiciona os dados dos casos de uso no modelo da tabela
        for (Actor actor : Controller.getUseCases()) {
            for (Object useCase : actor.getUseCases()) {
                UseCase caso = (UseCase) useCase;
                vetCasosDeUso[0] = "" + cont++;
                vetCasosDeUso[1] = actor.getName();
                vetCasosDeUso[2] = caso.getName();
                vetCasosDeUso[3] = caso.getType();
                tabCasosDeUso.addRow(vetCasosDeUso);
            }
        }
        tabelUseCases.setModel(tabCasosDeUso);
        // seta a largura das colunas da tabela
        tabelUseCases.getColumnModel().getColumn(0).setPreferredWidth(30);
        tabelUseCases.getColumnModel().getColumn(1).setPreferredWidth(200);
        tabelUseCases.getColumnModel().getColumn(2).setPreferredWidth(300);
        tabelUseCases.getColumnModel().getColumn(3).setPreferredWidth(200);
    }

    /*
     * Método que insere um texto na área de texto de Casos de Uso com formatação
     */
    private void insertStyle(String text, SimpleAttributeSet style) {
        Document doc = textUseCases.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException ex) {
            Logger.getLogger(UseCasesView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tabelUseCasesValueChanged(ListSelectionEvent evt) {
        buttonDelete.setEnabled(true);
        buttonDiagram.setEnabled(true);
        int linha = tabelUseCases.getSelectedRow();
        if (linha != -1) {
            labelCasosDeUso.setText("Specification of the Use Case");
            textUseCases.setText(""); // limpa o painel de texto
            // define o objeto com formatação negrito negrito
            SimpleAttributeSet negrito = new SimpleAttributeSet();
            StyleConstants.setBold(negrito, true);
            //StyleConstants.setForeground(setNegrito, Color.RED); 
            //serve para definir uma cor ao texto tb.. (nao serve para sua dúvida.. m
            //as vale pro futuro se precisar. Nesse caso, a cor ficaria com a cor VERMELHA) 

            // insere os dados do caso de uso selecionado
            Actor actor = Controller.getActor("" + tabelUseCases.getValueAt(linha, 1));
            setSelectedActor(actor);
            setSelectedCase("" + tabelUseCases.getValueAt(linha, 2));
            insertStyle("Use Case: " + tabelUseCases.getValueAt(linha, 2) + "\n\n", negrito);
            insertStyle("CHARACTERISTIC INFORMATION\n", negrito);
            insertStyle("	Goal in Context:\n", null);
            insertStyle("	Scope:\n", null);
            insertStyle("	Preconditions:\n", null);
            insertStyle("	Success End Condition:\n", null);
            insertStyle("	Failed End Condition:\n", null);
            insertStyle("	Primary Actor: " + tabelUseCases.getValueAt(linha, 1) + "\n", null);
            insertStyle("\nMAIN SUCESS SCENARIO\n", negrito);
            UseCase caso = actor.getUseCase("" + tabelUseCases.getValueAt(linha, 2));
            if (caso.getSteps().isEmpty()) {
//                insertStyle("	1. Description Step 1 [<< include >> optional]\n", null);
//                insertStyle("	2. Description Step 2 [<< include >> optional]\n", null);
//                insertStyle("	3. Description Step 3 [<< include >> optional]\n", null);
//                insertStyle("	4. Description Step 4 [<< include >> optional]\n", null);
                insertStyle("\nEXTENSIONS\n", negrito);
//                insertStyle("	2.1. Extension Step 2 << extend >>\n", null);
//                insertStyle("	2.2. Extension Step 2 << extend >>\n", null);
//                insertStyle("	3.1. Extension Step 3 << extend >>\n", null);
            } else {
                int cont = 1;
                int n = caso.getSteps().size();
                Step step;
                for (int i = n - 1; i >= 0; i--) {
                    step = caso.getSteps().get(i);
                    String name = step.getName().replaceAll("\"", "");
                    if (step.getExtends().isEmpty()) {
                        if (step.isInclude()) {
                            name += " << include >> ";
                        }
                        insertStyle("	" + cont++ + ". " + name + "\n", null);
                    } else {
                        int m = step.getExtends().size() - 1;
                        name += " " + step.getExtend(m).getName().replaceAll("\"", "");
                        if (step.isInclude()) {
                            name += " << include >> ";
                        }
                        insertStyle("	" + cont++ + ". " + name + "\n", null);
                    }
                }
                insertStyle("\nEXTENSIONS\n", negrito);
                int aux = caso.getSteps().size();
                cont = 1;
                for (int i = n - 1; i >= 0; i--) {
                    step = caso.getSteps().get(i);
                    Extend extend;
                    n = step.getExtends().size();
                    if (n < 2) {
                        aux--;
                    } else {
                        for (int j = n - 2; j >= 0; j--) {
                            extend = step.getExtend(j);
                            insertStyle("	" + cont + "." + (n - j - 1) + ". " + extend.getName().replaceAll("\"", "") + " << extend >>\n", null);
                        }
                    }
                    cont++;
//                    if (aux == 0) {
//                        insertStyle("	2.1. Extension Step 2 << extend >>\n", null);
//                        insertStyle("	2.2. Extension Step 2 << extend >>\n", null);
//                        insertStyle("	3.1. Extension Step 3 << extend >>\n", null);
//                    }
                }
            }
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
            diretrizes = new GuidelinesDialogView(this);
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
        buttonShowNFRs = new javax.swing.JButton();
        buttonShowHerancas = new javax.swing.JButton();
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
        tabelUseCases.getColumnModel().getColumn(0).setResizable(false);
        tabelUseCases.getColumnModel().getColumn(1).setResizable(false);
        tabelUseCases.getColumnModel().getColumn(2).setResizable(false);
        tabelUseCases.getColumnModel().getColumn(3).setResizable(false);

        labelCasosDeUso.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labelCasosDeUso.setText("Specification of the Use Case");

        buttonShowNFRs.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonShowNFRs.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/nfr_32x32.png"))); // NOI18N
        buttonShowNFRs.setText("Show NFRs");
        buttonShowNFRs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowNFRsActionPerformed(evt);
            }
        });

        buttonShowHerancas.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        buttonShowHerancas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/isa_32x32.png"))); // NOI18N
        buttonShowHerancas.setText("Show ISAs");
        buttonShowHerancas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonShowHerancasActionPerformed(evt);
            }
        });

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
                            .addComponent(buttonShowHerancas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(buttonShowNFRs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(buttonDiagram)
                                        .addComponent(buttonDelete, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(buttonGuidelines, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {buttonDelete, buttonDiagram, buttonGuidelines, buttonShowHerancas, buttonShowNFRs});

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
                        .addComponent(buttonShowNFRs, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonShowHerancas, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonGuidelines, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(buttonSaveDescription)
                            .addComponent(buttonSaveUseCases, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {buttonGuidelines, buttonShowHerancas});

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
            Logger.getLogger(UseCasesView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UseCasesView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_buttonDiagramActionPerformed

    private void buttonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteActionPerformed
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
                    updateTabel();
                    buttonDelete.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Use Case deleted!", "Use Case deleted!", JOptionPane.INFORMATION_MESSAGE);
                    break;
                }
            }
        }
    }//GEN-LAST:event_buttonDeleteActionPerformed

    private void buttonShowHerancasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowHerancasActionPerformed
        buttonDelete.setEnabled(false);
        labelCasosDeUso.setText("ISA Links (Generalization)");
        textUseCases.setText(""); // limpa o painel de texto
        // define o objeto com formatação negrito negrito
        SimpleAttributeSet negrito = new SimpleAttributeSet();
        StyleConstants.setBold(negrito, true);
        // insere os dados dos ISAs
        for (ActorISA actor : Controller.getIsas()) {
            for (Object names : actor.getNameFathers()) {
                String name = (String) names;
                insertStyle("Actor: ", negrito);
                insertStyle(name, null);
                insertStyle("   Father: ", negrito);
                insertStyle(actor.getName() + "\n", null);
            }
        }
    }//GEN-LAST:event_buttonShowHerancasActionPerformed

    private void buttonShowNFRsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonShowNFRsActionPerformed
        buttonDelete.setEnabled(false);
        labelCasosDeUso.setText("Non-functional Requirement related to the System");
        textUseCases.setText(""); // limpa o painel de texto
        // define o objeto com formatação negrito negrito
        SimpleAttributeSet negrito = new SimpleAttributeSet();
        StyleConstants.setBold(negrito, true);
        // insere os dados dos NFRs
        for (Actor actor : Controller.getUseCases()) {
            for (NFR nfr : actor.getNfrs()) {
                insertStyle("Actor: ", negrito);
                insertStyle(actor.getName(), null);
                insertStyle("   NFR: ", negrito);
                insertStyle(nfr.getName() + "\n", null);
            }

        }
    }//GEN-LAST:event_buttonShowNFRsActionPerformed

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
    private javax.swing.JButton buttonShowHerancas;
    private javax.swing.JButton buttonShowNFRs;
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

    private void showUseCasesDiagram() throws HeadlessException, IOException {
//        UseCasesDiagramView useCasesDiagramFrame = new UseCasesDiagramView(getSelectedActor(), getSelectedCase());
//        useCasesDiagramFrame.setVisible(true);
        if (e4jInstace == null) {
            e4jInstace = new EditorJFrame(false);
            e4jInstace.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            EditorWindowListener windowListener = new EditorWindowListener(this, e4jInstace);
            this.addWindowListener(windowListener);
            e4jInstace.addWindowListener(windowListener);
            this.addWindowListener(windowListener);
        }
        mxGraph graph = generateDiagram();
        ((BasicUseCasesEditor) e4jInstace.getEditor()).getGraphComponent().setGraph(graph);
        // chama a rotina de mapeamento para geração do diagrama de casos de uso
//        try {
//            ((BasicUseCasesEditor) e4jInstace.getEditor()).generateDiagram();
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(null, "Error in Mapping of Diagram Use Cases!", "ERROR!", JOptionPane.ERROR_MESSAGE);
//        }
        e4jInstace.setVisible(true);
        this.setVisible(false);
    }

    private mxGraph generateDiagram() throws IOException {
        ArrayList<Actor> useCases = Controller.getUseCases();
        ArrayList<ActorISA> isas = Controller.getIsas();
        // comeca a atualizar o grafo
        mxGraph graph = new mxGraph();
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
        int yCase = 30;
        int xCase = 400;
        String cod;
        /*
         * Adicionar ao diagrama todos os atores com seus respectivos
         * casos de uso e arestas.
         */
        for (Actor actor : useCases) {
            // cria ator e sua geometria e estilo
            value = IStarUtils.createActorUseCase();
            value.setAttribute("label", actor.getName());
            geo = new mxGeometry(50, yActor, 80, 80);
            geo.setX(50);
            geo.setY(yActor);
            actors.put(actor.getCod(), new mxCell(value, geo, styleActor));
            actors.get(actor.getCod()).setVertex(true);
            graph.addCell(actors.get(actor.getCod()));
            // cria os casos de uso e arestas
            for (UseCase caso : actor.getUseCases()) {
                // cria caso de uso, sua geometria e estilo
                value = IStarUtils.createUseCase();
                value.setAttribute("label", caso.getName().replace("\"", ""));
                geo = new mxGeometry(xCase, yCase, 120, 60);
                xCase = xCase == 400 ? 700 : 400;
                geo.setX(xCase);
                geo.setY(yCase);
                yCase += 100;
                cod = caso.getCod();
                // se o caso de uso possui elemento decomposto guarda o codigo dele
                // pois é o elemento decomposto que é incluso por outros casos de uso
                if (caso.getCodDecomposedElement() != null) {
                    cod = caso.getCodDecomposedElement();
                }
                // verifica se o caso de uso ainda não foi adicionado no grafo
                if (cases.get(cod) == null) {
                    cases.put(cod, new mxCell(value, geo, styleCase));
                    cases.get(cod).setVertex(true);
                    graph.addCell(cases.get(cod));
                }
                // cria uma aresta entre o ator e seu caso de uso
                value = IStarUtils.createAssociation();
                aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, actors.get(actor.getCod()), cases.get(cod), "straight;endArrow=none;noLabel=1;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setEdge(true);
                aresta.setStyle("straight;endArrow=none;noLabel=1;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setSource(actors.get(actor.getCod()));
                aresta.setTarget(cases.get(cod));
                graph.addCell(aresta);
            }
            yActor = yCase + 100;
        }
        // Adiciona as ligacoes do tipo << include >> e << extend >>
        for (Actor actor : useCases) {
            for (UseCase caso : actor.getUseCases()) {
                // cria as ligações do tipo << include >>
                for (Step step : caso.getSteps()) {
                    if (step.isInclude()) {
                        cod = caso.getCod();
                        // se o ator possui elemento decomposto, pega o código dele
                        if (caso.getCodDecomposedElement() != null) {
                            cod = caso.getCodDecomposedElement();
                        }
                        // cria a ligação include entre os casos de uso
                        value = IStarUtils.createInclude();
                        aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, cases.get(cod), cases.get(step.getCod()), "straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                        aresta.setSource(cases.get(cod));
                        aresta.setTarget(cases.get(step.getCod()));
                        aresta.setEdge(true);
                        aresta.setValue(value);
                        aresta.setStyle("straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                        graph.addCell(aresta);
                        // cria as ligações do tipo << extend >>
                        for (Extend extend : step.getExtends()) {
                            // verifica se existe um caso de uso que é a extensão
                            if (cases.get(extend.getCod()) != null) {
                                value = IStarUtils.createExtend();
                                ext = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, cases.get(extend.getCod()), cases.get(cod), "straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                                ext.setEdge(true);
                                ext.setSource(cases.get(extend.getCod()));
                                ext.setTarget(cases.get(cod));
                                ext.setValue(value);
                                ext.setStyle("straight;dashed=1;endArrow=open;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                                graph.addCell(ext);
                            }
                        }
                    }
                }
            }
        }
        // adiciona as ligações do tipo ISA (generalização de atores)
        for (ActorISA isa : isas) {
            for (int i = 0; i < isa.getCodFathers().size(); i++) {
                String codFather = isa.getCodFathers().get(i);
                String nameFather = isa.getNameFathers().get(i);
                // cria a ligação generalization entre os atores
                value = IStarUtils.createGeneralization();
                aresta = (mxCell) graph.createEdge(graph.getDefaultParent(), null, value, actors.get(codFather), actors.get(isa.getCod()), "straight;noLabel=1;endArrow=diamond;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
                aresta.setEdge(true);
                // verifica se o ator está no diagrama
                if (actors.get(codFather) == null) {
                    // cria ator e sua geometria e estilo
                    value = IStarUtils.createActorUseCase();
                    value.setAttribute("label", nameFather);
                    geo = new mxGeometry(50, yActor, 80, 80);
                    geo.setX(50);
                    geo.setY(yActor);
                    actors.put(codFather, new mxCell(value, geo, styleActor));
                    actors.get(codFather).setVertex(true);
                    graph.addCell(actors.get(codFather));
                    yActor += 100;
                }
                aresta.setSource(actors.get(codFather));
                // verifica se o ator está no diagrama
                if (actors.get(isa.getCod()) == null) {
                    // cria ator e sua geometria e estilo
                    value = IStarUtils.createActorUseCase();
                    value.setAttribute("label", isa.getName());
                    geo = new mxGeometry(50, yActor, 80, 80);
                    geo.setX(50);
                    geo.setY(yActor);
                    actors.put(isa.getCod(), new mxCell(value, geo, styleActor));
                    actors.get(isa.getCod()).setVertex(true);
                    graph.addCell(actors.get(isa.getCod()));
                    yActor += 100;
                }
                aresta.setTarget(actors.get(isa.getCod()));
                aresta.setValue(value);
                aresta.setStyle("straight;noLabel=1;endArrow=block;endFill=0;endSize=14;shape=curvedEdge;edgeStyle=curvedEdgeStyle");
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
                    for (Actor actor : Controller.getUseCases()) {
                        for (UseCase caso : actor.getUseCases()) {
                            writer.write("Use Case: " + caso.getName() + "\n\n"
                                    + "CHARACTERISTIC INFORMATION\n"
                                    + "	Goal in Context:\n"
                                    + "	Scope:\n"
                                    + "	Preconditions:\n"
                                    + "	Success End Condition:\n"
                                    + "	Failed End Condition:\n"
                                    + "	Primary Actor: " + actor.getName() + "\n"
                                    + "\nMAIN SUCESS SCENARIO\n");
                            if (caso.getSteps().isEmpty()) {
                                writer.write("	1. Description Step 1 [<< include >> optional]\n"
                                        + "	2. Description Step 2 [<< include >> optional]\n"
                                        + "	3. Description Step 3 [<< include >> optional]\n"
                                        + "\nEXTENSIONS\n"
                                        + "	2.1. Extension Step 2 << extend >>\n"
                                        + "	2.2. Extension Step 2 << extend >>\n"
                                        + "	3.1. Extension Step 3 << extend >>\n");
                            } else {
                                int cont = 1;
                                int n = caso.getSteps().size();
                                Step step;
                                for (int i = n - 1; i >= 0; i--) {
                                    step = caso.getSteps().get(i);
                                    String name = step.getName().replaceAll("\"", "");
                                    if (step.isInclude()) {
                                        name += " << include >> ";
                                    }
                                    if (step.getExtends().isEmpty()) {
                                        if (step.isInclude()) {
                                            name += " << include >> ";
                                        }
                                        writer.write("	" + cont++ + ". " + name + "\n");
                                    } else {
                                        int m = step.getExtends().size() - 1;
                                        name += " " + step.getExtend(m).getName().replaceAll("\"", "");
                                        if (step.isInclude()) {
                                            name += " << include >> ";
                                        }
                                        writer.write("	" + cont++ + ". " + name + "\n");
                                    }
                                }
                                writer.write("\nEXTENSIONS\n");
                                int aux = caso.getSteps().size();
                                cont = 1;
                                for (int i = n - 1; i >= 0; i--) {
                                    step = caso.getSteps().get(i);
                                    Extend extend;
                                    n = step.getExtends().size();
                                    if (n < 2) {
                                        aux--;
                                    } else {
                                        for (int j = n - 2; j >= 0; j--) {
                                            extend = step.getExtend(j);
                                            writer.write("	" + cont + "." + (n - j - 1) + "." + extend.getName().replaceAll("\"", "") + " << extend >>\n");
                                        }
                                    }
                                    cont++;
                                    if (aux == 0) {
                                        writer.write("	2.1. Extension Step 2 << extend >>\n"
                                                + "	2.2. Extension Step 2 << extend >>\n"
                                                + "	3.1. Extension Step 3 << extend >>\n");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(UseCasesView.class.getName()).log(Level.SEVERE, null, ex);
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
            } catch (IOException ex) {
                Logger.getLogger(UseCasesView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
