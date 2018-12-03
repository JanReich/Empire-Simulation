package engine.graphics.interfaces;

import java.awt.event.MouseEvent;

public interface AdvancedInteractableObject extends BasicInteractableObject {

    void mouseMoved(MouseEvent event);

    void mouseDragged(MouseEvent event);

    void mousePressed(MouseEvent event);
}
