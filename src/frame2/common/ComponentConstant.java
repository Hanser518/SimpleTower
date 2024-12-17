package frame2.common;

import frame2.component.TowerComponent;
import frame2.component.scene.CandidateScene;

import java.util.ArrayList;
import java.util.List;

public class ComponentConstant {

    /**
     * 每单位块宽高
     */
    public static int UNIT_WIDTH = 64;

    public static int UNIT_HEIGHT = 64;

    /**
     * target 每单位移动次数
     */
    public static int MOVE_COUNT = 20;

    /**
     * candidate
     */
    public static CandidateScene CANDIDATE;

    /**
     * Candidate list
     */
    public static List<TowerComponent> CANDIDATE_LIST = new ArrayList<>();
}
