package gamePackage.Buildings;

import engine.graphics.Display;
import engine.graphics.interfaces.GraphicalObject;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import gamePackage.Game.GameField;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

    public class PathManagement implements LiteInteractableObject {

                //Attribute
            private boolean move;

                //Referenzen
            private GameField gameField;
            private String[][] pathManagement;

                //Path
            private BufferedImage end0;
            private BufferedImage end1;
            private BufferedImage end2;
            private BufferedImage end3;

            private BufferedImage pathK;
            private BufferedImage pathS;
            private BufferedImage pathW;

            private BufferedImage curv0;
            private BufferedImage curv1;
            private BufferedImage curv2;
            private BufferedImage curv3;

            private BufferedImage pathT0;
            private BufferedImage pathT1;
            private BufferedImage pathT2;
            private BufferedImage pathT3;

            private Display display;
            private BuildingOverlay overlay;
            private BufferedImage pathNormal;

        public PathManagement(Display display, GameField gameField) {

            this.display = display;
            this.gameField = gameField;
            pathManagement = new String[gameField.getAmountOfFields()][gameField.getAmountOfFields()];

            for (int row = 0; row < pathManagement.length; row++) {
                for (int col = 0; col < pathManagement[row].length; col++) {

                    pathManagement[row][col] = "O";
                }
            }

            this.end0 = ImageHelper.getImage("res/Path/End0.png");
            this.end1 = ImageHelper.getImage("res/Path/End1.png");
            this.end2 = ImageHelper.getImage("res/Path/End2.png");
            this.end3 = ImageHelper.getImage("res/Path/End3.png");

            this.pathK = ImageHelper.getImage("res/Path/PathK.png");
            this.pathS = ImageHelper.getImage("res/Path/PathS.png");
            this.pathW = ImageHelper.getImage("res/Path/PathW.png");

            this.curv0 = ImageHelper.getImage("res/Path/Curv0.png");
            this.curv1 = ImageHelper.getImage("res/Path/Curv1.png");
            this.curv2 = ImageHelper.getImage("res/Path/Curv2.png");
            this.curv3 = ImageHelper.getImage("res/Path/Curv3.png");

            this.pathT0 = ImageHelper.getImage("res/Path/PathT0.png");
            this.pathT1 = ImageHelper.getImage("res/Path/PathT1.png");
            this.pathT2 = ImageHelper.getImage("res/Path/PathT2.png");
            this.pathT3 = ImageHelper.getImage("res/Path/PathT3.png");

            this.pathNormal = ImageHelper.getImage("res/Path/PathNormal.png");
        }

        public void addPath(int fieldX, int fieldY) {

            if(pathManagement[fieldX][fieldY].equalsIgnoreCase("O")) {

                pathManagement[fieldX][fieldY] = "X";
                gameField.simulateBuild(fieldX, fieldY, 1, 1);
            } else {

                //THERE IS ALREADY AN PATH
            }
        }

        public void build(int fieldX, int fieldY, int wood, int stone, int wheat, int coins) {

            if(pathManagement[fieldX][fieldY].equalsIgnoreCase("O") && gameField.checkFields(fieldX, fieldY, 1, 1, true,wood, stone, wheat, coins, 0)) {

                addPath(fieldX, fieldY);
                gameField.simulateBuild(fieldX, fieldY, 1, 1);
            }
        }



        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

            for (int x = 0; x < pathManagement.length; x++) {
                for (int y = 0; y < pathManagement[x].length; y++) {

                    if (pathManagement[x][y].equalsIgnoreCase("X")) {

                        if (x != 0 && x != (gameField.getAmountOfFields() - 1) && y != 0 && y != (gameField.getAmountOfFields() - 1)) {

                            BufferedImage image = getImage(pathManagement[x - 1][y], pathManagement[x][y - 1], pathManagement[x + 1][y], pathManagement[x][y + 1]);
                            draw.drawImage(image, gameField.getFieldX() + (x * gameField.getFieldSquareSize()), gameField.getFieldY() + (y * gameField.getFieldSquareSize()), gameField.getFieldSquareSize(), gameField.getFieldSquareSize());
                        } else {

                            String tile0 = "";
                            String tile1 = "";
                            String tile2 = "";
                            String tile3 = "";

                            if(x == 0) tile0 = "O";
                            else tile0 = pathManagement[x - 1][y];
                            if(y == 0) tile1 = "O";
                            else tile1 = pathManagement[x][y - 1];
                            if(x == gameField.getAmountOfFields() - 1) tile2 = "O";
                            else tile2 = pathManagement[x + 1][y];
                            if(y == gameField.getAmountOfFields() - 1) tile3 = "O";
                            else tile3 = pathManagement[x][y + 1];

                            BufferedImage image = getImage(tile0, tile1, tile2, tile3);
                            draw.drawImage(image, gameField.getFieldX() + (x * gameField.getFieldSquareSize()), gameField.getFieldY() + (y * gameField.getFieldSquareSize()), gameField.getFieldSquareSize(), gameField.getFieldSquareSize());
                        }
                    }
                }
            }
        }

        public BufferedImage getImage(String tile0, String tile1, String tile2, String tile3) {

                //End-Of-Path
            if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("O"))
                return end2;
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("O"))
                return end3;
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("O"))
                return end0;
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("X"))
                return end1;

                //Kreuzung
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("X"))
                return pathK;
                //Waagerecht
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("O"))
                return pathW;
                //Senkrecht
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("X"))
                return pathS;

                //Curve
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("X"))
                return curv0;
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("X"))
                return curv1;
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("O"))
                return curv2;
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("O"))
                return curv3;

                //T-Curve
            else if(tile0.equalsIgnoreCase("O") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("X"))
                return pathT0;
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("O") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("X"))
                return pathT1;
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("O") && tile3.equalsIgnoreCase("X"))
                return pathT2;
            else if(tile0.equalsIgnoreCase("X") && tile1.equalsIgnoreCase("X") && tile2.equalsIgnoreCase("X") && tile3.equalsIgnoreCase("O"))
                return pathT3;

            else return pathNormal;
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            for (int x = 0; x < pathManagement.length; x++) {
                for (int y = 0; y < pathManagement[x].length; y++) {

                    if(pathManagement[x][y].equalsIgnoreCase("X") && !move) {

                        display.getActivePanel().removeObjectFromPanel(overlay);
                        if (e.getX() > gameField.getFieldX() + (x * gameField.getFieldSquareSize()) && e.getX() < gameField.getFieldX() + (x * gameField.getFieldSquareSize()) + gameField.getFieldSquareSize() && e.getY() > gameField.getFieldY() + (y * gameField.getFieldSquareSize()) && e.getY() < gameField.getFieldSquareSize() + gameField.getFieldY() + (y * gameField.getFieldSquareSize())) {

                            overlay = new BuildingOverlay(this, gameField.getFieldX() + (x * gameField.getFieldSquareSize()), gameField.getFieldY() + (y * gameField.getFieldSquareSize()), gameField.getFieldSquareSize(), gameField.getFieldSquareSize(), x, y);
                            display.getActivePanel().drawObjectOnPanel(overlay, 40);
                        }
                    }
                }
            }
        }

        public void destroy(int fieldX, int fieldY) {

            pathManagement[fieldX][fieldY] = "O";
            gameField.destroyPath(fieldX + "-" + fieldY, fieldX, fieldY);
        }

        @Override
        public void mouseMoved(MouseEvent event) {

        }
    }
