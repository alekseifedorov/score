package my.assignment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.assignment.exception.MyException;
import my.assignment.model.Score;
import my.assignment.service.AmazonScoreService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class AmazonScoreServiceImpl implements AmazonScoreService {

    protected static final List<Double> WEIGHTS = Arrays.asList(0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625, 0.0078125, 0.00390625, 0.001953125, 0.001953125);

    private final TransportClient transportClient;
    private final ExecutorService service;
    private final ObjectFactory<Worker> workerFactory;

    @Override
    public Score estimate(String keyword) {
        try {
            double score = doEstimate(keyword);
            return new Score(keyword, score);
        } catch (InterruptedException e) {
            throw new MyException("worker manager interrupted", e);
        }
    }

    private double doEstimate(String keyword) throws InterruptedException {
        //first search
        List<String> resultKeywords = transportClient.search(keyword);

        List<Worker> workers = new ArrayList<>();
        for (int i = 0; i < resultKeywords.size(); i++) {
            Worker worker = workerFactory.getObject();
            worker.setFirstLevelWeight(WEIGHTS.get(i));
            worker.setReturnedResult(resultKeywords.get(i).substring(keyword.length()).trim());
            worker.setKeyword(keyword);
            workers.add(worker);
        }

        List<Future<Double>> futures = service.invokeAll(workers, 10, TimeUnit.SECONDS);

        return futures.stream()
                      .map(f -> {
                          try {
                              return f.get();
                          } catch (Exception e) {
                              throw new MyException("Exception when getting result", e);
                          }
                      })
                      .filter(Objects::nonNull)
                      .mapToDouble(f -> f)
                      .sum();
    }
}
