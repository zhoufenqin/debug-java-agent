# debug-java-agent
reference: https://github.com/runsidekick/sidekick-agent-java

The repo is a java agent which support dynamic logging injection


The feature include:
- 1. inject log around class method
- 2. inject log after a specific line (with specific condition)

Now the features hasn't been integrated into one agent, and there is a provided "containerapps-albumapi-java-0.0.1-SNAPSHOT.jar" jar can be used for the dynamic logging injection. There are some hard-code in the repo which need to be improved later

Step to use the java agent in local env:
- configure `FILE_PATH` in [DebugClassFileTransformer.java](src%2Fmain%2Fjava%2Fcom%2Fmicrosoft%2Fazure%2Fspring%2FDebugClassFileTransformer.java), point to the transform configuration file, example `transform` file is under resources folder
  - `around#{classname}#{methodname}` means add around log
  - `insert#{classname}#{lineno}#{printlogcontent}[#{conditions}]` means insert log after specific line [with specific condition]
- run `mvn clean package -DskipTests`
- run `java -javaagent:./target/debug-java-agent-0.0.1-SNAPSHOT.jar -jar ./containerapps-albumapi-java-0.0.1-SNAPSHOT.jar` to start the app
- curl `localhost:8080/{path}` to see the request,in this transform demo
  - curl `localhost:8080/albums` to see the logs around method, example output
    ```java
    15633 [http-nio-8080-exec-4] INFO  e.a.c.AlbumController - come in to getAllAlbums
    15633 [http-nio-8080-exec-4] INFO  e.a.c.AlbumController - hello
    [===== START =====] Method: getAlbums Start Time: 2024-09-14T02:27:53.771934333Z
    15634 [http-nio-8080-exec-4] INFO  e.a.c.AlbumController - come in to getAlbums
    [===== END =====] Method: getAlbums End Time: 2024-09-14T02:27:53.773362778Z Total Time: 1 milliseconds
    ```
  - curl `localhost:8080/condition?number=5` to see the log insert after a specific line
    ```java
    6609 [http-nio-8080-exec-1] INFO  e.a.c.AlbumController - log number: 5
    On logpoint in method examples/azure/containerappsalbumapijava/AlbumController.testConditions on line 40
    - number: 5
    print log when number == 5
    6611 [http-nio-8080-exec-1] INFO  e.a.c.AlbumController - hi
     ```



TODO: 

We may consider to use bytebuddy to replace javassist tool?

Some dependency libs are old, consider to upgrade them.