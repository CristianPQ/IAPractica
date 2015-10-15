package iapractica;

import aima.search.framework.GoalTest;

public class BGoalTest implements GoalTest {

    @Override
    public boolean isGoalState(Object state) {
        Escenario e = (Escenario) state;
        System.out.println("goalstate " + e.isGoalState());
        return e.isGoalState();
    }
}
