FROM maven:3.8.3-openjdk-17 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip=true
EXPOSE 8080
ENV DB_SERVER=localhost DB_PORT=3306 DB_NAME=fasteatspagamentodb
ENV DB_USER=fiap56 DB_PASSWORD=fiapsoat1grupo56
ENV DB_ROOT_PASSWORD=fiapsoat1grupo56
ENV MERCADO_PAGO_EMAIL_EMPRESA=pagamento@fasteats-fiap.com.br
ENV MERCADO_PAGO_CREDENCIAL=TEST-2087963774082813-080820-ee2b9b80edbdecf3ea8453bb8c088bc7-64946408
ENV MERCADO_PAGO_USERID=64946408
ENV MERCADO_PAGO_TIPO_PAGAMENTO=pix
ENV URL_PEDIDO_SERVICE=http://localhost:8082
ENV URL_COZINHA_PEDIDO_SERVICE=http://localhost:8083
ENTRYPOINT ["java", "-jar","/home/app/target/api-pagamento-2.0.0-SNAPSHOT.jar"]