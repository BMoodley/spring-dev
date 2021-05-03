The TechStack:
    
    > Fail over using Resilience4J from spring cloud circuit breaker
    > Swagger UI (SprinFox) 
    > Prometheus metrics with micrometer
    > Junit Integration Testing
    > Mongodb Atlas Integration
    
URLS (to see app health, swagger and prometheus metrics):

    swagger ui    
    http://localhost:8081/swagger-ui/
    
    swagger docs
    http://localhost:8081/v2/api-docs
    
    default metrics
    http://localhost:8081/actuator/metrics
    localhost:8081/actuator/health
    http://localhost:8081/actuator/metrics/jvm.memory.used
    
    these counters can be scraped by a prometheus dashboard (counts and times) 
    and are visible under prometheus when looking at "http://localhost:8081/actuator/metrics"
    
        products_time_seconds_count
        products_time_seconds_sum
        (there are more for each GET call)
    
To Run the App:

download the "activedays-0.0.1-SNAPSHOT.jar" from the target folder
and execute this cmd (eg. in gitBash) on its location

    java -jar activedays-0.0.1-SNAPSHOT.jar
    

How to use endpoints:
    
   (Note: a postman collection is included - with sample responses saved
   file: Active Days Points.postman_collection.json)

Operations by curl command:

Customer can view all products:

    curl --location --request GET 'http://localhost:8081/product/products'

View all customers:

    curl --location --request GET 'http://localhost:8081/customer/customers'

Customer can view their purchased orders by customer id:

    curl --location --request GET 'http://localhost:8081/order/customer/608eaf8a0d375292bcf052e1'

Customer does not have enough points to place order:

    curl --location --request POST 'http://localhost:8081/order/orders' \
    --header 'Content-Type: application/json' \
    --data-raw '{
       "customer":"608f6d67e82d8f8c73f9b0c4",
       "products":[
          "608f6dade82d8f8c73f9b0c5",
          "608eb52e0d375292bcf052e4"
       ]
    }'