package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.graphics.Display;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import gamePackage.GameController;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

    public class GUI implements LiteInteractableObject {

                //Attribute
            private boolean hoverWood;
            private boolean hoverStone;
            private boolean hoverWheat;
            private boolean hoverMilitary;

            private BufferedImage woodOverlay;
            private BufferedImage stoneOverlay;
            private BufferedImage wheatOverlay;
            private BufferedImage militaryOverlay;

                //Referenzen
            private Shop shop;
            private Player player;
            private GameController controller;

            private BufferedImage gui;
            private BufferedImage taxGUI;
            private BufferedImage menuGUI;
            private BufferedImage resourceBar;

        public GUI(Display display, DatabaseConnector connector, GameField gameField, Player player, GameController controller) {

            this.player = player;
            this.controller = controller;

            this.shop = new Shop(controller, connector, gameField, player, display);
            display.getActivePanel().drawObjectOnPanel(shop, 4);

            this.gui = ImageHelper.getImage("res/images/Gui/GUI.png");
            this.taxGUI = ImageHelper.getImage("res/images/Gui/TaxGUI.png");
            this.menuGUI = ImageHelper.getImage("res/images/Gui/MenuGUI.png");
            this.resourceBar = ImageHelper.getImage("res/images/Gui/ResourceBar.png");

            this.woodOverlay = ImageHelper.getImage("res/images/Gui/wood.png");
            this.stoneOverlay = ImageHelper.getImage("res/images/Gui/stone.png");
            this.wheatOverlay = ImageHelper.getImage("res/images/Gui/wheat.png");
            this.militaryOverlay = ImageHelper.getImage("res/images/Gui/military.png");
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void draw(DrawHelper draw) {

                //Menu
            draw.drawImage(menuGUI, 842, 875, 176.8, 120.9);
            draw.drawImage(taxGUI, 810, 850, 121 * 1.3, 41 * 1.3);
            draw.drawImage(resourceBar, 240, 0, 418 * 1.3, 36 * 1.3);

            draw.setColour(Color.RED);

            double scale1 = (double) player.getWood() / (double) player.getStorageAmount();
            double scale2 = (double) player.getStone() / (double) player.getStorageAmount();
            double scale3 = (double) player.getWheat() / (double) player.getStorageAmount();

            if(scale1 > 1) scale1 = 1;
            if(scale2 > 1) scale2 = 1;
            if(scale3 > 1) scale3 = 1;

            draw.fillRec(328, 18, (int) (45 * scale1), 5);
            draw.fillRec(418, 18, (int) (45 * scale2), 5);
            draw.fillRec(508, 18, (int) (45 * scale3), 5);

            draw.setColour(Color.BLACK);
            draw.setFont(new Font("Roboto", Font.PLAIN, 12));
            final DecimalFormat separator = new java.text.DecimalFormat("##,###");

            draw.drawString(separator.format(player.getWood()), 332, 35);
            draw.drawString(separator.format(player.getStone()), 421, 35);
            draw.drawString(separator.format(player.getWheat()), 511, 35);

            draw.drawString(separator.format(player.getPopulation()), 608, 35);
            draw.drawString("0", 695, 35);

            draw.drawImage(gui, 0, 5, 134 * 1.3, 135 * 1.3);
            draw.setFont(new Font("Roboto", Font.BOLD, 20));
            if(player.getLevel() >= 10) draw.drawString(player.getLevel() + "", 112, 68);
            else draw.drawString(player.getLevel() + "", 118, 68);

            draw.setFont(new Font("Roboto", Font.BOLD, 14));
            draw.drawString(player.getUsername(), 4 + (145 - draw.getFontWidth(player.getUsername())) / 2, 115);
            draw.setFont(new Font("Roboto", Font.PLAIN, 14));
            draw.drawString(separator.format(player.getCoins()), 40, 158);

            draw.setColour(new Color(36, 45, 51));
            draw.setFont(new Font("Roboto", Font.BOLD, 14));

            int currentProgress = player.getCurrentProgress();
            double progress = player.getNextLevelXP();
            if(progress != -1) {

                progress = (double) (this.player.getXp() - currentProgress) /  (progress - currentProgress);

            } else progress = 1;
            draw.fillRec(10, 80, (int) (128 * progress), 17);
            draw.setColour(Color.WHITE);
            draw.drawString(separator.format(player.getXp()), 47, 93);

            if(hoverWood) {

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 12));
                draw.drawImage(woodOverlay, 290, 46, 150 , 80);
                draw.drawString(separator.format(player.getWoodPerHour()), 302, 95);
                draw.drawString(separator.format(player.getStorageAmount()), 401, 114);
            }

            else if(hoverStone) {

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 12));
                draw.drawImage(stoneOverlay, 335, 46, 150 , 80);
                draw.drawString(separator.format(player.getStonePerHour()), 348, 95);
                draw.drawString(separator.format(player.getStorageAmount()), 446, 114);
            }

            else if(hoverWheat) {

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 12));
                draw.drawImage(wheatOverlay, 425, 46, 150 , 150);

                draw.drawString(separator.format(player.getFoodProduction()), 435, 108);
                draw.drawString(separator.format(player.getNeededFood()), 435, 128);

                if(player.getWheatPerHour() >= 0) draw.setColour(Color.BLACK);
                else draw.setColour(Color.RED);

                draw.drawString(separator.format(player.getWheatPerHour()), 430, 158);
                draw.setColour(Color.BLACK);
                draw.drawString(separator.format(player.getStorageAmount()), 515, 178);
            }

            else if(hoverMilitary) {

                draw.setColour(Color.BLACK);
                draw.setFont(new Font("Roboto", Font.PLAIN, 12));
                draw.drawImage(militaryOverlay, 535, 46, 200 , 220);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

                //Shop button
            if(e.getX() > 885 && e.getX() < 950 && e.getY() > 935 && e.getY() < 995) {

                shop.setActive(true);
                controller.setBuildingmode(true);
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

            if(isInside(e, 290, 12, 88,30)) hoverWood = true; else  hoverWood = false;
            if(isInside(e, 380, 12, 88, 30)) hoverStone = true; else hoverStone = false;
            if(isInside(e, 479, 12, 88, 30)) hoverWheat = true; else hoverWheat = false;
            if(isInside(e, 650, 12, 88, 30)) hoverMilitary = true; else hoverMilitary = false;
        }

        private boolean isInside(MouseEvent e, int x, int y, int width, int height) {

            if(e.getX() > x && e.getX() < x + width && e.getY() > y && e.getY() < y + height) return true; return false;
        }

        public Shop getShop() {

            return shop;
        }
    }
