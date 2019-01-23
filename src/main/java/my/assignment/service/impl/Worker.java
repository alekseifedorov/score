package my.assignment.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Class to make a request and find the number of occurrences of the main completed keyword.
 */
@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Setter
public class Worker implements Callable<Long> {

    private TransportClient transportClient;

    private String keyword;
    private String prefix;

    public Worker(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @Override
    public Long call() {
        try {
            return doCall();
        } catch (Exception e) {
            log.error("Exception in worker", e);
            return null;
        }
    }

    private Long doCall() {
        List<String> result = transportClient.search(prefix);

        long occurences = result.stream().filter(r -> r.startsWith(keyword)).count();

        log.trace("Prefix: {}; occurences: {}", prefix, occurences);

        return occurences;
    }
}
