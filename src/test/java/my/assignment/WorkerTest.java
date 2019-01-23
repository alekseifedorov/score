package my.assignment;

import my.assignment.service.impl.TransportClient;
import my.assignment.service.impl.Worker;
import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


public class WorkerTest {

    @Test
    public void shouldEstimate() {
        String keyword = "tablet";
        TransportClient client = mock(TransportClient.class);
        when(client.search(any())).thenReturn(Arrays.asList("tab abc", "taburet def", "tatoo ghi klm", "tatoo ghi klm",
                "turttle ghi klm", "toomba ghi klm", "tartartar ghi klm", "tablet xiaomi", "tablet samsung", "tableron zhk"));
        Worker worker = new Worker(client);
        worker.setKeyword(keyword);
        worker.setPrefix(keyword.substring(0, keyword.length() - 2));
        Long score = worker.call();
        assertThat(score).isEqualTo(2); // 2 occurrences of `table`
    }
}

