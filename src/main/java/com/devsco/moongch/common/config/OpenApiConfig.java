package com.devsco.moongch.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static java.util.Collections.emptyList;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {

  private final SpringdocProperties props;

  @Bean
  public OpenAPI openApi() {
    return new OpenAPI()
      .info(apiInfo())
      .servers(props.getServers());
  }

  private Info apiInfo() {
    return new Info()
      .title("에브리틴 API 문서") // API 의 제목
      .version("1.0.0"); // API 의 버전
  }

  @Configuration
  @ConfigurationProperties("springdoc")
  @Setter
  static class SpringdocProperties {
    private List<String> servers;

    public List<Server> getServers() {
      return null == servers
        ? emptyList()
        : servers.stream().map(server -> new Server().url(server)).toList();
    }
  }
}
