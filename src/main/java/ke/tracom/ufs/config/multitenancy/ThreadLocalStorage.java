package ke.tracom.ufs.config.multitenancy;

public class ThreadLocalStorage {

    private static final ThreadLocal<String> tenant = ThreadLocal.withInitial(() -> "0");
    private static final ThreadLocal<String> language = ThreadLocal.withInitial(() -> "en");

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static String getTenantName() {
        System.err.printf("Fetching active tenant id [%s] \n", tenant.get());
        return tenant.get() == null ? "0" : tenant.get();
    }

    public static void setLanguage(String lang) {
        language.set(lang);
    }

    public static String getLanguage() {
        System.err.printf("Fetching language from the request [%s]%n", language.get());
        // use english as the default language
        return language.get();
    }
}
