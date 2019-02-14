package pico.erp.outsourcing.request;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class OutsourcingRequestCodeGeneratorImpl implements OutsourcingRequestCodeGenerator {

  @Lazy
  @Autowired
  private OutsourcingRequestRepository outsourcingRequestRepository;

  @Override
  public OutsourcingRequestCode generate(OutsourcingRequest outsourcingRequest) {
    val now = OffsetDateTime.now();
    val begin = now.with(LocalTime.MIN);
    val end = now.with(LocalTime.MAX);
    val count = outsourcingRequestRepository.countCreatedBetween(begin, end);
    val date =
      Integer.toString(now.getYear() - 1900, 36) + Integer.toString(now.getMonthValue(), 16)
        + Integer.toString(now.getDayOfMonth(), 36);
    val code = String.format("%s-%04d", date, count + 1).toUpperCase();
    return OutsourcingRequestCode.from(code);
  }
}
