package ke.co.tra.ufs.tms.config.multitenancy;

public class ThreadLocalStorage {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();
    private static ThreadLocal<String> language = new ThreadLocal<>();

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static void setLanguage(String lang) {
        language.set(lang);
    }

    public static String getTenantName() {
        System.err.printf("Fetching active tenant id [%s]%n", tenant.get());
        return tenant.get();
    }

    public static String getLocalLanguage() {
        System.err.printf("Fetching default language [%s]%n", language.get());
        return language.get() == null ? "en" : language.get();
    }
}
