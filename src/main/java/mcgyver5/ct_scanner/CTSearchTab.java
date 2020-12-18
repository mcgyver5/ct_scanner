package mcgyver5.ct_scanner;

import burp.ITab;
import mcgyver5.ct_scanner.model.SubDomain;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;

public class CTSearchTab implements ITab {
    private Object[][] data;
    private JTable domainTable;
    private ArrayList<String> domainList;
    private PrintWriter stdout;
    private CTController controller;

   // public CTSearchTab(Object[][] data) {
     public CTSearchTab(CTController controller){
        this.controller = controller;
        // this.data = data;
    }

    @Override
    public String getTabCaption() {
        return "CT Search";
    }

    @Override
    public Component getUiComponent() {
        DomainTableModel tableModel = new DomainTableModel(controller);
        this.domainTable = new JTable(tableModel);
        this.domainTable.setRowHeight(30);
        tableModel.setData(controller.getData());
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        JTable table = new JTable(tableModel);
        TableRowSorter<DomainTableModel> rowSorter = new TableRowSorter<DomainTableModel>((DomainTableModel)table.getModel());
        table.setRowSorter(rowSorter);

        JPanel topPane = new JPanel();
        JButton scopeButton = new JButton("Add Selected Domains To Scope");
        JButton dnsButton = new JButton("Resolve Selected Domains");
        JButton httpButton = new JButton("Check HTTP Status");
        JButton unCheckButton = new JButton("Uncheck");
        JButton checkButton = new JButton("Check All");
        JButton saveButton = new JButton("Save Results");
        JTextField searchField = new JTextField(20);

        scopeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addSelectedToScope();
            }
        });

        dnsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.resolveSelected();
                tableModel.fireTableDataChanged();
                table.updateUI();
            }
        });

        unCheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stdout.println("action of uncheckbutton");
                java.util.List<SubDomain> subDomainList = tableModel.getData();
                int rowsInView = table.getRowCount();
                for(int viewRow = 0; viewRow < rowsInView; viewRow++){
                    int vr = table.convertRowIndexToModel(viewRow);
                    SubDomain subDomain = subDomainList.get(vr);
                    subDomain.setSelected(false);
                    tableModel.fireTableDataChanged();
                }
                tableModel.fireTableDataChanged();
                table.updateUI();
                table.repaint();
            }
        });

        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean b = SwingUtilities.isEventDispatchThread();
                stdout.println("is EDT: " + b);

                java.util.List<SubDomain> subDomainList = tableModel.getData();
                int rowsInView = table.getRowCount();
                for(int viewRow = 0; viewRow < rowsInView; viewRow++){
                   int vr = table.convertRowIndexToModel(viewRow);
                   SubDomain subDomain = subDomainList.get(vr);
                   subDomain.setSelected(true);
                   tableModel.fireTableDataChanged();
                }
                tableModel.getUpdatesFromDataKeeper();
                //controller.checkAll();
                tableModel.fireTableDataChanged();
                table.updateUI();
            }
        });
        httpButton.addActionListener(new ActionListener() {
            // domain must be both onscreen and checked to be acted upon.
            @Override
            public void actionPerformed(ActionEvent e) {
                java.util.List<SubDomain> subDomainList = tableModel.getData();
                int rowsInView = table.getRowCount();
                for(int viewRow = 0; viewRow < rowsInView; viewRow++){
                    int modelRow = table.convertRowIndexToModel(viewRow);
                    SubDomain subDomain = subDomainList.get(modelRow);
                    if(subDomain.isSelected()){
                        try {
                            InetAddress ipAddress = InetAddress.getByName(subDomain.getDomain());
                            String addressString = ipAddress.getHostAddress();
                            subDomain.setIPAddress(addressString);
                            if(InetAddress.getByName(subDomain.getDomain()).isReachable(1000)){
                                subDomain.setHttpStatus(1337);
                            } else {
                                subDomain.setHttpStatus(9009);
                            };
                        } catch (IOException ioException) {
                            subDomain.setIPAddress("err");
                            subDomain.setHttpStatus(8008);
                            ioException.printStackTrace();
                        }
                    }
                }
                controller.checkReachable();
                tableModel.fireTableDataChanged();
                table.updateUI();
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileWriter fw = null;
                String fileName = "";
                JFileChooser fileChooser = new JFileChooser();
                if(fileChooser.showOpenDialog(topPane) == JFileChooser.APPROVE_OPTION){
                    fileName =fileChooser.getSelectedFile().getAbsolutePath();
                }

                try {
                    fw = new FileWriter(fileName);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                java.util.List<SubDomain> subDomainList = tableModel.getData();
                String sep = System.lineSeparator();
                int rowsInView = table.getRowCount();
                for(int viewRow = 0; viewRow < rowsInView; viewRow++){
                    int vr = table.convertRowIndexToModel(viewRow);
                    String domainString = subDomainList.get(vr).getDomain();
                    try {
                        fw.write(domainString + sep);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                try {
                    fw.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();
                stdout.println(text);
                if(text.trim().length() == 0){
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    tableModel.fireTableDataChanged();

                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();
                stdout.println("removeUpdate");
                stdout.println(text);
                if(text.trim().length() == 0){
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                    tableModel.fireTableDataChanged();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                stdout.println("ChangedUpdate...?");
            }
        });
        topPane.add(scopeButton);
        topPane.add(dnsButton);
        topPane.add(httpButton);
        topPane.add(saveButton);
        topPane.add(searchField);
        topPane.add(unCheckButton);
        topPane.add(checkButton);

        JScrollPane scrollPane = new JScrollPane(table);
        splitPane.setTopComponent(topPane);
        splitPane.setBottomComponent(scrollPane);
        return splitPane;
    }

    public void setStdOut(PrintWriter stdout) {
        this.stdout = stdout;
    }

    public List getSelectedRows(){
         return null;
    }
}
