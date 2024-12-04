package frame;

import frame.annotation.InitMethod;

import java.awt.*;

import static frame.Element.MAIN_PANEL;
import static frame.Element.TEST_LABEL;

public class InitElement {

    @InitMethod
    public void initMainPanel(){
        MAIN_PANEL.setBackground(new Color(0xAEAEAE));
    }

    @InitMethod
    public void initTestLabel(){
        TEST_LABEL.setBounds(0, 0, 100, 50);
        TEST_LABEL.setBackground(new Color(0x806FAB9B));
        TEST_LABEL.setOpaque(true);
    }
}
