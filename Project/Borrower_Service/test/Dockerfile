
FROM hseeberger/scala-sbt:11.0.13_1.6.1_2.13.7 as build

WORKDIR /app

COPY . /app

RUN sbt clean compile stage

FROM openjdk:11-jre-slim

COPY --from=build /app/target/universal/stage /app

WORKDIR /app

EXPOSE 6474

ENV PLAY_HTTP_SECRET=b1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0u1v2w3x4y5z6A7B8C9D0E1F2G3H4I5J6K7L8M9N0O1P2Q3R4S5T6U7V8W9X0Y1Z2

CMD ./bin/Borrower_Service -Dplay.http.secret.key=$PLAY_HTTP_SECRET