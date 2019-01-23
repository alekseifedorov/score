1. This is a Microservice based on the Spring framework that exposes the following single REST endpoint :

   http://<HOSTNAME>:9000/estimate?keyword=<keyword>

- Input: an Amazon Keyword 
- Output (json format) : a search-volume estimate from 1 to 100.

For example, when typing `http://localhost:9000/estimate?keyword=linux` in the browser the result is following:
  {
    "keyword": "linux",
    "score": 16
  }

2. Algorithm
  The sequence of substrings are created from the input Amazon word by sequential adding of one letter, for example, for the word `linux` the sequence is following:
  `l`, `li`, `lin`, `linu`.
  Each of the words is fed to http://completion.amazon.com/search/complete which returns 10 strings representing the most popular user's input.
  A score is assigned for one occurence of the main completed keyword in each of 10 returned strings.
  To align scores for words of different length the coefficient is applied. 

3. How to build
    Type 'gradlew clean build' to build amazon-score-1.0.jar.

    The server port is specified in application.properties (server.port=9000)

4. How to run
  Type either of the following commands:
 -  java -jar build/libs/amazon-score-1.0.jar
 -  gradlew bootRun
