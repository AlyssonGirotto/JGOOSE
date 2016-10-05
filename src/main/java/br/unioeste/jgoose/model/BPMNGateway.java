/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unioeste.jgoose.model;

import java.util.ArrayList;

/**
 *
 * @author Alysson Girotto
 */
public class BPMNGateway extends BPMNElement{
    public static final Integer A = 1;
    public static final Integer B = 2;
    
    private Integer gatewayType;

    public BPMNGateway(){
        super();
        setType(BPMNElement.GATEWAY);
    }
    
    public BPMNGateway(Integer gatewayType, String code, String label, Integer type, String parent, ArrayList<String> links) {
        super(code, label, type, parent, links);
        this.gatewayType = gatewayType;
    }

    public Integer getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(Integer gatewayType) {
        this.gatewayType = gatewayType;
    }

    @Override
    public String toString() {
        return "BPMNGateway{" + "gatewayType=" + gatewayType + '}' + super.toString();
    }
         
}
