package frame;

import frame.annotation.InitMethod;

import java.awt.*;

import static frame.Element.MAIN_PANEL;

public class InitElement {

    @InitMethod
    public void initMainPanel(){
        MAIN_PANEL.setBackground(new Color(0xAEAEAE));
    }
}
