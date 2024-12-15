package frame2.component.scene;

import common.Element;
import frame2.component.SceneComponent;
import method.map.BuildMap;
import method.map.TransMap;

import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.MAIN_LAYER;

/**
 * 地图容器类，无需注册到执行线程中
 */
public class StageComponent extends SceneComponent {

    /**
     * 场景宽高
     */
    protected int sceneHeight;

    protected int sceneWidth;

    /**
     * 数值矩阵
     */
    protected int[][] sceneMatrix;

    /**
     * 组件矩阵
     */
    protected SceneComponent[][] panelMatrix;

    public StageComponent(int width, int height) {
        super();
        // 初始化矩阵
        initMatrix(width, height);
        initContent();
    }

    @Override
    public void incident() {

    }

    @Override
    protected void setContent(Graphics g, int width, int height) {

    }

    /**
     * 根据输入宽高获取相应矩阵
     */
    private void initMatrix(int width, int height){
        sceneMatrix = BuildMap.getMap(width, height);
        sceneMatrix = TransMap.getTransMatrix(sceneMatrix);
        sceneWidth = sceneMatrix.length;
        sceneHeight = sceneMatrix[0].length;
        this.setBounds(0, 0, sceneWidth * UNIT_WIDTH, sceneHeight * UNIT_HEIGHT);
    }

    /**
     * 创建组件矩阵
     */
    private void initContent() {
        panelMatrix = new SceneComponent[sceneWidth][sceneHeight];
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                if (i != 0 && j != 0 && i < panelMatrix.length - 1 && j < panelMatrix[0].length - 1) {
                    double r = Math.random();
                    if (r > 0.8) {
                        sceneMatrix[i][j] = 0;
                    }
                }
                if (sceneMatrix[i][j] == 1){
                    panelMatrix[i][j] = new PlatformComponent();
                } else {
                    panelMatrix[i][j] = new FloorComponent();
                }
                panelMatrix[i][j].setLocation(i * UNIT_WIDTH, j * UNIT_HEIGHT);
                panelMatrix[i][j].register(MAIN_LAYER);
            }
        }
    }

    /**
     * 获取组件宽度
     * @return
     */
    public int getScreenWidth() {
        return sceneWidth;
    }

    /**
     * 获取组件高度
     * @return
     */
    public int getSceneHeight() {
        return sceneHeight;
    }

    /**
     * 获取数据矩阵
     * @return
     */
    public int[][] getSceneMatrix(){
        return sceneMatrix;
    }

    /**
     * 获取panel矩阵
     * @return
     */
    public SceneComponent[][] getPanelMatrix(){
        return panelMatrix;
    }
}
