package pico.erp.outsourcing.request;

import kkojaeh.spring.boot.component.Give;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.user.group.GroupData;

@Give
@Data
@Configuration
@ConfigurationProperties("outsourcing-request")
public class OutsourcingRequestPropertiesImpl implements OutsourcingRequestProperties {

  GroupData accepterGroup;

}
