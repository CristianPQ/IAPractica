package iapractica;

import aima.search.framework.HeuristicFunction;

public class BHeuristicFunction implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        Escenario e = (Escenario) state;
        return e.heuristicValue(0);
    }

}
