package br.unioeste.jgoose.controller;

import br.unioeste.jgoose.UseCases.Actor;
import br.unioeste.jgoose.UseCases.ActorISA;
import br.unioeste.jgoose.UseCases.Mapping;
import br.unioeste.jgoose.model.IStarActorElement;
import br.unioeste.jgoose.model.TokensOpenOME;
import br.unioeste.jgoose.view.MainView;
import br.unioeste.jgoose.view.SelectActorView;
import br.unioeste.jgoose.view.UseCasesDiagramView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 *
 * @author Diego Peliser
 */
public class Controller {

    private static MainView mainView = new MainView();
    private static TokensOpenOME ome;
    private static String systemActor;
    private static Mapping mapping;

    /*
     * Método que controla o mapeamento dos casos de uso
     */
    public static void mapUseCases() {
        mapping = new Mapping();
        try {
            mapping.mappingStep1();
            mapping.mappingStep2();
            if (ome.checkSDSR(systemActor)) {
                mapping.mappingStep3();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error in Mapping of Use Cases!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * Método que abre o Arquivo Telos, procura e armazena em uma estrutura de
     * dados os Atores, Elementos e Links
     */
    public static void openTelosFile() {
        ome = new TokensOpenOME();
        ome.openFile();
        // Mapeia o arquivo se está no formato Telos
        if (ome.getDirIn().contains(".tel")) {
            ome.searchFile();
            mainView.setEnabled(false);
            // Abre a janela para selecionar o Ator Sistema
            SelectActorView atorsistema = new SelectActorView(mainView);
            atorsistema.setVisible(true);
            atorsistema.setAlwaysOnTop(true);
        } else if (!ome.getDirIn().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Select a Telos File (.tel)", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Salva o último caminho de um arquivo salvo.
     *
     * @param properties Caminho do arquivo salvo.
     */
    public static boolean saveProperties(String properties) {
        Properties prop = new Properties();
        prop.setProperty("file.chooser", properties);

        try {
            try (FileOutputStream fos = new FileOutputStream(new File("./caminho.properties"))) {
                prop.store(fos, "");
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /**
     * Carrega o último caminho de um arquivo salvo.
     *
     * @return path Retorna o caminho.
     */
    public static String loadProperties() {
        File file = new File("./caminho.properties");
        Properties prop = new Properties();
        String path = null;
        try {
            prop.load(new FileReader(file));
            path = prop.getProperty("file.chooser");
        } catch (IOException e) {
            System.out.println("Erro ao abrir arquivo de configuração!");
        }
        return path;
    }

    public static void setMainView(MainView mainView) {
        Controller.mainView = mainView;
    }

    /**
     * @return Returns the ome.
     */
    public static TokensOpenOME getOme() {
        return ome;
    }

    public static void setOme(TokensOpenOME ome) {
        Controller.ome = ome;
    }

    /**
     * @return Returns the systemActor
     */
    public static String getSystemActor() {
        return systemActor;
    }

    public static void setSystemActor(String systemActor) {
        Controller.systemActor = systemActor;
    }

    /*
     * Método que verifica o Ator Sistema Selecionado na ComboBox e o seta na
     * Interface Principal
     */
    public static void setSystemActor(int pos) {
        IStarActorElement Actor = ome.getActor(pos);
        systemActor = Actor.getCod();
        mainView.updateTables();
        mainView.setEnabled(true);
    }

    public static void setDiagram(Mapping diagram) {
        Controller.mapping = diagram;
    }

    /*
     * @returns the UseCases
     */
    public static ArrayList<Actor> getUseCases() {
        return mapping.useCases;
    }

    /*
     * @returns the ISAs
     */
    public static ArrayList<ActorISA> getIsas() {
        return mapping.isas;
    }

    /*
     * @returns the Actor
     */
    public static Actor getActor(String name) {
        for (Actor actor : mapping.useCases) {
            if (actor.getName().equals(name)) {
                return actor;
            }
        }
        return null;
    }

    public static void showActorSystemSelectionView() {
        SelectActorView atorsistema = new SelectActorView(mainView);
        atorsistema.setVisible(true);
        atorsistema.setAlwaysOnTop(true);
    }
}
