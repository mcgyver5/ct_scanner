package mcgyver5.ct_scanner;

import burp.ITab;
import mcgyver5.ct_scanner.model.DomainObject;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CTSearchTab implements ITab {
    private JTable domainTable;
    private ArrayList<DomainObject> domainList;

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
        JButton dnsButton = new JButton("Check if domains resolve");
        JButton saveButton = new JButton("Save Results");
        JButton httpButton = new JButton("Check HTTP Status");
        topPane.add(scopeButton);
        topPane.add(dnsButton);
        topPane.add(httpButton);
        topPane.add(saveButton);
        JScrollPane scrollPane = new JScrollPane();
        splitPane.setTopComponent(topPane);
        splitPane.setBottomComponent(scrollPane);
        return splitPane;
    }

    public void setDomainList(ArrayList<DomainObject> domainList){
        this.domainList = domainList;
    }
}
