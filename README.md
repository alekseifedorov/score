1. This is a Microservice based on the Spring framework that exposes the following single REST endpoint :

   http://<HOSTNAME>:9000/estimate?keyword=<keyword>

- Input: an Amazon Keyword 
- Output (json format) : a search-volume estimate from 0 to 1

For example, when typing `http://localhost:9000/estimate?keyword=linux` in the browser the result is following:
  {
    "keyword": "linux",
    "score": 0.3134765625
  }

2. Algorithm
  The input Amazon word is fed to http://completion.amazon.com/search/complete. 
This operation is repeated for each item of the returned list if this item contains the word.

The list is supposed to be sorted by relevancy so each item has the respective weight in the formula as follows:

   EST = N1 * W1 + N2 * W2 + ... + N10 * W10 

      where Nj is 0 or 1 whether the input word is present in the item of the second level result or not;
      Wj is gradually reduced weights (0.5, 0.25, 0.125 etc).

To perform the requests in parallel the java.util.concurrent.ThreadPoolExecutor is used. The overall timeout is 10 seconds.

3. How to build
    Type 'gradlew clean build' to build amazon-score-1.0.jar.

    The server port is specified in application.properties (server.port=9000)

4. How to run
  Type either of the following commands:
 -  java -jar build/libs/amazon-score-1.0.jar
 -  gradlew bootRun
