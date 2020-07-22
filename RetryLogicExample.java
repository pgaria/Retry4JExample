import com.evanlennick.retry4j.CallExecutorBuilder;
import com.evanlennick.retry4j.Status;
import com.evanlennick.retry4j.config.RetryConfig;
import com.evanlennick.retry4j.config.RetryConfigBuilder;
import java.util.concurrent.Callable;
import static java.time.temporal.ChronoUnit.SECONDS;

public class RetryLogicExample {
    private int counter = 0;

    public String getRetryString() {
        System.out.println("In method trying for : " + counter);
        counter++;
        if (counter < 4) {
            System.out.println("Throw the Exception");
            throw new RuntimeException();
        }
        return "Retry";
    }

    public static void main(String[] args) {
        RetryLogicExample retry = new RetryLogicExample();
        RetryConfig retryConfig = new RetryConfigBuilder().withMaxNumberOfTries(5)
                                         .withFixedBackoff().withDelayBetweenTries(2, SECONDS)
                                         .retryOnSpecificExceptions(RuntimeException.class).build();
        Callable<Object> callable = () -> {
            return retry.getRetryString();
        };
        Status<String> result = new CallExecutorBuilder().config(retryConfig).build().execute(callable);
        System.out.println("Retry string at the End: " + result.getResult());
    }
}

/*
Output:
In method trying for : 0
Throw the Exception
In method trying for : 1
Throw the Exception
In method trying for : 2
Throw the Exception
In method trying for : 3
Retry string at the End: Retry
*/
