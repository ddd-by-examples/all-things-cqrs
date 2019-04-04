# All Things CQRS

A bunch of ways of doing [CQRS](https://martinfowler.com/bliki/CQRS.html) with various [Spring](https://spring.io) tools.

## Getting Started

These instructions will get you and overview of how to synchronize two different datasources. We will do so by separating command and queries in a simple CQRS app. Each module represents a different way of introducing this pattern. Also, each module is a standalone [Spring Boot](https://spring.io/projects/spring-boot) application. 

### Prerequisites

What things you need to run the software:

* Java 11+
* [docker-compose](https://docs.docker.com/compose/)

## Overview

Sample applications are based on a simple domain that serves credit cards. There are two usecases:

*  Money can be withdrawn from a card (*Withdraw* **command**)
*  List of withdrawals from a card can be read (**query**)

The important is that:
```
After a successful Withdraw command, a withdrawal should be seen in a result from list of withdrawals query.
```

Hence there is a need for some **synchronization** that makes state for commands and queries consistent.

Let's agree on a color code for commands, queries and synchronization. It will make our drawings consistent.

![color code](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/colorcode.jpg "Color code")

### Commands and queries handled in one class (no CQRS)

Code can be found under [in-one-class](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/in-one-class) module. 

Running the app:
```
mvn spring-boot:run
```

A sample *Withdraw* command:

```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query:
```
curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result:
```
[{"amount":10.00}]
```

Architecture overview:

![in-one-class](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/inoneclass.jpg)

Automatic E2E test for REST API can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/in-one-class/src/test/java/io/dddbyexamples/cqrs/CommandQuerySynchronizationTest.java):

```java
    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100)); //HTTP POST
        // when
        clientWantsToWithdraw(TEN, cardUUid); //HTTP GET
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }
```

### CQRS with application service as explicit synchronization

Code can be found under [explicit-with-dto](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/explicit-with-dto) module. Same version, but with JPA entities as results of a query can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/explicit-with-entity).

Running the app:
```
mvn spring-boot:run
```

A sample *Withdraw* command:

```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query:
```
curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result:
```
[{"amount":10.00}]
```

Architecture overview:

![application-process](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/appprocess.jpg) 

Automatic E2E test for REST API can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/explicit-with-dto/src/test/java/io/dddbyexamples/cqrs/CommandQuerySynchronizationTest.java):

```java
    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100)); //HTTP POST
        // when
        clientWantsToWithdraw(TEN, cardUUid); //HTTP GET
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }
```

### CQRS with spring application events as implicit synchronization

Code can be found under [with-application-events](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-application-events) module.

There is also a version with immutable domain module which just returns events. It Can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-application-events-immutable).

Running the app:
```
mvn spring-boot:run
```

A sample *Withdraw* command:

```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query:
```
curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result:
```
[{"amount":10.00}]
```

Architecture overview:

![appevents](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/appevents.jpeg) 

Automatic E2E test for REST API can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-application-events/src/test/java/io/dddbyexamples/cqrs/CommandQuerySynchronizationTest.java):

```java
    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100)); //HTTP POST
        // when
        clientWantsToWithdraw(TEN, cardUUid); //HTTP GET
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }
```

### CQRS with trigger as implicit synchronization

Code can be found under [trigger](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-trigger) module.

Running the app:
```
mvn spring-boot:run
```

A sample *Withdraw* command:

```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query:
```
curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result:
```
[{"amount":10.00}]
```

Architecture overview:

![trigger](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/trigger.jpg) 

Automatic E2E test for REST API can be found [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-trigger/src/test/java/io/dddbyexamples/cqrs/CommandQuerySynchronizationTest.java):

```java
    @Test
    public void shouldSynchronizeQuerySideAfterSendingACommand() {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100)); //HTTP POST
        // when
        clientWantsToWithdraw(TEN, cardUUid); //HTTP GET
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }
```

### CQRS with transaction log tailing as synchronization

Synchronization done by listening to database's [transaction log](https://en.wikipedia.org/wiki/Transaction_log), which is a log of transactions accepted by a database management system.

Code can be found under [with-log-tailing](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-log-tailing) module.

Additional components:
*  MySQL to keep withdrawals and credit cards.
*  [Apache Kafka](https://kafka.apache.org) for pub/sub for messages read from database transaction log (in this case it is MySQL).
*  [Kafka Connect](https://www.confluent.io/product/connectors/) with [Debezium](https://debezium.io) to read MySQL’s transaction log and stream messages to Kafka’s topic.
*  [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read messages from Kafka’s topic.


Running the app, remember to be in **root** of the project:

* In *docker-compose.yaml*, under service *kafka* - **CHANGE** IP to match your host machine. Keep port pointing to 9092:
```
ADVERTISED_LISTENERS=PLAINTEXT://YOUR_HOST_IP:9092
```
* Run the whole infrastructure:
```
docker-compose up
```
* Tell Kafka Connect to tail transaction log of MySQL DB and send messages to Kafka:
```
curl -i -X POST -H "Accept:application/json" -H  "Content-Type:application/json" http://localhost:8083/connectors/ -d @source.json --verbose
```
A sample *Withdraw* command:
```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query:
```
curl http://localhost:8080/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result can be seen below. Remember that it takes time to read transaction log and create a withdrawal. Hence a withdrawal might be not immedietly seen:
```
[{"amount":10.00}]
```

Architecture overview:

![logtailing](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/transactionlog.jpg) 

Since it is problematic (or immposible) to test transaction log tailing, there is no E2E test that verifies commands and queries. But we can test if a message arrival in Kafka's topic results in a proper withdrawal created. The code is [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-log-tailing/src/test/java/io/dddbyexamples/cqrs/sink/ReadModelUpdaterTest.java):

```java
    @Test
    public void shouldSynchronizeQuerySideAfterLogTailing() {
        // given
        String cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        creditCardUpdateReadFromDbTransactionLog(TEN, cardUUid);
        // then
        thereIsOneWithdrawalOf(TEN, cardUUid);
    }
```
### CQRS with Domain Events as synchronization

Synchronization done by sending a domain event after succesfully handling a command.

Code can be found under [events](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events) module. It has 2 further modules, architecture is fully distributed. There is a [source](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events/with-events-source) (deals with commands) and [sink](https://github.com/ddd-by-examples/all-things-cqrs/tree/master/with-events/with-events-sink) (deals with queries).


Additional components:
*  H2 DB to keep credit cards.
*  [MongoDB](https://www.mongodb.com/what-is-mongodb) to keep withdrawals.
*  Spring Data Reactive MongoDb to reactively talk to Mongo
*  [Project Reactor](http://projectreactor.io) to serve non-blocking web-service
*  [Apache Kafka](https://kafka.apache.org) for pub/sub for domain events
*  [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream/) to read/write messages from/to Kafka’s topic.


Running the app, remember to be in **root** of the project:

*  Run the whole infrastructure:
```
docker-compose up
```

A sample *Withdraw* command:
```
curl localhost:8080/withdrawals -X POST --header 'Content-Type: application/json' -d '{"card":"3a3e99f0-5ad9-47fa-961d-d75fab32ef0e", "amount": 10.00}' --verbose
```
Verifed by a query (notifce a different port: **8888**!):
```
curl http://localhost:8888/withdrawals?cardId=3a3e99f0-5ad9-47fa-961d-d75fab32ef0e --verbose
```
Expected result can be seen below. Remember that it takes time to publish and read domain events from Kafka. Hence a withdrawal might be not immedietly seen:
```
[{"amount":10.00}]
```

Architecture overview:

![events](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/events.jpg) 

Since it is not recommended to test 2 microservices in one test, there is no E2E test that verifies commands and queries. But we can test if a message arrival in Kafka's topic results in a proper withdrawal created. The code is [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-events/with-events-sink/src/test/java/io/dddbyexamples/cqrs/sink/ReadModelUpdaterTest.java):

```java
    @Test
    public void shouldSeeWithdrawalAfterGettingAnEvent() {
        //when
        anEventAboutWithdrawalCame(TEN, cardID);

        //then
        thereIsOneWithdrawalOf(TEN, cardID);
    }
```
Also it is possible to test if a successful withdrawal is followed eventually by a proper domain event publication. The code is [here](https://github.com/ddd-by-examples/all-things-cqrs/blob/master/with-events/with-events-source/src/test/java/io/dddbyexamples/cqrs/EventsPublishingTest.java). 

```java
    @Test
    public void shouldEventuallySendAnEventAboutCardWithdrawal() throws IOException {
        // given
        UUID cardUUid = thereIsCreditCardWithLimit(new BigDecimal(100));
        // when
        clientWantsToWithdraw(TEN, cardUUid);
        // then
        await().atMost(FIVE_SECONDS).until(() -> eventAboutWithdrawalWasSent(TEN, cardUUid));
    }
```

### CQRS with Axon Framework
Take a look [here](https://github.com/pivotalsoftware/ESarch)
