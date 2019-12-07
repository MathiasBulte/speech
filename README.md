[![Release](https://jitpack.io/v/MathiasBulte/speech.svg)](https://jitpack.io/#MathiasBulte/speech)

When making a service that interacts with Alexa, I struggled with easily making correct sentences. I quickly abandoned the service which interacts with Alexa, but I was intrigued with making a library that made it simpler to create coherent sentences. 

# Example
My use case was a health indicator endpoint which can be called through Alexa, to get a quick overview of the status of my systems.
I wanted to make Alexa say "2 applications are having trouble". In this first sentence, there are already a couple of exceptions:
- Nothing's wrong: "All applications are up and running"
- 1 application is having trouble: "1 *application is having* trouble"
- A few instances are having trouble: "2 *applications are having* trouble"

```java
String singular = Speech.builder()
        .sentence("{} {} having trouble",
                tuple(1),
                plural(Tuple1::v1, "application is", "applications are"))
        .build();

assertEquals("1 application is having trouble.", singular);

String plural = Speech.builder()
        .sentence("{} {} having trouble",
                tuple(2),
                plural(Tuple1::v1, "application is", "applications are"))
        .build();

assertEquals("2 applications are having trouble.", plural);

```
A more advanced example:
```java
var speech = Speech.builder()
        .sentence("{} {} having trouble", tuple(2),
                plural(Tuple1::v1, "application is", "applications are"))
        .enumeration("{} out of {} of the {} instances are down",
                List.of(tuple(1, 2, "alexa-service"),
                        tuple(2, 4, "adoption-service")))
        .build();

assertEquals("2 applications are having trouble. 1 out of 2 of the alexa-service instances are down " +
        "and 2 out of 4 of the adoption-service instances are down.", speech);
```
More examples can be found in the unit tests.
