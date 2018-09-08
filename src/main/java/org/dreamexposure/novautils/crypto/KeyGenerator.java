package org.dreamexposure.novautils.crypto;

import java.security.SecureRandom;
import java.util.Random;

@SuppressWarnings("SpellCheckingInspection")
public class KeyGenerator {
    private static char[] VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456879".toCharArray();

    /**
     * Securely generates an alphanumeric string to use for various things.
     *
     * @param numChars The length of the string
     * @return A secure string.
     */
    // cs = cryptographically secure
    @SuppressWarnings("SameParameterValue")
    public static String csRandomAlphaNumericString(int numChars) {
        SecureRandom secRand = new SecureRandom();
        Random rand = new Random();
        char[] buff = new char[numChars];

        for (int i = 0; i < numChars; ++i) {
            // reseed rand once you've used up all available entropy bits
            if ((i % 10) == 0)
                rand.setSeed(secRand.nextLong()); // 64 bits of random!

            buff[i] = VALID_CHARACTERS[rand.nextInt(VALID_CHARACTERS.length)];
        }
        return new String(buff);
    }

    /**
     * Securely generates a string from the provided valid characters to use for various things.
     *
     * @param numChars the length of the string.
     * @param valid    The valid characters to use for the string.
     * @return A secure string.
     */
    // cs = cryptographically secure
    @SuppressWarnings("SameParameterValue")
    public static String csRandomString(int numChars, char[] valid) {
        SecureRandom secRand = new SecureRandom();
        Random rand = new Random();
        char[] buff = new char[numChars];

        for (int i = 0; i < numChars; ++i) {
            // reseed rand once you've used up all available entropy bits
            if ((i % 10) == 0)
                rand.setSeed(secRand.nextLong()); // 64 bits of random!

            buff[i] = valid[rand.nextInt(valid.length)];
        }
        return new String(buff);
    }

}