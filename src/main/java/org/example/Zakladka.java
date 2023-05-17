package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.io.File;

public class Zakladka extends JScrollPane{

    private File file;
    private File path;
    private JTextArea area;

    private boolean knowFile;

    private String name;

    private static int id = 1;
    private int sizeText;

    private boolean wrap;

    private boolean someChange;
    private int count = 0;

    private JMenuItem save;
    public Zakladka(JMenuItem save){
        super();

        area = new JTextArea();
        setViewportView(area);

        this.save = save;

        sizeText = area.getFont().getSize();
        knowFile = false;
        name = "Nieznane " + id++;


        area.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                someChange = true;
                if(file != null) save.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                someChange = true;
                if(file != null) save.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        someChange = false;
    }


    public boolean isKnowFile() {
        return knowFile;
    }

    public String getName() {
        return name;
    }

    public void setFile(File file) {
        this.file = file;
        name = file.getName();
        knowFile = true;
    }

    public void setPath(File path) {
        this.path = path;
    }

    public void setKnowFile(boolean knowFile) {
        this.knowFile = knowFile;
    }

    public File getFile() {
        return file;
    }

    public File getPath() {
        return path;
    }

    public int getSizeText() {
        return sizeText;
    }

    public int increaseSizeText(int value){
        return setSizeText(getSizeText() + value);
    }

    public int decreaseSizeText(int value){
        return setSizeText(getSizeText() - value);
    }

    public int setSizeText(int newSize){
        Font font;

        sizeText = newSize;
        font = new Font(Font.SANS_SERIF, Font.PLAIN, sizeText);
        area.setFont(font);

        return sizeText;
    }

    public boolean isWrap() {
        return wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }


    public boolean isSomeChange() {
        return someChange;
    }

    public void setSomeChange(boolean someChange) {
        this.someChange = someChange;
    }

    public JTextArea getArea() {
        return area;
    }
}
