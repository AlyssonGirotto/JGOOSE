/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

import java.util.List;

/**
 *
 * @author Alysson Girotto
 */
public class UCUseCase {
   private String code;
   private String name;
   private String guidelineUsed; //Diretriz utilizada
   private Integer instanceCod;
   private List<Integer> includedUseCases; //Código dos casos de uso incluídos
   
   
}
