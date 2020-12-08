package mcgyver5.ct_scanner;

import org.junit.Test;

import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class NetworkUtilsTest {

    @Test
    public void dnsLookup() {
        String expected = "188.166.48.69";
        NetworkUtils nu = new NetworkUtils();
        String actual = "";
        try {
             actual = nu.dnsLookup("bugs.python.org");
        } catch (UnknownHostException e) {
            actual = "0";
            e.printStackTrace();
        }
        assertEquals("Message", expected, actual);
    }

    @Test
    public void dnsLookupBad(){

    }
}