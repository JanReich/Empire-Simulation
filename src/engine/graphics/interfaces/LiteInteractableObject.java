package engine.graphics.interfaces;

import java.awt.event.MouseEvent;

    public interface LiteInteractableObject extends GraphicalObject {

        void mouseReleased(MouseEvent event);

        void mouseMoved(MouseEvent event);
    }
