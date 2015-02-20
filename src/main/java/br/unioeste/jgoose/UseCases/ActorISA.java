package br.unioeste.jgoose.UseCases;

import java.util.ArrayList;

/**
 *
 * @author Diego Peliser
 */
public class ActorISA {

    private String cod;
    private String name;
    private ArrayList<String> codFathers; // pais associados ao ATOR: ISA (códigos)
    private ArrayList<String> nameFathers; // pais associados ao ATOR: ISA (nome)

    public ActorISA(String cod, String name) {
        this.cod = cod;
        this.name = name;
        this.codFathers = new ArrayList<>();
        this.nameFathers = new ArrayList<>();
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

    public ArrayList<String> getCodFathers() {
        return codFathers;
    }

    public void setCodFathers(ArrayList<String> codFathers) {
        this.codFathers = codFathers;
    }

    public void setCodFather(String codFather) {
        this.codFathers.add(codFather);
    }

    public ArrayList<String> getNameFathers() {
        return nameFathers;
    }

    public void setNameFathers(ArrayList<String> nameFathers) {
        this.nameFathers = nameFathers;
    }

    public void setNameFather(String nameFather) {
        this.nameFathers.add(nameFather);
    }
}
