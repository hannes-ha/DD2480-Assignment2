# DD2480 Assignment 2 (CI Server)
This project is centered around the creation of a Continuous Integration server for GitHub. The purpose of it is to be able to clone any repository and build + run it. By using a webhook on GitHub which is triggered on commits, the server is capable of receiving the latest changes made to any branch and checking if it works, whereafter it reports the result to the author of the commit. The main focus of the project is to implement the core features of a CI server, in order to accomplish this desired result.

## Build & run or test
Maven quickstart [Guide](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html)

1. Install maven
2. mvn compile
3. mvn test
4. mvn exec:java

## Team
Going off of the checklist for the Essence standard, we conclude that our team is at least in the "Collaborating" stage. At this point in time, we have all gotten to know each other and are constantly communicating in order to organise all the work among us. Overall we are making solid progress, and are structuring up the work so that different parts can easily be assigned between us team members. It is hard to say whether we fully achieve the status of "Performing", since our way of working could be improved further so that we work even more efficiently together.

## Individual Contributions

Amanda:


Erik (tayloh):
* Maven project setup
* Skeleton structure for ContinuousIntegrationServer class (request handling) (POST, GET)
* Top-level handling of POST requests (parsing request) + Util class for parsing request information + Tests for Util class
* MavenRunner class for handling running mvn compile, mvn test, and collecting maven output
* Some parts of runContinuousIntegration() in ContinuousIntegrationServer class

Tobias:
* Handler for git commands (cloning repo & setting directory) + tests
* Deletion of git directories after use + tests

Hannes:
- Github commit status handler
- Build history frontend and history doc generator
