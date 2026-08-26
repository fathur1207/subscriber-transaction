Provide new API to get subscriber monthly transaction summary. Total amount and total transaction for each month. For security measurement we need to validate subscriber PIN

#application.propertis config
spring.application.name= subscriber-transaction
spring.datasource.driverClassName= com.mysql.cj.jdbc.Driver
spring.datasource.url= jdbc:mysql://localhost:3306/xl_playground
spring.datasource.username= root
spring.datasource.password= root
spring.datasource.name= PlaygroundDB
spring.jackson.default-property-inclusion=non_null
query.transaction.fetchbymsisdn= select * from transaction where msisdn = :msisdn
getpin.baseurl= https://4k38m.wiremockapi.cloud

