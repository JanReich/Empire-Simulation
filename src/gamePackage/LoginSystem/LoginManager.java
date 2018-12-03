package gamePackage.LoginSystem;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.cryptography.MD5;
import engine.graphics.Display;
import gamePackage.JFrameController;

    public class LoginManager {

                //Attribute

                //Referenzen
            private FormLayout layout;
            private JFrameController controller;

            private DatabaseConnector connector;

        public LoginManager(Display display, DatabaseConnector connector, JFrameController controller) {

            this.connector = connector;
            this.controller = controller;

            this.layout = new FormLayout(display, this);
            display.getActivePanel().drawObjectOnPanel(layout);
        }

        public String login(String loginName, String password) {


                //Login with Mail
            if(loginName.contains("@")) {

                connector.executeStatement("" +
                        "SELECT Password, Mail FROM JansEmpire_Users WHERE Mail = '" + loginName + "'");
            } else {

                connector.executeStatement("" +
                        "SELECT Password, Mail FROM JansEmpire_Users WHERE Username = '" + loginName + "'");
            }

            if(connector.getCurrentQueryResult().getRowCount() >= 1) {

                if(connector.getCurrentQueryResult().getData()[0][0] != null) {

                    String md5Password = MD5.MD5(password);
                    if(md5Password.equals(connector.getCurrentQueryResult().getData()[0][0])) {

                        String mail = connector.getCurrentQueryResult().getData()[0][1];
                        controller.startGame(mail);
                        return "successful";
                    } else {

                        return "failed";
                    }
                }
            }
            return "failed";
        }

        public String register(String username, String password, String mail) {

            if(username != null && password != null && mail != null && mail.contains("@") && username != mail && mail.length() >= 5 && username.length() >= 3) {

                    //Falls die Tabelle noch nicht existiert wird sie vorher erstellt!
                connector.executeStatement("" +
                        "CREATE TABLE IF NOT EXISTS JansEmpire_Users ( " +
                        "Mail VARCHAR(50) NOT NULL PRIMARY KEY , " +
                        "Username VARCHAR(20) NOT NULL ," +
                        "Password VARCHAR(50) NOT NULL );");

                connector.executeStatement("" +
                        "CREATE TABLE JansEmpire_PlayerData IF NOT EXISTS( " +
                        "Mail VARCHAR(50) NOT NULL PRIMARY KEY, " +
                        "Level INT(2) NOT NULL , " +
                        "XP INT(7) NOT NULL , " +
                        "Wood INT(5) NOT NULL , " +
                        "Stone INT(5) NOT NULL , " +
                        "Wheat INT(5) NOT NULL , " +
                        "Gold INT(7) NOT NULL , " +
                        "Population INT(4) NOT NULL );");

                connector.executeStatement("" +
                        "SELECT * FROM JansEmpire_Users WHERE Mail = '" + mail + "' OR Username = '" + username + "'");


                    //Mail und Username existieren noch nicht in der Tabelle
                if(connector.getCurrentQueryResult().getRowCount() == 0) {

                        //MD5-Verschlüsselung
                    String md5Password = MD5.MD5(password);

                    connector.executeStatement("" +
                            "INSERT INTO JansEmpire_Users " +
                            "(Mail, Username, Password) " +
                            "" +
                            "VALUES " +
                            "" +
                            "('" + mail + "', '" + username + "', '" + md5Password + "')");


                    connector.executeStatement("" +
                            "SELECT StorageAmount From JansEmpire_Castle WHERE Level = '1'");

                    int startStorage = Integer.parseInt(connector.getCurrentQueryResult().getData()[0][0]);

                    connector.executeStatement("" +
                            "SELECT QuestID FROM JansEmpire_QuestList");
                    QueryResult result = connector.getCurrentQueryResult();

                    for (int i = 0; i < result.getRowCount(); i++) {

                        connector.executeStatement("" +
                                "INSERT INTO JansEmpire_UserQuestList " +
                                "(Mail, QuestID, Done) " +
                                "" +
                                "VALUES " +
                                "" +
                                "('" + mail + "', '" + result.getData()[i][0] + "', 'false');");
                    }

                    connector.executeStatement("" +
                            "INSERT INTO JansEmpire_PlayerData " +
                            "(Mail, Level, XP, StorageAmount, Wood, Stone, Wheat, Coins, Population) " +
                            "" +
                            "VALUES " +
                            "" +
                            "('" + mail + "', '1', '50', '" + startStorage + "', '" + (startStorage / 2 )+ "', '" + (startStorage / 2) + "', '" + startStorage + "', '" + startStorage + "', '10');");

                    connector.executeStatement("" +
                            "INSERT INTO JansEmpire_Buildings " +
                            "(Mail, Type, Level, Position) " +
                            "" +
                            "VALUES " +
                            "" +
                            "('" + mail + "', 'Castle', '1', '0-0');");

                    return "successful";
                } else return "failed";
            } else return "failed";

        }
    }
