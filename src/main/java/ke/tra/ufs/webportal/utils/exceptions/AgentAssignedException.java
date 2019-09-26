package ke.tra.ufs.webportal.utils.exceptions;

public class AgentAssignedException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public AgentAssignedException(){super();}

    public AgentAssignedException(String msg){
        super(msg);
    }
}
