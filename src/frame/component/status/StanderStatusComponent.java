package frame.component.status;

import frame.component.interaction.StanderInteractionComponent;

public abstract class StanderStatusComponent {

    protected StanderInteractionComponent inflictor;

    protected StanderInteractionComponent given;

    public StanderStatusComponent(StanderInteractionComponent inflictor, StanderInteractionComponent given){
        this.inflictor = inflictor;
        this.given = given;
    }

    public abstract void invoke();
}
