package engine.toolBox;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

    public class DrawHelper {

                //Attribute
            private int screenWidth;
            private int screenHeight;

                //Referenzen
            private Graphics2D g2d;

        public DrawHelper(Graphics2D g2d) {

            this.g2d = g2d;
        }

                //Methoden zum zeichnen

            //Colour
        public void setColour(Color colour) {

            g2d.setColor(colour);
        }

        public void setColour(int r, int g, int b) {

            this.setColour(new Color(r, g, b));
        }

        public void setColour(int r, int g, int b, int alpha) {

            this.setColour(new Color(r, g, b, alpha));
        }

            //Rectangle
        public void drawRec(int x, int y, int scale) {

            this.drawRec(x, y, scale, scale);
        }

        public void drawRec(int x, int y, int width, int height) {

            if(g2d != null) g2d.drawRect(x, y, width, height);
        }

        public void fillRec(int x, int y, int scale) {

            this.fillRec(x, y, scale, scale);
        }

        public void fillRec(int x, int y, int width, int height) {

            if(g2d != null) g2d.fillRect(x, y, width, height);
        }

        public void drawRoundRec(int x, int y, int scale, int arc) {

            this.drawRoundRec(x, y, scale, scale, arc);
        }

        public void drawRoundRec(int x, int y, int width, int height, int arc) {

            if(g2d != null) g2d.drawRoundRect(x, y, width, height, arc, arc);
        }

        public void fillRoundRec(int x, int y, int scale, int arc) {

            this.fillRoundRec(x, y, scale, scale, arc);
        }

        public void fillRoundRec(int x, int y, int width, int height, int arc) {

            if(g2d != null) g2d.fillRoundRect(x, y, width, height, arc, arc);
        }

            //Elipse
        public void drawOval(int x, int y, int scale) {

            this.drawOval(x, y, scale);
        }

        public void drawOval(int x, int y, int width, int height) {

            if(g2d != null) g2d.drawOval(x, y, width, height);
        }

        public void fillOval(int x, int y, int scale) {

            this.fillOval(x, y, scale, scale);
        }

        public void fillOval(int x, int y, int width, int height) {

            if(g2d != null) g2d.fillOval(x, y, width, height);
        }

            //Bilder zeichnen
        public void drawImage(String path, int x, int y) {

            BufferedImage image = ImageHelper.getImage(path);
            if(image != null) this.drawImage(image, x, y, image.getWidth(), image.getHeight());
        }

        public void drawImage(String path, int x, int y, int scale) {

            this.drawImage(path, x, y, scale, scale);
        }

        public void drawImage(String path, int x, int y, int width, int height) {

            this.drawImage(ImageHelper.getImage(path), x, y, width, height);
        }

        public void drawImage(BufferedImage image, int x, int y) {

            this.drawImage(image, x, y, image.getWidth(), image.getHeight());
        }

        public void drawImage(BufferedImage image, int x, int y, int scale) {

            this.drawImage(image, x, y, scale, scale);
        }

        public void drawImage(BufferedImage image, double x, double y, double width, double height) {

            this.drawImage(image, (int) x, (int) y, (int) width, (int) height);
        }

        public void drawImage(BufferedImage image, int x, int y, int width, int height) {

        if(g2d != null && image != null) g2d.drawImage(image, x, y, width, height,null);
        }

        public void drawHollowCircle(int centerX, int centerY, int radius, int thickness) {

            if(g2d != null) {

                Ellipse2D outer = new Ellipse2D.Double(centerX- radius, centerY - radius, radius * 2, radius * 2);
                Ellipse2D inner = new Ellipse2D.Double(centerX - radius + thickness, centerY - radius + thickness, radius * 2 - thickness * 2, radius * 2 - thickness * 2);

                Area area = new Area(outer);
                area.subtract(new Area(inner));

                g2d.draw(area);
            }
        }

        public void fillHollowCircle(int centerX, int centerY, int radius, int thickness) {

            if(g2d != null) {

                Ellipse2D outer = new Ellipse2D.Double(centerX- radius, centerY - radius, radius * 2, radius * 2);
                Ellipse2D inner = new Ellipse2D.Double(centerX - radius + thickness, centerY - radius + thickness, radius * 2 - thickness * 2, radius * 2 - thickness * 2);

                Area area = new Area(outer);
                area.subtract(new Area(inner));

                g2d.fill(area);
            }
        }

            //Button zeichnen
        public void drawButton(Button button) {

            if(g2d != null && button != null) button.paint(this);
        }

        public void drawButton(AnimatedButton button) {

            if(g2d != null && button != null) button.paint(this);
        }

            //Fonts
        public Font createFont(int fontType, int fontSize) {

            return new Font("", fontType, fontSize);
        }

        public Font getFont() {

            if(g2d != null) return g2d.getFont();
            return null;
        }

        public void setFont(Font font) {

            if(g2d != null) g2d.setFont(font);
        }

        public void setFont(int fontType, int fontSize) {

            if(g2d != null) g2d.setFont(new Font("", fontType, fontSize));
        }

        public int getFontWidth(String text) {

            if(g2d != null) return g2d.getFontMetrics().stringWidth(text);
            return -1;
        }

        public int getFontHeight(String text) {

            if(g2d != null) return g2d.getFontMetrics().getHeight();
            return -1;
        }

            //DrawString
        public void drawString(String text, int x, int y) {

            if(g2d != null) g2d.drawString(text, x, y);
        }

            //Stroke
        public void setStroke(int stroke) {

            if(g2d != null) g2d.setStroke(new BasicStroke(stroke));
        }

            //Line
        public void drawLine(int x, int y, int x2, int y2) {

            if(g2d != null) g2d.drawLine(x, y, x2 ,y2);
        }

            //Graphics Update
        public void updateGraphics(Graphics2D g2d) {

            this.g2d = g2d;
        }

            //GETTER & SETTER

        public int getScreenWidth() {

            return screenWidth;
        }

        public void setScreenWidth(int screenWidth) {

            this.screenWidth = screenWidth;
        }

        public int getScreenHeight() {

            return screenHeight;
        }

        public void setScreenHeight(int screenHeight) {

            this.screenHeight = screenHeight;
        }


    }
