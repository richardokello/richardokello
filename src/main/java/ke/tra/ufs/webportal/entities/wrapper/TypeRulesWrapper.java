package ke.tra.ufs.webportal.entities.wrapper;

public class TypeRulesWrapper {
    private Long id;
    private Short active;

    public TypeRulesWrapper() {
    }

    public TypeRulesWrapper(Long id, Short active) {
        this.id = id;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Short isActive() {
        return active;
    }

    public void setActive(Short active) {
        this.active = active;
    }
}
