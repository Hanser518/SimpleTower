package frame.component;

import entity.Direction;
import frame.pipeLine.GlobalMotionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import java.awt.*;

import static frame.pipeLine.GlobalMotionLine.components;

public class TowerComponent extends JPanel {

    /**
     * 类型标识
     */
    public static final Integer TOWER_TYPE_TYPE1 = 1;

    /**
     * 点位标识
     */
    private Direction towerLocation;


    public Direction getTowerLocation() {
        return towerLocation;
    }

    /**
     * 类型
     */
    private Integer type = null;

    /**
     * 容器
     */
    private static JPanel container = null;

    /**
     * 目标
     */
    private TargetComponent target = null;

    /**
     * atk value
     */
    private Integer atkValue;

    /**
     * atk load
     */
    private int atkLoad = 0;

    /**
     * atk interval
     */
    private Integer atkInterval;

    public TargetComponent getTargetComponent() {
        return target;
    }

    public TowerComponent(Integer type) {
        super(null);
        if (type.equals(TOWER_TYPE_TYPE1)) {
            this.type = TOWER_TYPE_TYPE1;
            setBackground(new Color(228, 47, 47));
        } else {
            this.type = 0;
            setBackground(new Color(255, 0, 0, 127));
        }
    }

    /**
     * 注册组件
     */
    public void register(JPanel panel, Point location, int dir) {
        container = panel;
        this.towerLocation = new Direction(location, dir);
        GlobalParticleLine.createParticle(container, this, this.getWidth());
        GlobalMotionLine.addToPrepareComponents(this);
    }

    /**
     * 事务（暂定，后面可能会根据type执行不同的事务）
     */
    public void motion() {
        if (components.isEmpty()) {
            return;
        }
        if (target != null && !target.isAliveState()) {
            target = null;
        }
        if (target == null) {
            TargetComponent prepare = null;
            int dist = 115411;
            for (JPanel rawComponent : components) {
                if (rawComponent instanceof TargetComponent tar) {
                    if (!tar.isFocus()) {
                        Direction tarLocation = tar.getTargetLocation();
                        int distFromTower = (int) Math.sqrt(Math.pow(tarLocation.x - towerLocation.x, 2) + Math.pow(tarLocation.y - towerLocation.y, 2));
                        prepare = distFromTower < dist ? tar : prepare;
                        dist = Math.min(distFromTower, dist);
                    }
                }
            }
            if (prepare != null) {
                prepare.setFocus(this);
                target = prepare;
                atkInterval = atkInterval == null ? 10 : atkInterval;
                atkLoad = 0;
            }
        } else {
            if (atkLoad > atkInterval){
                int targetValue = target.getRestValue(getAtkValue());
                if (targetValue < 0) {
                    target = null;
                }
                atkLoad = 0;
            } else {
                atkLoad ++;
            }
        }
    }

    public int getAtkValue() {
        if (atkValue == null) {
            return 1;
        } else {
            return atkValue;
        }
    }
}
