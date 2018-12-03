package engine.toolBox;

import engine.graphics.interfaces.AdvancedInteractableObject;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button implements AdvancedInteractableObject {

            //Attribute
        private double x;
        private double y;
        private double width;
        private double height;

        private boolean hover;
        private boolean inside;
        private boolean clicked;

            //Referenzen
        private BufferedImage normal;
        private BufferedImage hoverImg;

    public Button(double x, double y, double width, double height, String path, boolean hover) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.hover = hover;
        this.normal = ImageHelper.getImage(path + "ShopItemKaserneMax.png");
        if(hover) this.hoverImg = ImageHelper.getImage(path + "-hover.png");
    }

    @Override
    public void draw(DrawHelper draw) {

    }

    public void paint(DrawHelper draw) {

        if(hover) {

            if(!inside) draw.drawImage(normal, x, y, width, height);
            else draw.drawImage(hoverImg, x, y, width, height);
        } else draw.drawImage(normal, x, y, width, height);
    }

    @Override
    public void mouseReleased(MouseEvent event) {

        clicked = false;
}

    @Override
    public void mousePressed(MouseEvent e) {

        if(e.getX() >= (int) x && e.getX() <= (int) (x + width) && e.getY() >= (int) y && e.getY() <= (int) (y + height))
        clicked = true;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

        if(e.getX() >= (int) x && e.getX() <= (int) (x + width) && e.getY() >= (int) y && e.getY() <= (int) (y + height)) inside = true;
        else inside = false;

    }

    @Override
    public void update(double delta) {

    }

    @Override
    public void mouseDragged(MouseEvent event) {

    }

    @Override
    public void keyPressed(KeyEvent event) {

    }

    @Override
    public void keyReleased(KeyEvent event) {

    }

        // --------------- GETTER & SETTER --------------- \\


    public boolean isClicked() {

        boolean temp = clicked;
        clicked = false;
        return temp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public boolean isHover() {
        return hover;
    }

    public boolean isInside() {
        return inside;
    }

    public BufferedImage getNormal() {
        return normal;
    }

    public BufferedImage getHoverImg() {
        return hoverImg;
    }
}
