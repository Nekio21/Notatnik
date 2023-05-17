package org.example;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.nio.file.Path;

public class Notatnik implements ActionListener {

    //UI
    private JTabbedPane tabbedPane1;
    private JPanel panel1;

    //Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu otherMenu;
    private JMenuItem saveAsItem;
    private JMenuItem saveItem;
    private JMenuItem loadItem;
    private JMenuItem exitItem;
    private JMenuItem fontIItem;
    private JMenuItem wrapItem;
    private JMenuItem fontDItem;

    //File
    private File path;
    private File file;

    public static void main(String[] args){
        new Notatnik();
    }

    public Notatnik(){
        JFrame okno = new JFrame("Suuuuper app :)");

        okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        okno.setResizable(true);
        okno.setMinimumSize(new Dimension(650,350));
        setMenu(okno);


        Zakladka zakladka = new Zakladka(saveItem);
        tabbedPane1.add(zakladka);

        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Zakladka z = (Zakladka) tabbedPane1.getSelectedComponent();

                if(z.isKnowFile()){
                    saveItem.setEnabled(true);
                }else{
                    saveItem.setEnabled(false);
                }

                if(z.isWrap()){
                    z.getArea().setLineWrap(true);
                    wrapItem.setText("No wrap text");
                }else{
                    z.getArea().setLineWrap(false);
                    wrapItem.setText("Wrap text");
                }
            }
        });



        okno.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                while(tabbedPane1.getTabCount() != 0) {
                    close((Zakladka) tabbedPane1.getSelectedComponent());
                }
            }
        });

        okno.setContentPane(panel1);
        okno.setVisible(true);
    }

    public void setMenu(JFrame jFrame){
        initMenu();

        setMnemonicMenu();

        saveItem.setEnabled(false);

        setListenerMenu();

        addingAll(jFrame);
    }

    public void save(Zakladka z){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write(z.getArea().getText());

            bw.close();
            z.setSomeChange(false);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null,"", "Bład Exception", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void saveAs(Zakladka z)  {
        JFileChooser fc = new JFileChooser(path);

        int result = fc.showSaveDialog(panel1);

        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(result == JFileChooser.APPROVE_OPTION) {
            path = new File(fc.getSelectedFile().toPath().getParent().toAbsolutePath().toString());
            file = new File(fc.getSelectedFile().getAbsolutePath());

            saveItem.setEnabled(false);

            save(z);

            z.setFile(file);
            z.setPath(path);
            tabbedPane1.setTitleAt(tabbedPane1.getSelectedIndex(), z.getName());
        }
    }

    public void load(Zakladka z) throws IOException {
        JFileChooser fc = new JFileChooser(path);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int result = fc.showOpenDialog(panel1);

        if(result == JFileChooser.APPROVE_OPTION){
            path = new File(fc.getSelectedFile().toPath().getParent().toAbsolutePath().toString());
            file = new File(fc.getSelectedFile().getAbsolutePath());

             saveItem.setEnabled(false);

            BufferedReader br = new BufferedReader(new FileReader(file));

            String txt;
            StringBuilder sb = new StringBuilder();

            while((txt = br.readLine()) != null){
                sb.append(txt).append("\n");
            }

            txt = sb.toString();

            Zakladka zakladka = null;

            if((z.isKnowFile() == false) && (z.getArea().getText().equals(""))){
                zakladka = z;
                z.getArea().setText(txt);
            } else{
              zakladka = new Zakladka(saveItem);
              tabbedPane1.add(zakladka);
            }

            zakladka.getArea().setText(txt);
            zakladka.setFile(file);
            zakladka.setPath(path);
            zakladka.setSomeChange(false);

            tabbedPane1.setSelectedComponent(zakladka);
            tabbedPane1.setTitleAt(tabbedPane1.getSelectedIndex(), zakladka.getName());

            br.close();
        }

    }

    private void close(Zakladka z){

        if(z.isSomeChange()) {

            int result = JOptionPane.showOptionDialog(null, "Czy chcesz zapisac zmiany pliku: " + z.getName() + "???", "Zapisywanie", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

            if (result == JOptionPane.OK_OPTION) {
                if (file != null) {
                    save(z);
                } else {
                    saveAs(z);
                }
            }
        }

        if(tabbedPane1.getTabCount() != 1) {
            tabbedPane1.remove(z);
        }else {
            System.exit(0);
        }
    }

    private void initMenu(){
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        saveAsItem = new JMenuItem("Save As");
        saveItem = new JMenuItem("Save");
        loadItem = new JMenuItem("Load");
        exitItem = new JMenuItem("Close");

        otherMenu = new JMenu("Text");
        fontIItem = new JMenuItem("Font Increase");
        wrapItem = new JMenuItem("Wrap text");
        fontDItem = new JMenuItem("Font Decrease");
    }

    private void addingAll(JFrame jFrame){
        fileMenu.add(saveAsItem);
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(exitItem);

        otherMenu.add(fontIItem);
        otherMenu.add(fontDItem);
        otherMenu.add(wrapItem);


        menuBar.add(fileMenu);
        menuBar.add(otherMenu);


        jFrame.setJMenuBar(menuBar);
    }

    private void setListenerMenu(){
        saveItem.addActionListener(this);
        saveAsItem.addActionListener(this);
        loadItem.addActionListener(this);

        fontIItem.addActionListener(this);
        fontDItem.addActionListener(this);
        wrapItem.addActionListener(this);
        exitItem.addActionListener(this);
    }

    private void setMnemonicMenu(){
        fileMenu.setMnemonic(KeyEvent.VK_F);
        saveItem.setMnemonic(KeyEvent.VK_S);
        saveAsItem.setMnemonic(KeyEvent.VK_A);
        loadItem.setMnemonic(KeyEvent.VK_L);

        otherMenu.setMnemonic(KeyEvent.VK_O);
        fontIItem.setMnemonic(KeyEvent.VK_I);
        fontDItem.setMnemonic(KeyEvent.VK_D);
        wrapItem.setMnemonic(KeyEvent.VK_W);
        exitItem.setMnemonic(KeyEvent.VK_E);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand() + "/" + e.paramString() + "/" + e.getActionCommand());

        Zakladka z = ((Zakladka)tabbedPane1.getSelectedComponent());

        if(e.getActionCommand().equals("Save")){
            save(z);
        }else if(e.getActionCommand().equals("Save As")){
            saveAs(z);
        }else if(e.getActionCommand().equals("Load")){
            try {
                load(z);
            }catch (IOException f){
                JOptionPane.showMessageDialog(null, "Nie znaleziono pliku", "Bład IO Exception", JOptionPane.WARNING_MESSAGE);

                System.err.println("Blad: " + f);
            }
        }else if(e.getActionCommand().equals("Font Increase")){
            fontDItem.setEnabled(true);
            if(z.increaseSizeText(3)>117){
                fontIItem.setEnabled(false);
            }
        }else if(e.getActionCommand().equals("Font Decrease")){
            fontIItem.setEnabled(true);
            if(z.decreaseSizeText(3)<9) {
                fontDItem.setEnabled(false);
            }
        }else if(e.getActionCommand().equals("Wrap text")){
            z.setWrap(true);
            z.getArea().setLineWrap(true);
            wrapItem.setText("No wrap text");
        }else if(e.getActionCommand().equals("No wrap text")){
            z.setWrap(false);
            z.getArea().setLineWrap(false);
            wrapItem.setText("Wrap text");
        }else if(e.getActionCommand().equals("Close")){
            close(z);
        }

    }
}


