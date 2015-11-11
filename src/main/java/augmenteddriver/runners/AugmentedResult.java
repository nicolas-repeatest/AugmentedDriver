package augmenteddriver.runners;

import org.junit.runner.Result;

import java.io.ByteArrayOutputStream;

/**
 * Wrapper o a result that also contains the output stream.
 */
public class AugmentedResult {

    private final Result result;
    private final ByteArrayOutputStream out;

    public AugmentedResult(Result result, ByteArrayOutputStream out) {
        this.result = result;
        this.out = out;
    }

    public Result getResult() {
        return result;
    }

    public ByteArrayOutputStream getOut() {
        return out;
    }
}
