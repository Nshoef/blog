This app enable to its user to send posts and like/dislike them.


#### Running to app ####
 
In order to run it you'd need first to build it and create a docker image.
run:

mvn clean install docker:build

You'd now be able to run the docker compose file.

run:

docker compose up


You should be able to see 2 containers, one for elasticsearch and one for the service.

### Documentation ###

You can use swagger to view the api documentation and try the endpoints:

http://localhost:8080/swagger-ui/index.html

#### Default user ###

The app created with a default admin user with password "password".
You can override the password env variable in the docker compose.

You can also set the elastic search password in the same way

### Assumption I took during development ###

1. Updating posts would remove rating. This was not mentioned as required or not
but it felt inappropriate to allow rate something that later can become completely different.
2. User are able to rate their own posts
3. Filter by user id mean author and not person who rate
4. Sort by timestemp means created time and not updated or rated

