package ke.co.tra.ufs.tms.config.multitenancy;

public class ThreadLocalStorage {

    private static ThreadLocal<String> tenant = new ThreadLocal<>();
    private static ThreadLocal<String> language = new ThreadLocal<>();

    public static void setTenantName(String tenantName) {
        tenant.set(tenantName);
    }

    public static String getTenantName() {
        Class<?> walker = java.lang.StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE).getCallerClass();
        System.err.printf("Fetching active tenant id [%s] >>> caller [%s]%n", tenant.get(), walker.getSimpleName());
        return tenant.get() == null ? "0" : tenant.get();
    }

    public static void setLanguage(String lang) {
        language.set(lang);
    }

    public static String getLanguage() {
        System.err.printf("Fetching language from the request [%s]%n", language.get());
        // use english as the default language
        return language.get() == null ? "en" : language.get();
    }
}
