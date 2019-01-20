package my.assignment.controller;

import lombok.AllArgsConstructor;
import my.assignment.model.Score;
import my.assignment.service.AmazonScoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class AmazonScoreController {

    private final AmazonScoreService amazonScoreService;

    @GetMapping("estimate")
    public Score estimate(@RequestParam String keyword) {
        if (StringUtils.isBlank(keyword)) {
            throw new IllegalArgumentException("returnedResult");
        }

        return amazonScoreService.estimate(keyword);
    }
}
