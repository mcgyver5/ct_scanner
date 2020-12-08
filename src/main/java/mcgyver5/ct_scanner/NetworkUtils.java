package mcgyver5.ct_scanner;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    //DNS lookup
    //Save results
    //
    public String dnsLookup(String domain) throws UnknownHostException {
        // return the IP address if DNS resolves.
        InetAddress result = InetAddress.getByName(domain);
        String r = result.getHostAddress();
        return r;
    }
}
