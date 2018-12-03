package gamePackage.Game;

import engine.graphics.interfaces.GraphicalObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;

import java.awt.image.BufferedImage;

    public class Worker implements GraphicalObject {

                //Attribute

                //Referenzen
            private BufferedImage image;

        public Worker() {

            image = ImageHelper.getImage("res/Worker/Woodcutter.png");
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

            draw.drawImage(image, 200, 200, 34, 62);
        }
    }
