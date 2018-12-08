package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.graphics.Display;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import gamePackage.Buildings.*;
import gamePackage.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;

    public class Shop implements LiteInteractableObject {

                //Attribute
            private int shopPage = 0;
            private boolean  active;
            private int shopState = 0;

                //Referenzen
            private Display display;
            private BufferedImage shopGUI;
            private BufferedImage shopGUI2;

            private ArrayList<ShopItem> items;
            private GameController controller;

        public Shop(GameController controller, DatabaseConnector connector, GameField gameField, Player player, Display display) {

            this.display = display;
            this.controller = controller;
            this.shopGUI = ImageHelper.getImage("res/images/Gui/Shop/ShopGUI.png");
            this.shopGUI2 = ImageHelper.getImage("res/images/Gui/Shop/ShopGUI2.png");

            items = new ArrayList<>();
            load(connector, gameField, player);
        }

        public void load(DatabaseConnector connector, GameField gameField, Player player) {

            for(ShopItem item : items) {

                display.getActivePanel().removeObjectFromPanel(item);
                SwingUtilities.invokeLater(() -> items.remove(item));
            }

            ShopItem item;
            item = new ShopItem(this, connector, gameField, player,"images/Gui/Shop/ShopItemWoodcutter", "images/Buildings/Woodcutter/Woodcutter_St1",  "Woodcutter","3x3", 0, 0, 0);
            display.getActivePanel().drawObjectOnPanel(item, 30);
            items.add(item);

            connector.executeStatement("SELECT Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND TYPE = 'Warehouse';");
            if(connector.getCurrentQueryResult().getRowCount() >= 1) {

                item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemWarehouse2",1, 0);
                display.getActivePanel().drawObjectOnPanel(item, 29);
                items.add(item);
            } else {

                item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemWarehouse", "images/Buildings/Warehouse/Warehouse_St1", "Warehouse","5x4", 1, 0, 0);
                display.getActivePanel().drawObjectOnPanel(item, 29);
                items.add(item);
            }

            item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemFarmhouse", "images/Buildings/Farmhouse/Farmhouse_St1", "Farmhouse","4x4", 2, 0, 0);
            display.getActivePanel().drawObjectOnPanel(item, 28);
            items.add(item);

            item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemStonemason", "images/Buildings/Stonemason/Stonemason_St1", "Stonemason","3x3", 3, 0, 0);
            display.getActivePanel().drawObjectOnPanel(item, 27);
            items.add(item);

            item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemHouse", "images/Buildings/House/House_St1", "House","3x4", 4, 0, 0);
            display.getActivePanel().drawObjectOnPanel(item, 26);
            items.add(item);

            connector.executeStatement("SELECT Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND TYPE = 'Kaserne';");
            if(connector.getCurrentQueryResult().getRowCount() >= 1) {

                item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemKaserneMax", 0, 1);
                display.getActivePanel().drawObjectOnPanel(item, 29);
                items.add(item);
            } else {

                item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemKaserne", "images/Buildings/Kaserne/Kaserne_St1", "Kaserne","5x5", 0, 0, 1);
                display.getActivePanel().drawObjectOnPanel(item, 29);
                items.add(item);
            }

            item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ItemShopGuardHouse", "images/Buildings/GuardHouse/GuardHouse_St1", "GuardHouse", "4x4", 1, 0, 1);
            display.getActivePanel().drawObjectOnPanel(item, 28);
            items.add(item);

            item = new ShopItem(this, connector, gameField, player, "images/Gui/Shop/ShopItemStreet", "images/Buildings/PathTiles", "Path", "2x2", 0, 1, 0);
            display.getActivePanel().drawObjectOnPanel(item, 28);
            items.add(item);
        }

        public void reset(ShopItem shopItem) {

            for(ShopItem item : items) {

                if(item != shopItem)
                    item.buildMode = false;
            }
        }

        @Override
        public void draw(DrawHelper draw) {

            if(active) {

                if(shopState == 0) draw.drawImage(shopGUI, 200, 800, 600, 200);
                else if(shopState == 1) draw.drawImage(shopGUI2, 200, 800, 600, 200);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            if(active && !controller.getQuestbook().isActive()) {

                if(e.getX() > 730 && e.getX() < 780 && e.getY() > 810 && e.getY() < 850) {

                    active = false;
                    reset(null);
                    controller.setBuildingmode(false);
                }
            }

            if(e.getX() > 215 && e.getX() < 265 && e.getY() > 810 && e.getY() < 840 && active) shopState = 0;
            else if(e.getX() > 315 && e.getX() < 365 && e.getY() > 810 && e.getY() < 840 && active)  shopState = 1;

            if(e.getX() > 740 && e.getX() < 760 && e.getY() > 905 && e.getY() < 940 && active) {

                shopPage++;
                for (ShopItem item : items)
                    item.setShopPage(shopPage);
            }
            else if(e.getX() > 235 && e.getX() < 250 && e.getY() > 900 && e.getY() < 940 && active) {

                shopPage--;
                for (ShopItem item : items)
                    item.setShopPage(shopPage);
            }
        }

        @Override
        public void update(double delta) {

        }

        @Override
        public void mouseMoved(MouseEvent event) {

        }

            //--------------- GETTER AND SETTER ---------------\\

        public boolean isActive() {

            return active;
        }

        public void setActive(boolean active) {

            this.active = active;
        }

        public class ShopItem implements LiteInteractableObject {

                    //Attribute
                private int index;
                private int x = 268;
                private int y = 862;
                private boolean inActive;

                private int page;
                private int state;
                private int worker;
                private int storage;
                private int coinCost;
                private int woodCost;
                private int shopPage;
                private int stoneCost;
                private int wheatCost;
                private int livingroom;

                private String type;
                private String[] size;

                private int mouseX;
                private int mouseY;
                private boolean buildMode;
                private boolean showPrices;

                    //Referenzen
                private Shop shop;
                private Player player;
                private GameField gameField;
                private DatabaseConnector connector;

                private BufferedImage item;
                private BufferedImage building;
                private BufferedImage priceItem;

            public ShopItem(Shop shop, DatabaseConnector connector, GameField gameField, Player player, String shopPath, int index, int state) {

                this.shop = shop;
                this.index = index;
                this.state = state;
                this.player = player;
                this.inActive = true;
                this.gameField = gameField;
                this.connector = connector;
                item = ImageHelper.getImage("res/" + shopPath + ".png");
            }

            public ShopItem(Shop shop, DatabaseConnector connector, GameField gameField, Player player, String shopPath, String buildingPath, String type, String size, int index, int page, int state) {

                this.page = page;
                this.shop = shop;
                this.type = type;
                this.state = state;
                this.index = index;
                this.player = player;
                this.inActive = false;
                this.gameField = gameField;
                this.connector = connector;
                this.size = size.split("x");
                item = ImageHelper.getImage("res/" + shopPath + ".png");
                building = ImageHelper.getImage("res/" + buildingPath + ".png");
                priceItem = ImageHelper.getImage("res/images/Gui/Shop/PriceItem.png");

                connector.executeStatement("SELECT WoodCost, StoneCost, WheatCost, CoinCost, WorkerAmount, Livingroom, StorageAmount FROM JansEmpire_StaticBuildings WHERE Level = '1' AND Type = '" + type + "';");
                QueryResult result = connector.getCurrentQueryResult();

                woodCost = Integer.parseInt(result.getData()[0][0]);
                coinCost = Integer.parseInt(result.getData()[0][3]);
                stoneCost = Integer.parseInt(result.getData()[0][1]);
                wheatCost = Integer.parseInt(result.getData()[0][2]);
                worker = Integer.parseInt(result.getData()[0][4]);
                livingroom = Integer.parseInt(result.getData()[0][5]);
                storage = Integer.parseInt(result.getData()[0][6]);
            }

            @Override
            public void draw(DrawHelper draw) {

                if(state == shopState && page == shopPage) {

                    if (active) {
                        draw.drawImage(item, x + (88 * index), y, 93, 128);

                        draw.setColour(Color.BLACK);
                        final DecimalFormat separator = new java.text.DecimalFormat("##,###");
                        if(!inActive) draw.drawString(separator.format(stoneCost) + "", x + (88 * index) + 43, y + 101);
                        if(!inActive) draw.drawString(separator.format(woodCost) + "", x + (88 * index) + 43, y + 118);
                    }

                    if (showPrices && active) {

                        draw.drawImage(priceItem, x + (88 * index) + 40, y + 40, 96 * 1.3, 73 * 1.3);

                        draw.setColour(Color.BLACK);
                        final DecimalFormat separator = new java.text.DecimalFormat("##,###");
                        draw.drawString(separator.format(coinCost), x + (88 * index) + 85, y + 58);
                        draw.drawString(separator.format(stoneCost), x + (88 * index) + 85, y + 74);
                        draw.drawString(separator.format(woodCost), x + (88 * index) + 85, y + 90);
                        draw.drawString(separator.format(worker), x + (88 * index) + 85, y + 106);
                        draw.drawString(separator.format(wheatCost), x + (88 * index) + 85, y + 122);
                    }

                    if (buildMode) {

                        draw.drawImage(building, mouseX, mouseY, Integer.parseInt(size[0]) * 25, Integer.parseInt(size[1]) * 25);
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

                if(state == shopState) {

                    if (e.getX() > x + (88 * index) && e.getX() < x + (88 * (index + 1)) && e.getY() > y && e.getY() < y + 128 && !inActive)
                        showPrices = true;
                    else showPrices = false;

                    if (buildMode) {

                        mouseX = e.getX();
                        mouseY = e.getY();
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(shopState == state && !inActive && !gameField.getController().getQuestbook().isActive() && active && page == shopPage) {

                    if (e.getX() > x + (88 * index) && e.getX() < x + (88 * (index + 1)) && e.getY() > y && e.getY() < y + 128 && !buildMode) {

                        buildMode = true;
                        reset(this);
                    }

                    if (buildMode) {

                        if (!gameField.getField(e.getX(), e.getY()).equalsIgnoreCase("error")) {

                            String[] field = gameField.getField(e.getX(), e.getY()).split(":");

                            if (gameField.checkFields(Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), true, woodCost, stoneCost, wheatCost, coinCost, worker)) {

                                switch (type) {

                                    case "Woodcutter":
                                        Woodcutter woodcutter = new Woodcutter(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(woodcutter, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "Warehouse":
                                        Warehouse warehouse = new Warehouse(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(warehouse, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        player.addStorage(storage);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "Farmhouse":
                                        Farmhouse farmhouse = new Farmhouse(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(farmhouse, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "Stonemason":
                                        Stonemason stonemason = new Stonemason(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(stonemason, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "House":
                                        House house = new House(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(house, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        player.deposit(0, 0, 0, 0, livingroom);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "Kaserne":
                                        Kaserne kaserne = new Kaserne(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(kaserne, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        controller.setBuildingmode(false);
                                        break;
                                    case "GuardHouse":
                                        GuardHouse guardHouse = new GuardHouse(connector, display, gameField, 1, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]));
                                        gameField.build(guardHouse, Integer.parseInt(field[0]), Integer.parseInt(field[1]), Integer.parseInt(size[0]), Integer.parseInt(size[1]), woodCost, stoneCost, wheatCost, coinCost, worker);
                                        controller.setBuildingmode(false);
                                        break;
                                }

                                active = false;
                                connector.executeStatement("" +
                                        "INSERT INTO JansEmpire_Buildings (" +
                                        "" +
                                        "Mail, Type, Level, Position) " +
                                        "" +
                                        "VALUES (" +
                                        "" +
                                        "'" + player.getMail() + "', '" + type + "', '1', '" + field[0] + "-" + field[1] + "');");
                            }
                            active = false;
                            buildMode = false;
                            controller.setBuildingmode(false);
                            gameField.getController().refreshQuests();

                            gameField.refresh();
                            shop.load(connector, gameField, player);
                        }
                    }
                }

            }

            @Override
            public void update(double delta) {

            }

            public void setShopPage(int shopPage) {

                this.shopPage = shopPage;
            }
        }
    }
