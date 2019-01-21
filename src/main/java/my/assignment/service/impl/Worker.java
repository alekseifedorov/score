package my.assignment.service.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import static my.assignment.service.impl.AmazonScoreServiceImpl.WEIGHTS;

/**
 * Class to estimate the partial score by the second level's keyword.
 */
@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Setter
public class Worker implements Callable<Double> {

    private TransportClient transportClient;
    private String keyword;
    private String returnedResult;
    private double firstLevelWeight;

    public Worker(TransportClient transportClient) {
        this.transportClient = transportClient;
    }

    @Override
    public Double call() {
        try {
            return doCall();
        } catch (Exception e) {
            log.error("Unexpected exception: " + e, e);
            return null;
        }
    }

    private double doCall() {
        Objects.nonNull(returnedResult);
        List<String> result = transportClient.search(returnedResult);

        float partialScore = 0;
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).contains(keyword)) {
                partialScore += firstLevelWeight * WEIGHTS.get(i);
            }
        }

        return partialScore;
    }
}
