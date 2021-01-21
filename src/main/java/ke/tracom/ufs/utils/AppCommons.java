package ke.tracom.ufs.utils;

/**
 *
 * @author Kenny
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class AppCommons {

        public AppCommons() {

        }

        /**
         *
         * @return
         */
        private String getUuidString() {
            return UUID.randomUUID().toString();
        }

        /**
         * Random Filename for file storage service
         *
         * @param fileName
         * @return
         */
        public String getRandomFileName(String fileName) {
            return (this.getUuidString() + fileName.replaceAll("\\s+", "")).replaceAll("-", "");
        }

        /**
         * generateAccountNumber
         *
         * @return
         */
        public String generateAccountNumber() {
            String prefix = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
            String suffix = String.valueOf(new Random().nextInt(999));
            return prefix + suffix;

        }


}
