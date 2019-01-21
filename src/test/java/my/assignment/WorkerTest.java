package my.assignment;

import my.assignment.service.impl.TransportClient;
import my.assignment.service.impl.Worker;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import static my.assignment.service.impl.AmazonScoreServiceImpl.WEIGHTS;
import static org.mockito.Mockito.*;


public class WorkerTest {

	@Test
	public void shouldEstimate() {
		// let's suppose the first found item for `steven` is `king`
		String keyword = "steven";
		String returnedResult = "king";
		TransportClient client = mock(TransportClient.class);
		when(client.search(returnedResult)).thenReturn(Arrays.asList("king steven", "king kong", "king abc", "king of horror steven"));
		Worker worker = new Worker(client);
		worker.setReturnedResult(returnedResult);
		worker.setKeyword(keyword);
		worker.setFirstLevelWeight(WEIGHTS.get(0));
		Double score = worker.call();
		assertThat(score).isEqualTo(0.28125); // 0.5 * 0.5 + 0.5 * 0.0625
	}
}

