package engine.toolBox;

import engine.graphics.Display;
import engine.graphics.interfaces.GraphicalObject;

import java.awt.*;

public class Textfield implements GraphicalObject {

                //Attribute
            private int x;
            private int y;
            private int width;
            private int height;

                //Referenzen
            private Font font;
            private Inputmanager input;

        public Textfield(Display display, Font font, int x, int y, int width, int height) {

            this.x = x;
            this.y = y;
            this.font = font;
            this.width = width;
            this.height = height;

            input = new Inputmanager();
            display.getActivePanel().addManagement(input);
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

            draw.setFont(font);
            if(x + draw.getFontWidth(input.getInputQuerry()) > x + width - 20) {

                input.setInputQuerry(input.getInputQuerry().substring(0, input.getInputQuerry().length() - 1));
            }
        }

        public Inputmanager getInput() {

            return input;
        }

        public int getX() {

            return x;
        }

        public int getY() {

            return y;
        }

        public int getWidth() {

            return width;
        }

        public int getHeight() {

            return height;
        }
    }
