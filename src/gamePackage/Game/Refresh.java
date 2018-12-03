package gamePackage.Game;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.config.MySQLConfig;
import engine.toolBox.Math;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

    public class Refresh {

                //Attribute
            private Timer timer;

                //Referenzen
            private Player player;
            private DatabaseConnector connector;

        public Refresh(Player player) {

            this.player = player;
            MySQLConfig config = new MySQLConfig();
            connector = new DatabaseConnector(config.getHost(), Integer.parseInt(config.getPort()), config.getDatabase(), config.getUsername(), config.getPassword());

            timer();
        }

        public boolean calculateResources() {

                    connector.executeStatement("" +
                            "SELECT Type, Level FROM JansEmpire_Buildings WHERE Mail = '" + player.getMail() + "';");

            QueryResult production;
            QueryResult result = connector.getCurrentQueryResult();

            int population = 10;
            int woodProduction = 0;
            int stoneProduction = 0;
            int wheatProduction = 0;

            int coinProduction = 0;

            //0 = Type
            //1 = Level
            for (int row = 0; row < result.getRowCount(); row++) {

                if(result.getData()[row][0].equalsIgnoreCase("Woodcutter")) {

                    connector.executeStatement("" +
                            "SELECT WoodProduction FROM JansEmpire_WoodcutterHouseing WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    woodProduction += Integer.parseInt(production.getData()[0][0]);
                }

                else if(result.getData()[row][0].equalsIgnoreCase("Stonemason")) {

                    connector.executeStatement("" +
                            "SELECT StoneProduction FROM JansEmpire_Stonemason WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    stoneProduction += Integer.parseInt(production.getData()[0][0]);
                }

                else if(result.getData()[row][0].equalsIgnoreCase("Farmhouse")) {

                    connector.executeStatement("" +
                            "SELECT WheatProduction FROM JansEmpire_Farmhouse WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    wheatProduction += Integer.parseInt(production.getData()[0][0]);
                }

                else if(result.getData()[row][0].equalsIgnoreCase("Castle")) {

                    connector.executeStatement("" +
                            "SELECT GoldProduction FROM JansEmpire_Castle WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    coinProduction += Integer.parseInt(production.getData()[0][0]);
                }

                else if(result.getData()[row][0].equalsIgnoreCase("Kaserne")) {

                    connector.executeStatement("" +
                            "SELECT NeededGold FROM JansEmpire_Kaserne WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    coinProduction -= Integer.parseInt(production.getData()[0][0]);
                }

                else if(result.getData()[row][0].equalsIgnoreCase("House")) {

                    connector.executeStatement("" +
                            "SELECT Livingroom FROM JansEmpire_House WHERE Level = '" + result.getData()[row][1] + "';");

                    production = connector.getCurrentQueryResult();
                    population += Integer.parseInt(production.getData()[0][0]);
                }
            }

            player.setNeededFood(population * 3);
            player.setWheatProduction(wheatProduction);
            player.setWheatPerHour(wheatProduction - (population * 3));

            player.setWoodPerHour(woodProduction);
            player.setStonePerHour(stoneProduction);

                //Berechnung der Ressourcen
            connector.executeStatement("SELECT LastRefresh FROM JansEmpire_PlayerData WHERE Mail = '" + player.getMail()  + "';");

            String databaseQuarry = connector.getCurrentQueryResult().getData()[0][0];

            try {

                SimpleDateFormat sdfToDate = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                Date lastRefresh = sdfToDate.parse(databaseQuarry);

                boolean refresh  = calculateResources(lastRefresh, woodProduction, stoneProduction, wheatProduction - (population * 3), coinProduction);
                return refresh;
            } catch (Exception e) {

                System.out.println("Datetime - Formatieren Error || Hier ist noch eine Baustelle");
            }
            return false;
        }


        public boolean calculateResources(Date dateTime, int woodProduction, int stoneProduction, int wheatProduction, int coinProduction) {

            long timeDifferenceMilliseconds = new Date().getTime() - dateTime.getTime();
            long diffSeconds = timeDifferenceMilliseconds / 1000;
            long diffDays = timeDifferenceMilliseconds / (60 * 60 * 1000 * 24);

            long coinAmount;
            long woodAmount;
            long stoneAmount;
            long wheatAmount;

                //Max Aktualisierung: Letzte aktualisierung muss mindestens 1 Minute her sein!
            if(diffSeconds >= 60) {

                if(diffDays >= 3) {

                    woodAmount = (long) (woodProduction * 3 * 24);
                    stoneAmount = (long) (stoneProduction * 3 * 24);
                    wheatAmount = (long) (wheatProduction * 3 * 24);
                    coinAmount = (long) (coinProduction * 3 * 24);
                } else {

                    double scale = (double) diffSeconds / (double) 60;
                    System.out.println(scale + " Minuten sind seit der letzten aktualisierung vergangen!");
                    woodAmount = (long) ((double) woodProduction * (scale));
                    stoneAmount = (long) ((double) stoneProduction * (scale));
                    wheatAmount = (long) ((double) wheatProduction * (scale));
                    coinAmount = (long) ((double) coinProduction * (scale));
                }

                player.addGoods((int) woodAmount, (int) stoneAmount, (int) wheatAmount, (int) coinAmount, 0);
                return true;
            } else return false;
        }

        /**
         * In dieser Methode wird mittels einer TimerTask
         *
         * 1.000 = 1s
         * 10.000 = 10s
         * 60.000 = 60s = 1 Min
         */
        private void timer() {

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {

                @Override
                public void run() {

                    synchronizeData();
                    boolean temp = calculateResources();
                    if(temp) updateDatabase();
                }
            }, 0, 10000 * 60);
        }

        /**
         * In dieser Methode werden die Daten des Clients mit den Daten aus der
         * Datenbank verglichen
         */
        public void synchronizeData() {

            connector.executeStatement("" +
                    "SELECT Level, XP, StorageAmount, Wood, Stone, Wheat, Coins, Population FROM JansEmpire_PlayerData WHERE Mail = '" + player.getMail() + "';");

            QueryResult result = connector.getCurrentQueryResult();
            int level = Integer.parseInt(result.getData()[0][0]);
            int xp = Integer.parseInt(result.getData()[0][1]);
            int storageAmount = Integer.parseInt(result.getData()[0][2]);
            int wood = Integer.parseInt(result.getData()[0][3]);
            int stone = Integer.parseInt(result.getData()[0][4]);
            int wheat = Integer.parseInt(result.getData()[0][5]);
            int coins = Integer.parseInt(result.getData()[0][6]);
            int population = Integer.parseInt(result.getData()[0][7]);

            player.updateData(level, xp, storageAmount, wood, stone, wheat, coins, population);
        }

        private void updateDatabase() {

            connector.executeStatement("" +
                    "UPDATE JansEmpire_PlayerData SET " +
                    "LastRefresh = '" + Math.getDateTime() + "' WHERE Mail = '" + player.getMail() + "';");
        }
    }
