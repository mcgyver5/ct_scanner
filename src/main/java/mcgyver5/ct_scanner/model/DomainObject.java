package mcgyver5.ct_scanner.model;

public class DomainObject {
    private String IPAddress;
    private String domain;
    private boolean resolves;
    private int httpStatus;
    private int id;
    private boolean selected;
    private boolean inScope;

    public DomainObject(String IPAddress, String domain, boolean resolves, int httpStatus, int id, boolean selected, boolean inScope) {
        this.IPAddress = IPAddress;
        this.domain = domain;
        this.resolves = resolves;
        this.httpStatus = httpStatus;
        this.id = id;
        this.selected = selected;
        this.inScope = inScope;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isResolves() {
        return resolves;
    }

    public void setResolves(boolean resolves) {
        this.resolves = resolves;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isInScope() {
        return inScope;
    }

    public void setInScope(boolean inScope) {
        this.inScope = inScope;
    }
}
