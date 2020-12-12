package mcgyver5.ct_scanner;

import mcgyver5.ct_scanner.model.DomainObject;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class NetworkUtils {

    //get Local DNS Settings:
    public List<InetSocketAddress> getDNSServer(){
        //ResolverConfig.getCurrentConfig().servers();
        List<InetSocketAddress> dnsServers = ResolverConfig.getCurrentConfig().servers();
        return dnsServers;
    }

    //DNS lookup
    //Save results
    //
    public String dnsLookup(String domain)  {
        // return the IP address if DNS resolves.
        String r = "";
        domain = domain.replace("*.","");

        try {
            InetAddress result = InetAddress.getByName(domain);
            r = result.getHostAddress();
        } catch (UnknownHostException uhe){
            r = "0";
        }
        return r;
    }
    public HashSet<String> getDomainSetFromApi(){
        String domain = "python.org";
        JSONArray json = new JSONArray();
        String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", domain);
        int answer  = 0;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(apiUrlString);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = client.execute(request,responseHandler);
            JSONParser parser = new JSONParser();
            json = (JSONArray)parser.parse(responseBody);
//            StatusLine sl = rb.getStatusLine();
 //            answer = sl.getStatusCode();
 //           System.out.println(responseBody);
        } catch (IOException e){
            e.printStackTrace();
        } catch (ParseException pe){
            pe.printStackTrace();
        }
        int arraySize = json.size();
        int x;
        HashSet<String> domainSet = new HashSet<>();
        for(x = 0; x < arraySize; x++){
            JSONObject jo = (JSONObject) json.get(x);
            String subdomain = (String)jo.get("name_value");
            String[] stringArray = subdomain.split("\n");
            for (String d : stringArray){
                String dd = d.replace("*.","");
                domainSet.add(dd);
            }
//            String cleanDomain = subdomain.replace(".*","");
            //domainSet.add(subdomain.trim());

        }
        //domainSet.remove("*.python.org");
        System.out.println(domainSet.size());
        return domainSet;
    }
    public ArrayList<DomainObject> getDomainsFromApi(String domain){
        String apiUrlString = String.format("https://crt.sh/?q=%s&output=json", domain);
        try {
            URL url = new URL(apiUrlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ArrayList<DomainObject> domainList = new ArrayList<DomainObject>();
        return domainList;
    }

    public void testHttpClient(){

        CloseableHttpClient client = HttpClients.createDefault();
        String mykey = "D2E7AWjLFdsj6pWYtbYRnVbiQHfPgvDkB1wtPlPe";
        HttpGet request = new HttpGet("https://api.nasa.gov/planetary/apod?api_key="+mykey);
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String responseBody = client.execute(request,responseHandler);
            CloseableHttpResponse rb = client.execute(request);
            StatusLine sl = rb.getStatusLine();
            sl.getStatusCode();
            System.out.println(responseBody);
        } catch (IOException e) {
            System.out.println("err...");
            e.printStackTrace();
        }


    }
        public static void main(String args[]){
            System.out.println("hello");
            NetworkUtils nu = new NetworkUtils();
            nu.testHttpClient();
        }
    }

