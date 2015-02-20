package br.unioeste.jgoose.UseCases;

/**
 *
 * @author Diego Peliser
 */
public class Extend {

    private String cod;
    private String name;

    public Extend(String cod, String name) {
        this.cod = cod;
        this.name = name;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
