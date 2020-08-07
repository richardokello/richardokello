package ke.tra.ufs.webportal.entities.enums;

public enum  FeeCycleType {
    DAYS(24 * 60),
    WEEK(7 * 24 * 60),
    MONTHS(30 * 24 * 60),
    YEARS(12 * 30 * 24 * 60);

    private final long hours;

    FeeCycleType(long time) {
        this.hours = time;
    }

    public long getHours() {
        return this.hours;
    }
}
