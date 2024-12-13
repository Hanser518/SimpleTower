package frame.component.interaction.target;

import entity.Direction;
import frame.component.incident.StanderIncidentComponent;
import frame.pipeLine.GlobalIncidentLine;
import frame.pipeLine.GlobalParticleLine;
import method.way.BuildSolution;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static common.Constant.FRAME_REFRESH_INTERVAL;
import static common.Constant.UNIT_MOVE_COUNT;
import static common.FrameConstant.*;
import static frame.pipeLine.GlobalIncidentLine.removeComponents;

public class TargetComponent extends JPanel implements StanderIncidentComponent {

    /**
     * 地图数据
     */
    private static int[][] mapMatrix = null;

    /**
     * 移动路径
     */
    private List<Direction> motionPath = new ArrayList<>();

    /**
     * 点位标识
     */
    private Direction targetLocation;

    /**
     * 每单位移动量
     */
    private int frameMotionValue = 0;

    /**
     * 移动次数
     */
    private int motionCount = 0;

    /**
     * 承载容器
     */
    private static JLayeredPane container = null;

    /**
     * state
     */
    private boolean aliveState = true;

    /**
     * blood
     */
    private int blood = 20;



    private int bloodLimit = 20;

    /**
     * 阻挡状态
     */
    private boolean towerResist = false;

    /**
     * 阻挡组件
     */
    private JPanel resistComponent = null;

    /**
     * atk value
     */
    private final Integer atkValue = 1;

    /**
     * atk load
     */
    private int atkLoad = 20;

    /**
     * atk interval
     */
    private final Integer atkInterval = FRAME_REFRESH_INTERVAL / 3;

    /**
     *
     */
    public TargetComponent() {
        super(null);
        setBackground(new Color(131, 38, 56, 255));
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(9, 30, 67));
                g.drawRect(x + 1, y + 1, width - 2, 6);
                g.fillRect(x + 1, y + 1, (int) ((width - 2) * ((float) blood / bloodLimit)), 6);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return null;
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static JLayeredPane getContainer() {
        return container;
    }


    public static void setMatrix(int[][] sceneMatrix) {
        mapMatrix = sceneMatrix;
    }

    /**
     * 注册组件并添加到全局动作管线
     */
    public void register(JLayeredPane panel, Point start, Point end) {
        container = panel;
        // 计算路径、初始化点位
        motionPath = BuildSolution.getMapWay(mapMatrix, start, end);
        targetLocation = motionPath.get(0);
        // 获取帧位移量
        frameMotionValue = UNIT_SIZE / UNIT_MOVE_COUNT;
        GlobalIncidentLine.addToPrepareComponents(this);
    }

    @Override
    public void incident() {
        // 检测是否为阻挡状态
        if (!towerResist) {
            // 获取当前坐标
            Point location = getLocation();
            // 获取移动方向
            int dir = targetLocation.direction;
            switch (dir) {
                case 0, 2 -> {
                    int dist = (dir == 0 ? -1 : 1) * frameMotionValue;
                    this.setLocation(location.x, location.y + dist);
                }
                case 1, 3 -> {
                    int dist = (dir == 3 ? -1 : 1) * frameMotionValue;
                    this.setLocation(location.x + dist, location.y);
                }

            }
            Color colorNow = getBackground();
            setBackground(new Color(colorNow.getRed(), colorNow.getGreen(), colorNow.getBlue(), (int) (255 * ((double) blood / bloodLimit))));
            // 计算移动步骤
            motionCount += 1;
            if (motionCount == UNIT_MOVE_COUNT) {
                motionCount = 0;
                int index = motionPath.indexOf(this.targetLocation);
                if (index == motionPath.size() - 1) {
                    removeComponents.add(this);
                    aliveState = false;
                } else {
                    this.targetLocation = motionPath.get(index + 1);
                }
            }
        } else {
            if (resistComponent != null){
                if (atkLoad > atkInterval) {
                    Class<?> resistClass = resistComponent.getClass();
                    try{
                        Method getRestValue = resistClass.getMethod("getRestValue", int.class);
                        int resistValue = (int)getRestValue.invoke(resistComponent, atkValue);
                        if (resistValue < 0){
                            cancelResist();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    atkLoad = 0;
                } else {
                    atkLoad++;
                }
            }
        }
    }

    /**
     * 获取矩阵坐标
     *
     * @return
     */
    public Direction getTargetLocation() {
        return targetLocation;
    }

    public boolean isAliveState() {
        return aliveState;
    }

    public int getRestValue(int damage) {
        blood -= damage;
        if (blood < 2) {
            removeComponents.add(this);
            aliveState = false;
            GlobalParticleLine.registerBrokenParticle(container, this, getWidth() / 2);
        }
        return blood;
    }

    public int getDirection() {
        return targetLocation.direction;
    }

    public void setResist(JPanel component){
        towerResist = true;
        this.resistComponent = component;
    }

    public void cancelResist(){
        towerResist = false;
        this.resistComponent = null;
    }
}
