package mcgyver5.ct_scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import sun.nio.ch.Net;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class NetworkUtilsTest {
    NetworkUtils nu;
    HashSet<String> domains;
    @Before
    public void setUp() throws Exception {
        System.out.println("Setup runs once, right?");
        nu = new NetworkUtils();
        domains = nu.getDomainSetFromApi();
        System.out.println(domains.size());
    }

    @Test
    public void dnsLookup() {
        String expected = "188.166.48.69";
        String actual = "";
        actual = nu.dnsLookup("bugs.python.org");
        assertEquals("Message", expected, actual);
    }

   //
   // @Test
    //public void dnsLookupBad(){
    //    String expected = "0";
    //    String actual = "";
    //    actual = nu.dnsLookup("busdfgs.python.org");
    //    assertEquals("Message", expected, actual);
    //}
//    @Test
//    public void dnsLookupStar(){
//        String expected = "188.166.48.69";
//        NetworkUtils nu = new NetworkUtils();
//        String actual = "";
//        actual = nu.dnsLookup("*.bugs.python.org");
//        assertEquals("Message", expected, actual);
//    }
//
    @Test
    public void dnsSettings(){
        NetworkUtils nu = new NetworkUtils();
        int expected = 3;
        int actual = 0;
        List<InetSocketAddress> dnsList = nu.getDNSServer();
        for (InetSocketAddress addr : dnsList){
            System.out.println(addr.getAddress().getHostAddress());
        }
        actual = dnsList.size();
        assertEquals(expected, actual);
    }
    @Test
    public void crtReturnsJSON(){
        assertThat(domains, instanceOf(HashSet.class));
    }

    @Test
    public void domainSetDoesNotContainStars() {
        boolean b = domains.contains("*.python.org");
        System.out.println("size: " + domains.size());
        for(String d:domains){
            System.out.println(d);
        }
        assertFalse(b);
    }
}