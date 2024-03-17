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

@SpringBootApplication
public class StreamApplication {

	private final Log logger = LogFactory.getLog(getClass());

	public static void main(String[] args) {
		SpringApplication.run(StreamApplication.class, args);
	}

	@Bean
	public Consumer<Source> process() {
		return input -> logger.info("[INPUT-RECEIVED]: " + input.toString());
	}

	@Bean
	public Supplier<Source> trigger() {
		return () -> random();
	}

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
