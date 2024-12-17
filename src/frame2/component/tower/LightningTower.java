package frame2.component.tower;

import entity.Direction;
import frame2.component.TargetComponent;
import frame2.component.TowerComponent;
import frame2.component.effect.ParticleLightingEffect;
import frame2.pipeLine.TargetLine;

import javax.swing.border.Border;
import java.awt.*;
import java.util.Map;

public class LightningTower extends TowerComponent {

    public LightningTower() {
        super();
    }

    @Override
    protected boolean isSearchingForInteraction() {
        return interactorList.isEmpty();
    }

    @Override
    protected void searchInteraction() {
        Map<Direction, TargetComponent> map = TargetLine.getTargetComponentMap();
        TargetComponent TC = null;
        int distance = 51800;
        for (Map.Entry<Direction, TargetComponent> me : map.entrySet()) {
            if (selectRange.contains(me.getKey())) {
                int dis = Math.abs(compLocation.x - me.getKey().x) + Math.abs(compLocation.y - me.getKey().y);
                if (dis < distance) {
                    TC = me.getValue();
                    distance = dis;
                }
            }
        }
        if (TC != null) {
            interactorList.add(TC);
        }
    }

    @Override
    protected void triggerInteractionEvent() {
        if (atkSchedule >= atkInterval) {
            atkSchedule -= atkInterval;
            TargetComponent interact = interactorList.get(0);
            if (selectRange.contains(interact.getCompDirection())) {
                ParticleLightingEffect PLE = new ParticleLightingEffect(this, interact);
                PLE.register(getContainer());
                int residue = interact.getResidueEndurance(atkValue);
                if (residue <= 0) {
                    interactorList.remove(interact);
                }
            } else {
                interactorList.remove(interact);
            }
        } else {
            atkSchedule += atkLoad;
        }
    }

    @Override
    protected void setContent(Graphics2D g2d, int width, int height) {
        g2d.setColor(new Color(57, 113, 92));
        g2d.drawRoundRect(0, 0, width, height, width / 2, height / 2);
    }
}
