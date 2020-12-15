//Hello World burp extension taken from https://github.com/PortSwigger/example-hello-world/tree/master/java
package burp;

import mcgyver5.ct_scanner.CTController;
import mcgyver5.ct_scanner.CTSearchTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BurpExtender implements IBurpExtender, IContextMenuFactory {
    IBurpExtenderCallbacks callbacks;
    PrintWriter stdout;
    PrintWriter stderr;
    CTController controller;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        callbacks.setExtensionName("Certificate Transparency Recon Tool");
        callbacks.registerContextMenuFactory(this);
        this.callbacks = callbacks;
        stdout = new PrintWriter(callbacks.getStdout(), true);
        stderr = new PrintWriter(callbacks.getStderr(), true);
        stdout.println("Hello output v0.9.7");
        CTController controller = new CTController();
        controller.setCallbacks(this.callbacks);
        this.controller = controller;
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
                createCTTab(iContextMenuInvocation);
            }
        });
        menuList.add(lookupItem);

        return menuList;
    }

    public void createCTTab(IContextMenuInvocation iContextMenuInvocation){
        controller.lookupCT(iContextMenuInvocation);
        CTSearchTab ct_search_tab = new CTSearchTab(controller);
        this.callbacks.addSuiteTab(ct_search_tab);
    }


}