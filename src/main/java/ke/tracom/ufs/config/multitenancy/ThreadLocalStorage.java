package ke.tracom.ufs.config.multitenancy;

public class ThreadLocalStorage {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static String getTenantName() {
        System.err.printf("Fetching active tenant id [%s]%n", tenant.get());
        return tenant.get();
    }

}
