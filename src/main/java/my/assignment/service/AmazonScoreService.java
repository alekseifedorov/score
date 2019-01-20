package my.assignment.service;

import my.assignment.model.Score;

public interface AmazonScoreService {

    Score estimate(String keyword);
}
