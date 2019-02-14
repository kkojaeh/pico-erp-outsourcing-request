package pico.erp.outsourcing.request;

import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import pico.erp.audit.AuditApi;
import pico.erp.audit.AuditConfiguration;
import pico.erp.bom.BomApi;
import pico.erp.item.ItemApi;
import pico.erp.process.ProcessApi;
import pico.erp.project.ProjectApi;
import pico.erp.outsourcing.request.OutsourcingRequestApi.Roles;
import pico.erp.shared.ApplicationId;
import pico.erp.shared.ApplicationStarter;
import pico.erp.shared.Public;
import pico.erp.shared.SpringBootConfigs;
import pico.erp.shared.data.Role;
import pico.erp.shared.impl.ApplicationImpl;
import pico.erp.user.UserApi;
import pico.erp.warehouse.WarehouseApi;

@Slf4j
@SpringBootConfigs
public class OutsourcingRequestApplication implements ApplicationStarter {

  public static final String CONFIG_NAME = "outsourcing-request/application";

  public static final Properties DEFAULT_PROPERTIES = new Properties();

  static {
    DEFAULT_PROPERTIES.put("spring.config.name", CONFIG_NAME);
  }

  public static SpringApplication application() {
    return new SpringApplicationBuilder(OutsourcingRequestApplication.class)
      .properties(DEFAULT_PROPERTIES)
      .web(false)
      .build();
  }

  public static void main(String[] args) {
    application().run(args);
  }

  @Override
  public Set<ApplicationId> getDependencies() {
    return Stream.of(
      UserApi.ID,
      ItemApi.ID,
      AuditApi.ID,
      ProjectApi.ID,
      WarehouseApi.ID,
      ProcessApi.ID,
      BomApi.ID
    ).collect(Collectors.toSet());
  }

  @Override
  public ApplicationId getId() {
    return OutsourcingRequestApi.ID;
  }

  @Override
  public boolean isWeb() {
    return false;
  }

  @Bean
  @Public
  public Role outsourcingRequester() {
    return Roles.OUTSOURCING_REQUESTER;
  }

  @Bean
  @Public
  public Role outsourcingRequestManager() {
    return Roles.OUTSOURCING_REQUEST_MANAGER;
  }

  @Bean
  @Public
  public Role outsourcingRequestAccepter() {
    return Roles.OUTSOURCING_REQUEST_ACCEPTER;
  }

  @Override
  public pico.erp.shared.Application start(String... args) {
    return new ApplicationImpl(application().run(args));
  }

}
