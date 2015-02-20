package br.unioeste.jgoose.model;

import java.io.File;
import javax.swing.filechooser.*;

/**
 * Filtra para o JFileChooser somente salvar arquivos com extensão .doc
 *
 */
public class FiltroDOC extends FileFilter {

    public final static String tel = "doc";

    //Accept all directories and all gif, jpg, tiff, or png files.
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals("doc")) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Arquivo de Texto (.doc)";
    }
    
    /**
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}
