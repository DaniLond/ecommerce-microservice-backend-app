
FROM eclipse-temurin:17-jre
ARG PROJECT_VERSION=0.1.0
RUN addgroup --system --gid 1001 appgroup && \
	adduser --system --uid 1001 --gid 1001 appuser && \
	mkdir -p /home/app && \
	chown -R appuser:appgroup /home/app
WORKDIR /home/app
USER appuser
ENV SPRING_PROFILES_ACTIVE=dev \
	JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"
COPY --chown=appuser:appgroup target/favourite-service-v${PROJECT_VERSION}.jar favourite-service.jar
EXPOSE 8800
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar favourite-service.jar"]


