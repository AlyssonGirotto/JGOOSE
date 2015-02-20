package br.unioeste.jgoose.UseCases;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author Diego Peliser
 */
public class Actor {

    private String cod;
    private String name;
    private ArrayList<UseCase> useCases;
    private ArrayList<NFR> nfrs;

    public Actor(String cod, String name) {
        this.cod = cod;
        this.name = name;
        this.useCases = new ArrayList<>();
        this.nfrs = new ArrayList<>();
    }
    
    /**
     * @return Returns the cod.
     */
    public String getCod() {
        return cod;
    }

    /**
     * @param cod The cod to set.
     */
    public void setCod(String cod) {
        this.cod = cod;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the useCases.
     */
    public ArrayList<UseCase> getUseCases() {
        return useCases;
    }

    public UseCase getUseCase(int i) {
        UseCase caso = useCases.get(i);
        return caso;
    }

    public UseCase getUseCase(String name) {
        for (UseCase caso : useCases) {
            if (caso.getName().equals(name)) {
                return caso;
            }
        }
        return null;
    }

    /**
     * @param useCases The useCases to set.
     */
    public void setUseCases(ArrayList<UseCase> useCases) {
        this.useCases = useCases;
    }

    public void setUseCase(UseCase useCase) {
        this.useCases.add(useCase);
    }

    /**
     * @return Returns the useCases.
     */
    public ArrayList<NFR> getNfrs() {
        return nfrs;
    }

    public NFR getNfr(int i) {
        NFR nfr = nfrs.get(i);
        return nfr;
    }

    /**
     * @param useCases The useCases to set.
     */
    public void setNfrs(ArrayList<NFR> nfrs) {
        this.nfrs = nfrs;
    }

    public void setNfr(NFR nfr) {
        this.nfrs.add(nfr);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.cod);
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Actor other = (Actor) obj;
        if (!Objects.equals(this.cod, other.cod)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
