package pico.erp.outsourcing.request;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pico.erp.shared.Public;
import pico.erp.user.group.GroupData;

@Public
@Data
@Configuration
@ConfigurationProperties("outsourcing-request")
public class OutsourcingRequestPropertiesImpl implements OutsourcingRequestProperties {

  GroupData accepterGroup;

}
