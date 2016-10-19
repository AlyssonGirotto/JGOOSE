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
    public static final Integer GATEWAY_BASIC = 1;
    public static final Integer PARALLEL = 2;
    public static final Integer INCLUSIVE = 3;
    public static final Integer EVENT_BASED = 4;
    public static final Integer EXCLUSIVE_EVENT_BASED = 5;
    public static final Integer EXCLUSIVE = 6;
    public static final Integer PARALLEL_EVENT_BASED = 7;
    public static final Integer COMPLEX = 8;   
    
    private Integer gatewayType;

    public BPMNGateway(){
        super();
        setType(BPMNElement.GATEWAY);
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
