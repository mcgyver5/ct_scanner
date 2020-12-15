package mcgyver5.ct_scanner;

import burp.ITab;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
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
        this.domainTable = new JTable(new DomainTableModel());
        this.domainTable.setRowHeight(30);
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JPanel topPane = new JPanel();
        JButton scopeButton = new JButton("Add Selected Domains To Scope");
        scopeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.addSelectedToScope();
            }
        });
        JButton dnsButton = new JButton("Check if domains resolve");
        JButton saveButton = new JButton("Save Results");
        JButton httpButton = new JButton("Check HTTP Status");
        topPane.add(scopeButton);
        topPane.add(dnsButton);
        topPane.add(httpButton);
        topPane.add(saveButton);
        DomainTableModel tableModel = new DomainTableModel();
        tableModel.setData(controller.getData());
        JTable table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        splitPane.setTopComponent(topPane);
        splitPane.setBottomComponent(scrollPane);
        return splitPane;
    }

    public void setDomainList(ArrayList<String> domainList){
        this.domainList = domainList;
    }

    public void setStdOut(PrintWriter stdout) {
        this.stdout = stdout;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }
}
