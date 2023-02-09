# DD2480 Assignment 2 (CI Server)

The goal of this assignment is to master the core of continuous integration. To achieve this goal, the students are asked to implement a small continuous integration CI server. This CI server will only contain the core features of continuous integration. The features are all specified below, as grading criteria.

The grading focuses on the understanding and implementation of the core CI features, but also considers the application of software engineering on the development process, see the grading scheme below.

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
