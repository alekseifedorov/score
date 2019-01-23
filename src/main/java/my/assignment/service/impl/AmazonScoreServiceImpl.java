package my.assignment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.assignment.exception.MyException;
import my.assignment.model.Score;
import my.assignment.service.AmazonScoreService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Main class to create workers and summarize received partial results
 */
@Service
@AllArgsConstructor
@Slf4j
public class AmazonScoreServiceImpl implements AmazonScoreService {

    public static final int MAX_SCORE = 100;
    public static final int SEARCH_RESULTS_SIZE = 10;

    private final ExecutorService service;
    private final ObjectFactory<Worker> workerFactory;

    @Override
    public Score estimate(String keyword) {
        try {
            double score = createAndStartWorkers(keyword);
            return new Score(keyword, score);
        } catch (InterruptedException e) {
            throw new MyException("service interrupted", e);
        }
    }

    private long createAndStartWorkers(String keyword) throws InterruptedException {
        String prefix = "";
        List<Worker> workers = new ArrayList<>();
        for (char c : keyword.substring(0, keyword.length() - 1).toCharArray()) {
            prefix += c;
            Worker worker = workerFactory.getObject();
            worker.setPrefix(prefix);
            worker.setKeyword(keyword);
            workers.add(worker);
        }

        List<Future<Long>> futures = service.invokeAll(workers, 10, TimeUnit.SECONDS);

        int coefficient = MAX_SCORE / ((keyword.length() - 1) * SEARCH_RESULTS_SIZE);

        return futures.stream()
                      .map(f -> {
                          try {
                              return f.get();
                          } catch (Exception e) {
                              log.error("Exception when getting result", e);
                              return null;
                          }
                      })
                      .filter(Objects::nonNull)
                      .mapToLong(f -> f)
                      .sum() * coefficient;
    }
}
