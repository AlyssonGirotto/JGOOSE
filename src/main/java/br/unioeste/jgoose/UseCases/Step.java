package br.unioeste.jgoose.UseCases;

import java.util.ArrayList;

/**
 *
 * @author Diego Peliser
 */
public class Step extends Extend {
    
    private boolean include;
    private ArrayList<Extend> extend;

    public Step(String cod, String name, ArrayList<Extend> extend) {
        super(cod, name);
        this.extend = extend;
        include = false;
    }  

    public ArrayList<Extend> getExtends() {
        return extend;
    }
    
    public Extend getExtend(int i) {
        Extend ext = extend.get(i);
        return ext;
    }

    public boolean isInclude() {
        return include;
    }

    public void setInclude(boolean isInclude) {
        this.include = isInclude;
    }
}
