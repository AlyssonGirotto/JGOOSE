package br.unioeste.jgoose.io.filters;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Utility file filter to accept editor files, namely .tel.
 *
 * @see ImageIO#getReaderFormatNames()
 */
public class TelosFileFilter extends FileFilter {

    /**
     * Description of the File format
     */
    protected String desc;

    /**
     * Constructs a new editor file filter using the specified description.
     *
     */
    public TelosFileFilter() {
        this.desc = "Telos";
    }

    /**
     * Returns true if the file is a directory or has a .tel extension.
     *
     * @param file file or folder.
     * @return Returns true if the file is accepted.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        TelosFileNameFilter filter = new TelosFileNameFilter();
        String filename = file.getName().toLowerCase();
        return filter.accept(null, filename);
    }

    /**
     * Returns the description.
     *
     * @return Returns the description.
     */
    @Override
    public String getDescription() {
        return desc;
    }
}
