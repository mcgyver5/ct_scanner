//Hello World burp extension taken from https://github.com/PortSwigger/example-hello-world/tree/master/java
package burp;

import mcgyver5.ct_scanner.CTSearchTab;
import mcgyver5.ct_scanner.NetworkUtils;
import mcgyver5.ct_scanner.model.DomainObject;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class BurpExtender implements IBurpExtender, IContextMenuFactory {
    IBurpExtenderCallbacks callbacks;
    PrintWriter stdout;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks)
    {
        // set our extension name
        callbacks.setExtensionName("Certificate Transparency Recon Tool");
        callbacks.registerContextMenuFactory(this);
        this.callbacks = callbacks;
        // obtain our output and error streams
        stdout = new PrintWriter(callbacks.getStdout(), true);

        // write a message to our output stream
        stdout.println("Hello output v3");
        
        // write a message to the Burp alerts tab
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
        this.stdout.println("action detected");
        String host = "";
       IHttpRequestResponse[] messageArray = context.getSelectedMessages();
       for (IHttpRequestResponse reqResp: messageArray){
           IHttpService service = reqResp.getHttpService();
           host = service.getHost();
       }
       NetworkUtils nu = new NetworkUtils();
       IHttpService service = new IHttpService() {
           @Override
           public String getHost() {
               String crt = "crt.sh";
//               String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", domain);
               return crt;
           }

           @Override
           public int getPort() {
               return 443;
           }

           @Override
           public String getProtocol() {
               return "https";
           }
       };
       String domain = "crt.sh";
        String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", domain);
        byte[] request = apiUrlString.getBytes(StandardCharsets.UTF_8);
       IHttpRequestResponse httpRequestResponse = callbacks.makeHttpRequest(service, request);
        byte[] response = httpRequestResponse.getResponse();
        ArrayList<DomainObject> domainList = nu.getDomainsFromApi(host);
       ITab ct_search_tab = new CTSearchTab();
       //ct_search_tab.setDomainList(domainList);
       this.callbacks.addSuiteTab(ct_search_tab);


    }
}