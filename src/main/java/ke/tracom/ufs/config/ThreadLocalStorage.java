package ke.tracom.ufs.config;

public class ThreadLocalStorage {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static String getTenantName() {
        System.err.println( String.format("Fetching active tenant id [%s]",tenant.get()));
        return tenant.get() == null? "crdbtz":tenant.get() ;
    }

}
