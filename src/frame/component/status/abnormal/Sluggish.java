package frame.component.status.abnormal;

import frame.component.interaction.StanderInteractionComponent;
import frame.component.status.StanderStatusComponent;

public class Sluggish extends StanderStatusComponent {

    public Sluggish(StanderInteractionComponent inflictor, StanderInteractionComponent given) {
        super(inflictor, given);
    }

    @Override
    public void invoke() {
    }
}
