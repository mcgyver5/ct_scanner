package mcgyver5.ct_scanner;

import burp.*;
import mcgyver5.ct_scanner.model.SubDomain;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

// This class accepts actions from GUI and uses them to change the table Model.
// it observes changes to TableModel and updates GUI to reflect the changes.
// it has callbacks to Burp as well.  it serves as a local controller
public class CTController {
    IBurpExtenderCallbacks callbacks;
    PrintWriter stdout;
    private DataKeeper keeper;

    public DataKeeper getKeeper() {
        return keeper;
    }

    public void setKeeper(DataKeeper keeper) {
        this.keeper = keeper;
    }

    public List<SubDomain> getData(){
        return this.keeper.getDomains();
    }
    public void setCallbacks(IBurpExtenderCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    // put create the tableModel and give it the data.
    public void lookupCT(IContextMenuInvocation context ) {
        stdout.println("lookin up the domain!");
        NetworkUtils nu = null;
        IHttpRequestResponse[] traffic = context.getSelectedMessages();
        String domain = null;
        for(IHttpRequestResponse message : traffic){
            IHttpService service = message.getHttpService();
            String hostString = service.getHost();
            hostString = hostString.toLowerCase();
            domain = hostString.replace("www.","");
        }
        HashSet<String> domainSet = null;
        nu = new NetworkUtils(stdout);
        domainSet = nu.getDomainSetFromApi(domain);
        ArrayList<String> domainList = new ArrayList<>(domainSet);
        int id = 0;
        DataKeeper keeper = new DataKeeper();
        this.keeper = keeper;
        for(String domainString : domainList){
            SubDomain subDomain = new SubDomain("",domainString,false, 0,id,true,false);
            keeper.addSubdomain(subDomain);
            id = id + 1;
        }

    }

    public void addSelectedToScope() {
        for (SubDomain sd : keeper.getDomains()){
            if (sd.isSelected()){
                try {
                    String urlString = "https://"+sd.getDomain();
                    URL u = new URL(urlString);
                    callbacks.includeInScope(u);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void resolveSelected() {
        NetworkUtils nu = new NetworkUtils();
        for(SubDomain sd : keeper.getDomains()){
            if(sd.isSelected()){
                String address = nu.dnsLookup(sd.getDomain());
                sd.setIPAddress(address);

            }

        }
    }


    public void setStdOut(PrintWriter stdout) {
        this.stdout = stdout;
    }

    public void checkReachable()    {

    }
}
