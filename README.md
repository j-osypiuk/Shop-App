# Store-Management-System

Store management system designed to streamline the operations of retail businesses by automating various tasks such as inventory management, sales tracking, and notifying customers about their order details via email. Created by using the Spring Framework and a PostgreSQL database.

INSTRUCTION:
1. To start application, go to main project directory and run command:
   - docker-compose up -d

   It will start docker container with seeded database.
2. Start api project module to launch web api on localhost:8080
3. You can test api using web api client software like Postman etc. or open localhost:8080/swagger-ui/index.html to use swagger.
   
   The app has 3 users with different roles, to login use:
   - ADMIN:
       - username: adm@mail.com
       - password: password
   - EMPLOYEE:
       - username: emp@mail.com
       - password: password
   - CUSTOMER:
       - username: cus@mail.com
       - password: password

Email notifications are disabled by default. To enable them, go to the email-client module Config.java file and fill in the commented mailSender username and password with your own gmail credentials.
