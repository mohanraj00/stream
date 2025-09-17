package ids.lambda.stream;

import java.time.Instant;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import poc.ids.streams.Payload;
import poc.ids.streams.STATUS;
import poc.ids.streams.Source;

/**
 * Main class for the Spring Boot application.
 * <p>
 * This application demonstrates the use of Spring Cloud Stream with Kafka and Avro.
 * It includes a supplier to generate random data and a consumer to process it.
 * </p>
 */
@SpringBootApplication
public class StreamApplication {

	private final Log logger = LogFactory.getLog(getClass());

	/**
     * The main entry point for the Spring Boot application.
     *
     * @param args The command-line arguments.
     */
	public static void main(String[] args) {
		SpringApplication.run(StreamApplication.class, args);
	}

	/**
     * A Spring Cloud Stream consumer that processes incoming {@link Source} objects.
     * <p>
     * This method defines a bean that listens for messages from a Kafka topic,
     * deserializes them into {@link Source} objects, and logs their content.
     * </p>
     *
     * @return A {@link Consumer} that handles the incoming data.
     */
	@Bean
	public Consumer<Source> process() {
		return input -> logger.info("[INPUT-RECEIVED]: " + input.toString());
	}

	/**
     * A Spring Cloud Stream supplier that generates random {@link Source} objects.
     * <p>
     * This method defines a bean that periodically generates random data,
     * wraps it in a {@link Source} object, and sends it to a Kafka topic.
     * </p>
     *
     * @return A {@link Supplier} that provides the data to be sent.
     */
	@Bean
	public Supplier<Source> trigger() {
		return () -> random();
	}

	/**
     * Generates a random {@link Source} object with a nested {@link Payload}.
     * <p>
     * This method creates a {@link Payload} with a random clinical ID, sales order, and status.
     * It then wraps this payload in a {@link Source} object with a unique ID and timestamp.
     * </p>
     *
     * @return A randomly generated {@link Source} object.
     */
	private Source random() {
		Random random = new Random();
		int clin_id = random.ints(120, 150)
				.findFirst()
				.getAsInt();
		String so = "SO" + random.ints(200, 300).findFirst()
				.getAsInt();
		int rstatus = random.ints(0, 2).findFirst()
				.getAsInt();
		STATUS st = (rstatus == 2) ? STATUS.CREATED : (rstatus == 1) ? STATUS.UPDATED : STATUS.UPDATED;

		Payload p = new Payload(clin_id, so, st);
		Source s = new Source(UUID.randomUUID(), Instant.now(), p);
		logger.info("[SOURCE-GENERATED]: " + s);
		return s;
	}

}
