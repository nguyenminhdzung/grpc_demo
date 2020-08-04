# Cinema GRPC demo

This is implementation for GRPC demo of Cinema in the context of novel virus epidemic that the functionalities of cinema operation must respect social distancing.

This implementation is written using Java and MySQL. Please create a DB named **cinema** and an user account **cinema/cinema** or other names then change the **spring.datasource** configuration in *resources/application.properties* respectively. 
DDL script of tables is placed in file *schema.sql*.

The GRPC service is defined in *proto/CinemaEndpoint.proto*, with 3 methods
- configRoom
- getAvailableSeats
- reserveSeats

Please look at the proto file for more detail.

This implementation also using cache technique but for demo purpose, an in-memory cache is used. In production environment, it should use a high performance distributed cache such as Redis.

For demo purpose, UT cases for the GRPC endpoint are added.
