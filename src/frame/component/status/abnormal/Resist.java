package frame.component.status.abnormal;

import frame.component.interaction.StanderInteractionComponent;
import frame.component.status.StanderStatusComponent;

public class Resist extends StanderStatusComponent {

    public Resist(StanderInteractionComponent inflictor, StanderInteractionComponent given) {
        super(inflictor, given);
    }

    @Override
    public void invoke() {
        if (inflictor.isAliveState()){
            given.setWorking();
        } else {
            given.resetWorking();
        }
    }
}
