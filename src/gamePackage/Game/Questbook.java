package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.graphics.Display;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;
import gamePackage.GameController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

    public class Questbook implements LiteInteractableObject {

                //Attribute
            private boolean active;

                //Referenzen
            private Player player;
            private Display display;
            private ArrayList<Quest> quests;
            private GameController controller;
            private DatabaseConnector connector;

            private BufferedImage questbookIcon;
            private BufferedImage emptyQuestbookMenu;

        public Questbook(Display display, DatabaseConnector connector, Player player, GameController controller) {

            this.player = player;
            this.display = display;
            this.connector = connector;
            this.controller = controller;
            this.quests = new ArrayList();

            this.questbookIcon = ImageHelper.getImage("res/images/Questbook/QuestbookIcon.png");
            this.emptyQuestbookMenu = ImageHelper.getImage("res/images/Questbook/EmptyQuestbook.png");

            loadQuests();
        }

        private void loadQuests() {

            connector.executeStatement("" +
                    "SELECT * FROM JansEmpire_QuestList INNER JOIN JansEmpire_UserQuestList ON JansEmpire_UserQuestList.QuestID = JansEmpire_QuestList.QuestID INNER JOIN JansEmpire_Tasks ON JansEmpire_Tasks.TaskID = JansEmpire_QuestList.TaskID WHERE Mail = '" + player.getMail() +"' AND Done = 'false';");
            QueryResult result = connector.getCurrentQueryResult();

            for (int i = 0; i < 6; i++) {

                if(result.getRowCount() > i) {

                    Quest quest = new Quest(this, Integer.parseInt(result.getData()[i][0]), i, Integer.parseInt(result.getData()[i][3]), Integer.parseInt(result.getData()[i][4]), Integer.parseInt(result.getData()[i][5]), result.getData()[i][12], result.getData()[i][11], result.getData()[i][1]);
                    display.getActivePanel().drawObjectOnPanel(quest, 101);
                    quests.add(quest);
                }
            }
        }

        public void refresh() {

            for (Quest quest : quests) {

                quest.refresh();
            }
        }

        private void removeQuests() {

            for (Quest quest : quests) {

                display.getActivePanel().removeObjectFromPanel(quest);
                SwingUtilities.invokeLater(() -> quests.remove(quest));
            }
        }

        @Override
        public void draw(DrawHelper draw) {

            draw.drawImage(questbookIcon, 930, 740, 75, 75);

            if(active){

                draw.drawImage(emptyQuestbookMenu, 250, 200, 500, 650);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            if(e.getX() > 930 && e.getX() < 1005 && e.getY() > 740 && e.getY() < 815 && !controller.getGui().getShop().isActive()) {

                active = true;

                for (Quest quest : quests)
                    quest.setActive(true);
            }

            if(e.getX() > 680 && e.getX() < 743 && e.getY() > 207 && e.getY() < 270 && active) {

                close();
            }
        }

        public void close() {

            active = false;

            for (Quest quest : quests) {

                quest.setActive(false);
                quest.setActiveQuest(false);
            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {

        }

        @Override
        public void update(double delta) {

        }

            //GETTER AND SETTER
        public boolean isActive() {

            return active;
        }

        public void setActiveQuest(int index) {

            for (int i = 0; i < quests.size(); i++) {

                if(i == index) quests.get(i).setActiveQuest(true);
                else  quests.get(i).setActive(false);
            }
        }

        public class Quest implements LiteInteractableObject {

                    //Attribute
                private int questIndex;
                private boolean active;
                private int databaseIndex;
                private boolean activeQuest;

                private int level;
                private int amount;
                private int rewardID;
                private String[] rewards;
                private int amountReady;
                private String type;
                private String object;


                    //Referenzen
                private String name;
                private Questbook questbook;

                private BufferedImage finish;
                private BufferedImage questbar;
                private BufferedImage donateTemplate;
                private BufferedImage questTemplate;

            public Quest(Questbook questbook, int databaseIndex, int questIndex, int amount, int level, int rewardID, String object, String type, String name) {

                this.name = name;
                this.type = type;
                this.level = level;
                this.amount = amount;
                this.object = object;
                this.rewardID = rewardID;
                this.activeQuest = false;
                this.questbook = questbook;
                this.questIndex = questIndex;
                this.rewards = new String[3];
                this.databaseIndex = databaseIndex;

                this.finish = ImageHelper.getImage("res/images/Questbook/finish.png");
                this.questbar = ImageHelper.getImage("res/images/Questbook/Questbar.png");
                this.donateTemplate = ImageHelper.getImage("res/images/Questbook/donateBar.png");
                this.questTemplate = ImageHelper.getImage("res/images/Questbook/QuestTemplate.png");

                generateRewards();
                refresh();
            }

            @Override
            public void draw(DrawHelper draw) {

                if(active && !activeQuest) {

                    draw.drawImage(questbar, 273, 369 + (questIndex * 65), 454, 65);

                    draw.setColour(Color.BLACK);
                    draw.setFont(new Font("Roboto", Font.PLAIN, 20));
                    draw.drawString(name, 365, 410 + (questIndex * 65));
                }

                if(active && activeQuest) {

                    draw.drawImage(questTemplate, 273, 270, 454, 561);

                    if(type.equalsIgnoreCase("Donate") && amountReady < amount) draw.drawImage(donateTemplate, 273, 485, 454, 68);

                    draw.setColour(new Color(0,102,0));
                    double questProgress = (double) amountReady / (double) amount;
                    questProgress = (int) (430 * questProgress);
                    if(questProgress > 430) questProgress = 430;
                    draw.fillRec(285, 521, (int) questProgress, 21);
                    draw.setColour(Color.BLACK);

                    for (int i = 0; i < rewards.length; i++) {

                        if(rewards[i] != null) {

                            String[] temp = rewards[i].split(":");
                            draw.drawImage(getImage(temp[0]), 313 + (140 * i), 738, 94, 82);
                            draw.drawString(temp[1], 313 + (140 * i) + (94 - draw.getFontWidth(temp[1])) / 2, 815);
                        }
                    }

                    draw.setColour(Color.BLACK);
                    draw.setFont(new Font("Roboto", Font.PLAIN, 30));
                    draw.drawString(name, 273 + ((450 - draw.getFontWidth(name)) / 2), 315);

                    draw.setColour(Color.BLACK);
                    draw.setFont(new Font("Roboto", Font.PLAIN, 12));
                    draw.drawString("Hier entsteht eine Questbeschreibung, mit Zeilenumbruch!", 340, 360);

                    draw.setColour(Color.BLACK);
                    draw.setFont(new Font("Roboto", Font.PLAIN, 20));
                    if(type.equalsIgnoreCase("Build")) {

                        switch(object) {

                            case "Woodcutter":

                                if(amount == 1) draw.drawString("Bau ein Holzfäller Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue " + amount + " Holzfäller auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade " + amount + " Holzfäller auf Stufe " + level + ":", 280, 508);
                                break;
                            case "Stonemason":

                                if(amount == 1) draw.drawString("Baue ein Steinmetz Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue " + amount + " Steinmetze auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade " + amount + " Steinmetze auf Stufe " + level + ":", 280, 508);
                                break;
                            case "Farmhouse":

                                if(amount == 1) draw.drawString("Bau ein Farmhaus Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue " + amount + " Farnhäuser auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade " + amount + " Farnhäuser auf Stufe " + level + ":", 280, 508);
                                break;
                            case "Warehouse":

                                if(amount == 1) draw.drawString("Bau Lagerhaus Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue Lagerhaus auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade Lagerhaus auf Stufe " + level + ":", 280, 508);
                                break;
                            case "House":

                                if(amount == 1) draw.drawString("Bau " + amount + " Wohnhaus Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue " + amount + " Wohnhäuser auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade " + amount + " Wohnhäuser auf Stufe " + level + ":", 280, 508);
                                break;
                            case "Kaserne":

                                if(amount == 1) draw.drawString("Bau Kaserne Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue Kaserne auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade Kaserne auf Stufe " + level + ":", 280, 508);
                                break;
                            case "GuardHouse":

                                if(amount == 1) draw.drawString("Bau " + amount + " Wachhaus Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level == 1) draw.drawString("Baue " + amount + " Wachhäuser auf Stufe " + level + ":", 280, 508);
                                else if(amount > 1 && level != 1) draw.drawString("Upgrade " + amount + " Wachhäuser auf Stufe " + level + ":", 280, 508);
                                break;
                            case "Castle":

                                draw.drawString("Upgrade Burg auf Stufe " + level + ":", 280, 508);
                                break;
                        }
                    }

                    else if(type.equalsIgnoreCase("Collect")) {

                        switch (object) {

                            case "Coins":
                                draw.drawString("Sammel " + amount + " Münzen:", 280, 508);
                                break;
                        }
                    }

                    else if(type.equalsIgnoreCase("Donate")) {

                        switch (object) {

                            case "Wood":
                                draw.drawString("Spende " + amount + " Holz:", 280, 508);
                                break;
                            case "Stone":
                                draw.drawString("Spende " + amount + " Stein:", 280, 508);
                                break;
                            case "Wheat":
                                draw.drawString("Spende " + amount + " Weizen:", 280, 508);
                                break;
                            case "Coins":
                                draw.drawString("Spende " + amount + " Münzen:", 280, 508);
                                break;
                        }
                    }
                    draw.drawString(amountReady + "/" + amount, 273 + ((450 - draw.getFontWidth(amountReady + "/" + amount)) / 2), 538);
                    if(amountReady >= amount) draw.drawImage(finish, 700, 800, 60, 99);
                }
            }

            public void refresh() {

                amountReady = 0;

                if(type.equalsIgnoreCase("Build")) {

                    connector.executeStatement("" +
                            "SELECT Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "' AND Type = '" + object + "'");
                    QueryResult result = connector.getCurrentQueryResult();


                    for (int i = 0; i < result.getRowCount(); i++) {

                        if(Integer.parseInt(result.getData()[i][0]) >= level) amountReady += 1;
                    }
                }
            }

            public void generateRewards() {

                connector.executeStatement("" +
                        "SELECT xpReward, coinReward, woodReward, stoneReward, wheatReward FROM JansEmpire_QuestRewards WHERE RewardID = '" + rewardID + "'");

                ArrayList<String> list = new ArrayList<>();
                QueryResult result = connector.getCurrentQueryResult();

                for (int col = 0; col < result.getColumnCount(); col++) {

                    if(Integer.parseInt(result.getData()[0][col]) > 0) {

                        if(col == 0) {

                            list.add("xpIcon:" + result.getData()[0][col]);
                        } else if(col == 1) {

                            list.add("coinIcon:" + result.getData()[0][col]);
                        } else if(col == 2) {

                            list.add("woodIcon:" + result.getData()[0][col]);
                        } else if(col == 3) {

                            list.add("stoneIcon:" + result.getData()[0][col]);
                        } else if(col == 4) {

                            list.add("wheatIcon:" + result.getData()[0][col]);
                        }
                    }
                }

                for (int i = 0; i < list.size(); i++) {

                    if(i <= (rewards.length - 1)) {

                        rewards[i] = list.get(i);
                    }
                }
            }

            public BufferedImage getImage(String path) {

                return ImageHelper.getImage("res/images/Questbook/" + path + ".png");
            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(e.getX() >  273 && e.getX() < 727 && e.getY() > 369 + (questIndex * 65) && e.getY() < 424 + (questIndex * 65) && active) {

                    questbook.setActiveQuest(questIndex);
                }

                if(e.getX() >  700 && e.getX() < 760 && e.getY() > 800 && e.getY() < 899 && activeQuest && amountReady >= amount) {

                    if(type.equalsIgnoreCase("Build")) {

                        int xp = 0;
                        int wood = 0;
                        int coin = 0;
                        int stone = 0;
                        int wheat = 0;
                        int worker = 0;

                        for (int i = 0; i < rewards.length; i++) {

                            if(rewards[i] != null) {

                                String[] temp = rewards[i].split(":");

                                if(temp[0].contains("wood")) {

                                    wood = Integer.parseInt(temp[1]);
                                } else if(temp[0].contains("stone")) {

                                    stone = Integer.parseInt(temp[1]);
                                }  else if(temp[0].contains("wheat")) {

                                    wheat = Integer.parseInt(temp[1]);
                                }  else if(temp[0].contains("coin")) {

                                    coin = Integer.parseInt(temp[1]);
                                } else if(temp[0].contains("xp")) {

                                    xp = Integer.parseInt(temp[1]);
                                }
                            }
                        }
                        player.addXP(xp);
                        player.addGoods(wood, stone, wheat, coin, worker);
                        close();

                        connector.executeStatement("" +
                                "UPDATE JansEmpire_UserQuestList SET Done = 'true' WHERE Mail = '" + player.getMail() + "' AND QuestID = '" + databaseIndex + "';");
                        removeQuests();
                        loadQuests();
                    } else if(type.equalsIgnoreCase("Donate")) {

                        close();

                        removeQuests();
                        loadQuests();
                    }
                }

                if(e.getX() >  665 && e.getX() < 720 && e.getY() > 510 && e.getY() < 550 && activeQuest && type.equalsIgnoreCase("Donate")) {

                    if(object.equalsIgnoreCase("Coins")) {

                        if(player.checkGoods(0, 0, 0, amount, 0)) {

                            player.payGoods(0, 0, 0, amount, 0);
                            amountReady = amount;

                            connector.executeStatement("" +
                                    "UPDATE JansEmpire_UserQuestList SET Done = 'true' WHERE Mail = '" + player.getMail() + "' AND QuestID = '" + databaseIndex + "';");
                        }
                    } else if(object.equalsIgnoreCase("Wheat")) {

                        if(player.checkGoods(0, 0, amount, 0, 0)) {

                            player.payGoods(0, 0, amount, 0, 0);
                            amountReady = amount;

                            connector.executeStatement("" +
                                    "UPDATE JansEmpire_UserQuestList SET Done = 'true' WHERE Mail = '" + player.getMail() + "' AND QuestID = '" + databaseIndex + "';");
                        }
                    } else if(object.equalsIgnoreCase("Wood")) {

                        if(player.checkGoods(amount, 0, 0, 0, 0)) {

                            player.payGoods(amount, 0, 0, 0, 0);
                            amountReady = amount;

                            connector.executeStatement("" +
                                    "UPDATE JansEmpire_UserQuestList SET Done = 'true' WHERE Mail = '" + player.getMail() + "' AND QuestID = '" + databaseIndex + "';");
                        }
                    } else if(object.equalsIgnoreCase("Stone")) {

                        if(player.checkGoods(0, amount, 0, 0, 0)) {

                            player.payGoods(0, amount, 0, 0, 0);
                            amountReady = amount;

                            connector.executeStatement("" +
                                    "UPDATE JansEmpire_UserQuestList SET Done = 'true' WHERE Mail = '" + player.getMail() + "' AND QuestID = '" + databaseIndex + "';");
                        }
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {

            }

            @Override
            public void update(double delta) {

            }

            public void setActive(boolean active) {

                this.active = active;
            }

            public void setActiveQuest(boolean active) {

                this.activeQuest = active;
            }
        }
    }


