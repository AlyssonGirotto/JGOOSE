/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.controller;

import br.unioeste.jgoose.model.TokensBPMN;
import br.unioeste.jgoose.view.MainView;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNController {
    
    private static TokensBPMN tokensBPMN;
    private static MainView mainView = new MainView();
    
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
    }
}
