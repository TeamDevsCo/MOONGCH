package com.devsco.moongch.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ForwardedHeaderFilter;

@Configuration
public class WebConfig {
  @Bean
  public FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter() {
    FilterRegistrationBean<ForwardedHeaderFilter> filterRegistrationBean =
      new FilterRegistrationBean<>(new ForwardedHeaderFilter());
    filterRegistrationBean.setOrder(0);
    return filterRegistrationBean;
  }
}
