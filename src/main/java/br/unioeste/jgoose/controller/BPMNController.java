/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.controller;

import br.unioeste.jgoose.BPMNToUC.MappingBPMNToUC;
import br.unioeste.jgoose.model.TokensBPMN;
import br.unioeste.jgoose.model.UCActor;
import br.unioeste.jgoose.model.UCUseCase;
import br.unioeste.jgoose.view.MainView;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNController {
    
    private static TokensBPMN tokensBPMN;
    private static MappingBPMNToUC mappingBPMNToUC;
    private static MainView mainView = new MainView();
    
    /*
     * Método que controla o mapeamento dos casos de uso
     */
    public static void mapUseCases() {
        mappingBPMNToUC = new MappingBPMNToUC();
        try {
            mappingBPMNToUC.derivation();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error in Mapping of Use Cases!", "ERROR!", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static TokensBPMN getTokensBPMN(){
        return tokensBPMN;
    }
    
    public static void setTokensBPMN(TokensBPMN tokensBPMN){
        BPMNController.tokensBPMN = tokensBPMN;
    }
    
    public static void setMainView(MainView mainView) {
        BPMNController.mainView = mainView;
    }
    
    // Altera tabelas na janela principal
    public static void updateTables(){
        mainView.updateTableBPMN();
        
        MappingBPMNToUC bpmnToUC = new MappingBPMNToUC();
        bpmnToUC.derivation();
        
    }
    
    public static List<UCActor> getActors(){
        return mappingBPMNToUC.getActors();
    }
    
    public static List<UCUseCase> getUseCases(){
        return mappingBPMNToUC.getUseCases();
    }
}
