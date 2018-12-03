package engine.graphics.interfaces;

import engine.toolBox.DrawHelper;

public interface GraphicalObject {

    void update(double delta);

    void draw(DrawHelper draw);
}
