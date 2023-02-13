package com.github.tenxfuturetechnologies.kafkaconnecticeberg.containers;

import static com.github.tenxfuturetechnologies.kafkaconnecticeberg.util.Constants.SCHEMA_REGISTRY_IMAGE;
import static java.lang.String.format;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;

public class SchemaRegistryContainer extends GenericContainer<SchemaRegistryContainer> {

  private static final int SCHEMA_REGISTRY_INTERNAL_PORT = 8081;
  private static final String NETWORK_ALIAS = "schema-registry";

  public SchemaRegistryContainer() {
    super(SCHEMA_REGISTRY_IMAGE);
    addEnv("SCHEMA_REGISTRY_HOST_NAME", "localhost");
    withExposedPorts(SCHEMA_REGISTRY_INTERNAL_PORT);
    withNetworkAliases(NETWORK_ALIAS);
    waitingFor(Wait.forHttp("/subjects"));
  }

  public SchemaRegistryContainer withKafkaBoostrap(String kafkaBoostrap) {
    return withEnv("SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS", kafkaBoostrap);
  }

  public String getInternalUrl() {
    return format("http://%s:%d", NETWORK_ALIAS, SCHEMA_REGISTRY_INTERNAL_PORT);
  }

  public String getUrl() {
    return format("http://%s:%d", this.getHost(),
        this.getMappedPort(SCHEMA_REGISTRY_INTERNAL_PORT));
  }
}