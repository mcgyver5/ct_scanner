package mcgyver5.ct_scanner;

import mcgyver5.ct_scanner.model.SubDomain;

import java.util.LinkedList;
import java.util.List;

public class DataKeeper {
    private List<SubDomain> domains;

    public DataKeeper() {
        domains = new LinkedList<SubDomain>();
    }
    public void addSubdomain(SubDomain subDomain){
        domains.add(subDomain);
    }

    public List<SubDomain> getDomains(){
        return domains;
    }
}
