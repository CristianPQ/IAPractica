package iapractica;

//~--- non-JDK imports --------------------------------------------------------
import aima.search.framework.SuccessorFunction;

//~--- JDK imports ------------------------------------------------------------
import java.util.List;

public class BSuccessorFunction implements SuccessorFunction {

    @SuppressWarnings("unchecked")
    @Override
    public List getSuccessors(Object aState) {
        Escenario b = (Escenario) aState;
        return b.getTodosSucesores();
    }
}
