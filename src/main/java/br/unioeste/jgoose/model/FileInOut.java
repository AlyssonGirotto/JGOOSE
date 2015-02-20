package br.unioeste.jgoose.model;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 *
 * @author Diego Peliser
 */
/**
 * Classe com métodos para manipulação dos arquivos texto de entrada e saída da
 * ferramenta.
 */
public class FileInOut {

    private FileWriter write;
    private PrintWriter out;
    private FileReader read;
    private BufferedReader in;
    private String dirIn;
    private String dirOut;

    /**
     * Construtor da classe File
     */
    public FileInOut() {
        dirIn = ""; // caminho do arquivo de entrada
        dirOut = ""; // caminho do arquivo de saida

        String saida = "saida.txt";
        String curDir = System.getProperty("user.dir");
        //cria a caixa de dialogo
        JFileChooser fileChooser = new JFileChooser(curDir);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Telos", "tel");
        fileChooser.setFileFilter(filter);
        //fileChooser.addChoosableFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        //recebe o resultado
        int resultado = fileChooser.showOpenDialog(null);

        // se o usuario clicou no botao Cancel no dialogo, retorna
        if (resultado == JFileChooser.CANCEL_OPTION) {
            fileChooser.setVisible(false);
        } else {
            // obtem o arquivo selecionado
            File fileName = fileChooser.getSelectedFile();

            try {
                // exibe erro se invalido
                if ((fileName == null) || (fileName.getName().equals(""))) {
                    JOptionPane.showMessageDialog(null,
                            "Nome de Arquivo Inválido",
                            "Nome de Arquivo Inválido",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dirIn = fileName.getName();

                write = new FileWriter(saida);
                read = new FileReader(fileName);
                in = new BufferedReader(read);
                out = new PrintWriter(write);

            } catch (IOException excep) {
                System.out.println("Erro de excessao construtor");
            }
        }
    }

    public FileInOut(File inputFile, String outputFilename) {
        this(inputFile, new File(outputFilename));
    }

    public FileInOut(File inputFile, File outputFile) {
        this.dirIn = inputFile.getName();

        this.initReader(inputFile);

        // @ TODO: change this 'magic filename'.
//        String saida = "saida.txt";
        this.initWriter(outputFile);
    }

    private void initReader(File inputfile) {
        try {
            this.read = new FileReader(inputfile);
            in = new BufferedReader(this.read);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileInOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initWriter(File outputFile) {
        try {
            this.write = new FileWriter(outputFile);
            out = new PrintWriter(this.write);
        } catch (IOException ex) {
            Logger.getLogger(FileInOut.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Captura uma linha do arquivo texto
     *
     * @return String s
     */
    public String getLine() {
        String s = "";
        try {
            s = in.readLine();
        } catch (IOException excep) {
            System.out.println("Erro de excessao getline");
        }
        return s;
    }

    /**
     * Escreve no arquivo de saida (sem pular linha)
     *
     * @param s
     */
    public void setLine(String s) {
        out.print(s);
    }

    /**
     * Escreve no arquivo de saida (pulando linha)
     *
     * @param s
     */
    public void setLineln(String s) {
        out.println(s);
    }

    /**
     * Fecha os arquivos texto abertos
     */
    public void close() {
        try {
            in.close();
            out.close();
            write.close();
            read.close();
        } catch (IOException excep) {
            System.out.println("Erro de excessao");
        }
    }

    /**
     * Retorna o Diretorio (Caminho) do arquivo de entrada
     *
     * @return dirIn
     */
    public String getDirIn() {
        return dirIn;
    }

    /**
     * Retorna o Diretorio (Caminho) do arquivo de saida
     *
     * @return dirOut
     */
    public String getDirOut() {
        return dirOut;
    }
}
