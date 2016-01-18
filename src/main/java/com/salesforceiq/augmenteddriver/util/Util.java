package com.salesforceiq.augmenteddriver.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * Various utilities.
 */
public class Util {
    private static final SecureRandom random = new SecureRandom();

    /**
     * Waits for some time, handling Interrupted Exceptions.
     *
     * @param millis How much time to wait.
     */
    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("InterrupedException while pausing", e);
        }
    }

    /**
     * Gets a random number between min and max.
     *
     * @param min the minimum number.
     * @param max the maximum number
     * @return the value between min and max.
     */
    public static long getRandom(int min, int max) {
        return (long)(random.nextInt(max - min + 1) + min);
    }

    /**
     * Returns a 10 length String with a random number.
     *
     * @return the random string.
     */
    public static String getRandomAsString() {
        String randomString = String.valueOf(Math.abs(random.nextLong()));
        return randomString.substring(0, Math.min(10, randomString.length()));
    }

    /**
     * Given a class, it returns a shortened version.
     *
     * <p>
     *     For example com.relateiq.test.TestClass, it returns c.r.t.TestClass.
     * </p>
     *
     * @param clazz the class to shorten.
     * @return the shortened class
     */
    public static String shortenClass(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);

        String[] subpackages = clazz.getCanonicalName().split("\\.");
        StringBuilder builder = new StringBuilder();
        for(int index = 0; index < subpackages.length -1; index++) {
            builder.append(subpackages[index].substring(0, 1) + ".");
        }
        return builder.append(subpackages[subpackages.length - 1]).toString();
    }

    /**
     * Given a long with the time in milliseconds, it returns a pretty format with Hours, minutes, seconds.
     */
    public static final Function<Long, String> TO_PRETTY_FORMAT = millis -> String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

}
