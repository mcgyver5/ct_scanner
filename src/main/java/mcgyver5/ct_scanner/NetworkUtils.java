package mcgyver5.ct_scanner;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xbill.DNS.*;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.util.HashSet;
import java.util.List;

public class NetworkUtils {
    private PrintWriter stdout;

    public NetworkUtils(PrintWriter stdout) {

        stdout.println("new NU");
        this.stdout = stdout;
    }

    public NetworkUtils() {
    }

    //get Local DNS Settings:
    public List<InetSocketAddress> getDNSServer() {
        //ResolverConfig.getCurrentConfig().servers();
        List<InetSocketAddress> dnsServers = ResolverConfig.getCurrentConfig().servers();
        return dnsServers;
    }

    //DNS lookup
    //Save results
    //
    public String dnsLookup(final String domain) {

        String r = "";
        String cleandomain = domain.replace("*.", "");

        try {
            InetAddress result = InetAddress.getByName(cleandomain);
            r = result.getHostAddress();
        } catch (UnknownHostException uhe) {
            r = "0";
        }

        return r;
    }

    public HashSet<String> getDomainSetFromApi(String s) {
        JSONArray json = new JSONArray();
        String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", s);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrlString);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = client.execute(request, responseHandler);
            JSONParser parser = new JSONParser();
            json = (JSONArray) parser.parse(responseBody);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        int arraySize = json.size();
        int x;
        HashSet<String> domainSet = new HashSet<>();
        for (x = 0; x < arraySize; x++) {
            JSONObject jo = (JSONObject) json.get(x);
            String subdomain = (String) jo.get("name_value");
            String[] stringArray = subdomain.split("\n");
            for (String d : stringArray) {
                String dd = d.replace("*.", "");
                domainSet.add(dd);
            }
        }
        return domainSet;
    }
//    public ArrayList<DomainObject> getDomainsFromApi(String domain){
    //String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", domain);
    //try {
    //    URL url = new URL(apiUrlString);
    //} catch (MalformedURLException e) {
    //    e.printStackTrace();
    //}
    //ArrayList<DomainObject> domainList = new ArrayList<DomainObject>();
    //return domainList;
//    }

}
