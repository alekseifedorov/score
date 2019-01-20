package my.assignment.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TransportClient {

    private static final String AMAZON_COMPLETE = "http://completion.amazon.com/search/complete?search-alias=aps&mkt=1&q=%s";

    private final RestTemplate restTemplate;

    public List<String> search(String keyword) {
        String url = String.format(AMAZON_COMPLETE, keyword);
        Object[] body = restTemplate.getForEntity(url, Object[].class).getBody();
        return body != null && body.length > 1 ? (List<String>) body[1] : Collections.emptyList();
    }
}
