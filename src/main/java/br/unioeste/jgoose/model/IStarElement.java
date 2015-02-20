package br.unioeste.jgoose.model;

import br.unioeste.jgoose.controller.Controller;
import java.util.ArrayList;

/**
 *
 * @author Diego Peliser
 */
/**
 * Utilizada para especificar o Elementos i* (IStarGoalElement,
 * IStarTaskElement, IStarSoftGoalElement, IStarResourceElement)
 *
 * @param cod //código (Element_x)
 * @param name //nome do Elemento ("Elemento 1")
 * @param parents //pais associados ao Elemento (ATORES SR) - códigos
 * @param links //ligações (códigos)
 */
public class IStarElement {

    private String cod; //código (Element_x)
    private String name; //nome do Elemento ("Efetuar Pagamento")
    private ArrayList<String> parents; //pais associados ao Elemento - ATORES SR (códigos)
    private ArrayList<String> links; //ligações (códigos)

    public IStarElement() {
        this.links = new ArrayList();
        this.parents = new ArrayList();
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
     * @return Returns the links.
     */
    public ArrayList getLinks() {
        return links;
    }

    /**
     * @param link The links to set.
     */
    public void setLink(String link) {
        this.links.add(link);
    }

    /**
     * @param Seta os links, informando o tipo de ligação, se ela é "FROM" ou
     * "TO" 0 - dependência, 1 - Decomposição de Tarefas, 2 - Meio-FIM, 3 - ISA,
     * -1 - Outras
     */
    public void setLink(int tipo, String link) {
        this.links.add(tipo + " " + link);
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
     * @return Returns the parent.
     */
    public ArrayList getParents() {
        return parents;
    }

    /**
     * @param parent The parent to set.
     */
    public void setParent(String parent) {
        this.parents.add(parent);
    }

    /**
     *
     * @return the cod of the decomposed element
     */
    public String getCodDecomposedElement() {
        IStarLink link2;
        for (String link : links) {
            link = link.trim();
            for (Object obj : Controller.getOme().getDependenciess()) {
                link2 = (IStarLink) obj;
                // procura o link (IStarLink) que liga o elemento ao elemento
                // sendo decomposto (filho do sistema) e retorna o cod do 
                // elemento decomposto
                if (link.equals(link2.getCod()) && link2.getFrom().equals(cod)) {
                    return link2.getTo();
                }
            }
        }
        return null;
    }

    /**
     *
     * @return the name of the decomposed element
     */
    public String getNameDecomposedElement() {
        IStarLink link2;
        for (String link : links) {
            link = link.trim();
            for (Object obj : Controller.getOme().getDependenciess()) {
                link2 = (IStarLink) obj;
                // procura o link (IStarLink) que liga o elemento ao elemento
                // sendo decomposto (filho do sistema) e retorna o cod do 
                // elemento decomposto
                if (link.equals(link2.getCod()) && link2.getFrom().equals(cod)) {
                    IStarElement element = Controller.getOme().getElement(link2.getTo());
                    if (element != null) {
                        return element.getName();
                    }
                }
            }
        }
        return null;
    }
}
