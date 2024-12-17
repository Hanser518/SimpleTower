package frame2.component.effect;

import frame2.component.EffectComponent;
import frame2.component.TargetComponent;
import frame2.component.TowerComponent;
import frame2.pipeLine.EffectLine;

import javax.swing.*;
import java.awt.*;

public class ResistEffect extends EffectComponent {

    private boolean init = false;

    private TargetComponent target;

    private TowerComponent tower;


    public ResistEffect(JPanel interaction1, JPanel interaction2) {
        super(interaction1, interaction2);
        if (interaction1 instanceof TargetComponent tc) {
            target = tc;
        }
        if (interaction2 instanceof TowerComponent tc) {
            tower = tc;
        }
    }

    public void register(JLayeredPane container) {
        super.register(container);
        EffectLine.addToPrepareComponents(this);
    }

    @Override
    public void incident() {
        // 目标死亡或超出范围
        if (!target.isAlive() || !target.getCompDirection().equals(tower.getCompLocation()) || !tower.isAlive()) {
            if (init) {
                int resistRequire = target.getResistCount();
                tower.updateResistCount(resistRequire);
                target.setMotionState(true);
            }
            EffectLine.addToRemoveComponents(this);
            return;
        }
        // 初始化效果
        if (!init) {
            int resistRequire = target.getResistCount();
            int resistCount = tower.getResistCount();
            if (resistCount >= resistRequire) {
                init = true;
                tower.updateResistCount(-resistRequire);
            }
        } else {
            // 奏效
            boolean alive = tower.isAlive();
            target.setMotionState(!alive);
        }
    }

    @Override
    protected void setContent(Graphics2D g, int width, int height) {

    }
}
