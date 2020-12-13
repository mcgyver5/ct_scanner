//Hello World burp extension taken from https://github.com/PortSwigger/example-hello-world/tree/master/java
package burp;

import mcgyver5.ct_scanner.CTSearchTab;
import mcgyver5.ct_scanner.DomainTableModel;
import mcgyver5.ct_scanner.NetworkUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BurpExtender implements IBurpExtender, IContextMenuFactory {
    IBurpExtenderCallbacks callbacks;
    PrintWriter stdout;
    PrintWriter stderr;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        callbacks.setExtensionName("Certificate Transparency Recon Tool");
        callbacks.registerContextMenuFactory(this);
        this.callbacks = callbacks;
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        stdout.println("Hello output v0.9.7");
        
        callbacks.issueAlert("Hello alerts");

    }

    @Override
    public List<JMenuItem> createMenuItems(IContextMenuInvocation iContextMenuInvocation) {
        stdout.println("creatin menu Items");
        ArrayList<JMenuItem> menuList = new ArrayList<JMenuItem>();
        JMenuItem lookupItem = new JMenuItem("Lookup Domain in CT Logs");
        lookupItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stdout.println("Action Performed");
                lookupCT(iContextMenuInvocation);
            }
        });
        menuList.add(lookupItem);

        return menuList;
    }



    private void lookupCT(IContextMenuInvocation context) {
        NetworkUtils nu = null;
        IHttpRequestResponse[] traffic = context.getSelectedMessages();
        String domain = null;
        for(IHttpRequestResponse message : traffic){
            IHttpService service = message.getHttpService();
            String hostString = service.getHost();
            domain = hostString.replace("www.","");
        }
        HashSet<String> domainSet = null;
        nu = new NetworkUtils(this.stdout);
        domainSet = nu.getDomainSetFromApi(domain);
        ArrayList<String> domainList = new ArrayList<>(domainSet);
        int id = 0;
        int rowIndex =0;
        int numrows = domainList.size();
        Object[][] data = new Object[numrows][5];
        for(String domainString : domainList){
            Object[] row = {id,domainString,"0.0.0.0",200,true};
            data[rowIndex] = row;
            rowIndex = rowIndex + 1;
            id = id + 1;
        }

        //tableModel.setData(data);
       CTSearchTab ct_search_tab = new CTSearchTab(data);
        ct_search_tab.setStdOut(this.stdout);
//        ct_search_tab.setData(data);
       //ct_search_tab.setDomainList(domainList);
       this.stdout.println("Adding SuiteTab");
       this.callbacks.addSuiteTab(ct_search_tab);
    }
}