package ids.lambda.stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import poc.ids.streams.Payload;
import poc.ids.streams.STATUS;
import poc.ids.streams.Source;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class StreamApplicationTests {

    @Autowired
    private Supplier<Source> trigger;

    @Test
    void shouldGenerateAllStatusTypes() {
        // Repeatedly call the supplier to get a stream of Source objects
        Set<STATUS> generatedStatuses = Stream.generate(trigger)
                .limit(1000) // Call it 1000 times to have a high chance of getting all statuses
                .map(source -> (Payload) source.get("EVENT"))
                .map(payload -> (STATUS) payload.get("STATUS"))
                .collect(Collectors.toSet());

        // The set of all possible statuses
        Set<STATUS> allStatuses = EnumSet.allOf(STATUS.class);

        // Assert that the set of generated statuses is equal to the set of all possible statuses
        assertThat(generatedStatuses).isEqualTo(allStatuses);
    }
}
