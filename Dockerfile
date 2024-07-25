FROM eclipse-temurin:21.0.4_7-jre-alpine

WORKDIR /app

COPY ./target/memos_bot.jar .

