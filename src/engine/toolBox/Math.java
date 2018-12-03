package engine.toolBox;

import java.text.SimpleDateFormat;
import java.util.Date;

    public class Math {

        public static int generatePrime(int index) {

            int j = index;
            boolean prime = false;

            while (!prime) {

                for(int i = 2; i < j - 1; i++) {

                    if(j % i == 0) {

                        prime = false;
                        break;
                    } else prime = true;
                }

                if(prime) return j;
                else j++;
            }
            return -1;
        }

        public static String getDateTime() {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTime = new Date();
            return formatter.format(currentTime);
        }
    }
