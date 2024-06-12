# Backend for Lysep

## Used technologies

Java (Spring Boot, Selenium), Docker, 

## Prerequisites

Install Java (+v18) and Docker Daemon (can be Docker Desktop for instance)

## How to run the backend

- Launch the Docker Daemon
- Open the project and go inside the folder src/main/resources then type docker-compose up smtp4dev
  (Alternatively you can click on the arrow in IntelliJ)
  ![img_1.png](imagesReadme/img1.png)

- Then on IntelliJ open the file pom.xml and click on Reload All Maven Projects
![img2.png](imagesReadme/img2.png)

- Then go the file src/java/software.engineering.lysep/LysepApplication and click on 'Run'
![img3.png](imagesReadme/img3.png)

## Additional Information

API endpoints are available in resources, to be imported into postman.  

The database ER diagram is also available here:
![lysep.png](src/main/resources/lysep.png)

You can also run the tests in the Controller and Selenium subfolders of the test folder.