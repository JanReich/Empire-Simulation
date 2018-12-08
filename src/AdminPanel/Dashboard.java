package AdminPanel;

import engine.abitur.datenbanken.mysql.DatabaseConnector;
import engine.abitur.datenbanken.mysql.QueryResult;
import engine.graphics.Display;
import engine.graphics.interfaces.LiteInteractableObject;
import engine.toolBox.AnimatedButton;
import engine.toolBox.DrawHelper;
import engine.toolBox.ImageHelper;

import java.awt.*;
import java.awt.event.MouseEvent;

    public class Dashboard implements LiteInteractableObject {

                //Attribute

                //Referenzen
            private QueryResult users;
            private AnimatedButton refresh;
            private DatabaseConnector connector;

        public Dashboard(DatabaseConnector connector, Display display) {

            this.connector = connector;
            this.refresh = new AnimatedButton(900, 900, 60, 60, "res/refresh.png");
            display.getActivePanel().drawObjectOnPanel(refresh);
            display.getActivePanel().drawObjectOnPanel(refresh.getAnimation());

            refresh();
        }

        public void refresh() {

            connector.executeStatement("" +
                    "SELECT JansEmpire_Users.Mail, Username, Level, XP, LastRefresh FROM JansEmpire_Users INNER JOIN JansEmpire_PlayerData ON JansEmpire_Users.Mail = JansEmpire_PlayerData.Mail");

            users = connector.getCurrentQueryResult();
        }

        @Override
        public void draw(DrawHelper draw) {

            draw.drawLine(100, 150, 900, 150);
            draw.drawLine(100, 200, 900, 200);

            draw.setFont(new Font("Roboto", Font.BOLD, 18));
            draw.drawString("Mail-Addresse", 100 + (160 - draw.getFontWidth("Mail-Addresse")) / 2, 190);
            draw.drawString("Username", 260 + (160 - draw.getFontWidth("Username")) / 2, 190);
            draw.drawString("Level", 420 + (100 - draw.getFontWidth("Level")) / 2, 190);
            draw.drawString("Erfahrungspunkte (XP)", 520 + (220 - draw.getFontWidth("Erfahrungspunkte (XP)")) / 2, 190);
            draw.drawString("Last Login", 740 + (160 - draw.getFontWidth("Last Login")) / 2, 190);

            draw.setFont(new Font("Roboto", Font.PLAIN, 14));
            for (int i = 0; i < users.getColumnCount() + 1; i++) {

                if(i == 3) {

                    draw.drawLine(100 + 160 * (i - 1) + 100, 150, 100 + 160 * (i - 1) + 100, 200 + 50 * users.getRowCount());
                } else draw.drawLine(100 + 160 * i, 150, 100 + 160 * i, 200 + 50 * users.getRowCount());
            }

            for (int i = 0; i < users.getRowCount(); i++) {

                draw.drawLine(100, 250 + 50 * i, 900, 250 + 50 * i);

                draw.drawString(users.getData()[i][0], 100 + (160 - draw.getFontWidth(users.getData()[i][0])) / 2, 240 + 50 * i);
                draw.drawString(users.getData()[i][1], 250 + (160 - draw.getFontWidth(users.getData()[i][1])) / 2, 240 + 50 * i);
                draw.drawString(users.getData()[i][2], 420 + (100 - draw.getFontWidth(users.getData()[i][2])) / 2, 240 + 50 * i);
                draw.drawString(users.getData()[i][3], 520 + (160 - draw.getFontWidth(users.getData()[i][3])) / 2, 240 + 50 * i);
                draw.drawString(users.getData()[i][4], 740 + (160 - draw.getFontWidth(users.getData()[i][4])) / 2, 240 + 50 * i);

                draw.drawImage(ImageHelper.getImage("res/löschen.png"), 920, 220 + 50 * i, 15, 15);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {

            for (int i = 0; i < users.getRowCount(); i++) {

                if(e.getX() > 920 && e.getX() < 935 && e.getY() > 220 + 50 * i && e.getY() < 235 + 50 * i) {

                    connector.executeStatement("DELETE FROM JansEmpire_Buildings WHERE Mail = '" + users.getData()[i][0] + "';");
                    connector.executeStatement("DELETE FROM JansEmpire_PlayerData WHERE Mail = '" + users.getData()[i][0] + "';");
                    connector.executeStatement("DELETE FROM JansEmpire_UserQuestList WHERE Mail = '" + users.getData()[i][0] + "';");
                    connector.executeStatement("DELETE FROM JansEmpire_Users WHERE Mail = '" + users.getData()[i][0] + "';");

                    refresh();
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }

        @Override
        public void update(double delta) {

            if(refresh.isClicked()) {

                refresh();
            }
        }
    }
