package app.netlify.winikim.pay.share.util;

import java.util.Random;

public class Utils {

  public static String generateToken(int length) {
    char[] characterTable = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L',
        'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j',
        'k', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
        'w', 'x', 'y', 'z'};
    Random random = new Random(System.currentTimeMillis());
    int tableLength = characterTable.length;
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < length; i++) {
      stringBuilder.append(characterTable[random.nextInt(tableLength)]);
    }
    return stringBuilder.toString();
  }

}
