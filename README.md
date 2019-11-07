# ThingWorx-Case-Study

## Troubleshooting
* @Slf4j issue
The annotation is refer to Lombok.

Try the [tutorial](https://www.journaldev.com/18124/java-project-lombok) to resolve the problem.

## Executing spring boot application with external application.yml
```
//Command
$ java -jar <jar file> --spring.config.location=<application location>

//Execute
$ java -jar ThingWorx-Case-Study-0.0.1-SNAPSHOT.jar --spring.config.location=./application.yml
```

*After gradle build, you can find jar file & application file under project directory.*
`/build/libs`