package augmenteddriver.util;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

/**
 * Various utilities.
 */
public class Util {
    private static final SecureRandom random = new SecureRandom();

    public static void pause(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("InterrupedException while pausing", e);
        }
    }

    public static long getRandom(int min, int max) {
        return (long)(random.nextInt(max - min + 1) + min);
    }

    public static String getRandomAsString() {
        String randomString = String.valueOf(Math.abs(random.nextLong()));
        return randomString.substring(0, Math.min(10, randomString.length()));
    }

    public static String shortenClass(Class<?> clazz) {
        Preconditions.checkNotNull(clazz);
        String[] subpackages = clazz.getCanonicalName().split("\\.");
        StringBuilder builder = new StringBuilder();
        for(int index = 0; index < subpackages.length -1; index++) {
            builder.append(subpackages[index].substring(0, 1) + ".");
        }
        return builder.append(subpackages[subpackages.length - 1]).toString();
    }

    public static final Function<Long, String> TO_PRETTY_FORNAT = millis -> String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

}
