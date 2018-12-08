package gamePackage.Buildings;

import engine.graphics.interfaces.GraphicalObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import engine.toolBox.SpriteSheet;
import gamePackage.Game.GameField;

    public class Path implements GraphicalObject {

                //Attribute
            private int posX;
            private int posY;

                //Referenzen
            private SpriteSheet path;
            private GameField gameField;

            private String neighbour0;
            private String neighbour1;
            private String neighbour2;
            private String neighbour3;

        public Path(GameField gameField, int posX, int posY, String neighbour0, String neighbour1, String neighbour2, String neighbour3) {

            this.posX = posX;
            this.posY = posY;
            this.gameField = gameField;
            this.neighbour0 = neighbour0;
            this.neighbour1 = neighbour1;
            this.neighbour2 = neighbour2;
            this.neighbour3 = neighbour3;

            path = new SpriteSheet(ImageHelper.getImage("res/images/Buildings/PathTiles.png"), 3, 3,true);
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

                //Feld 1 (links - oben)
            if(neighbour0.equalsIgnoreCase("o") && neighbour1.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(0, 0), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            } else if (neighbour0.equalsIgnoreCase("x") && neighbour1.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(1, 0), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            }  else if (neighbour0.equalsIgnoreCase("o") && neighbour1.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(0, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            } else if(neighbour0.equalsIgnoreCase("x") && neighbour1.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(1, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            }

                //Feld 2 (rechts - oben)
            if(neighbour1.equalsIgnoreCase("o") && neighbour2.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(2, 0), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            } else if(neighbour1.equalsIgnoreCase("x") && neighbour2.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(2, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            } else if(neighbour1.equalsIgnoreCase("o") && neighbour2.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(1, 0), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            } else if(neighbour1.equalsIgnoreCase("x") && neighbour2.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(1, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY), 25, 25);
            }

                //Feld 3 (link - unten)
            if(neighbour0.equalsIgnoreCase("o") && neighbour2.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(0, 2), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour0.equalsIgnoreCase("x") && neighbour2.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(1, 2), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour0.equalsIgnoreCase("o") && neighbour2.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(0, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour0.equalsIgnoreCase("x") && neighbour2.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(1, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            }

                //Feld 4 (rechts - unten)
            if(neighbour2.equalsIgnoreCase("o") && neighbour3.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(2, 2), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour2.equalsIgnoreCase("x") && neighbour3.equalsIgnoreCase("o")) {

                draw.drawImage(path.getSubImage(1, 2), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour2.equalsIgnoreCase("o") && neighbour3.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(2, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            } else if(neighbour2.equalsIgnoreCase("x") && neighbour3.equalsIgnoreCase("x")) {

                draw.drawImage(path.getSubImage(1, 1), gameField.getFieldX() + (gameField.getFieldSquareSize() * posX) + gameField.getFieldSquareSize(), gameField.getFieldY() + (gameField.getFieldSquareSize() * posY) + gameField.getFieldSquareSize(), 25, 25);
            }
        }
    }
