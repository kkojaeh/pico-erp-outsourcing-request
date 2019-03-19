package pico.erp.outsourcing.request;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
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
    val begin = now.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
    val end = now.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);
    val count = outsourcingRequestRepository.countCreatedBetween(begin, end);
    val code = String
      .format("OR%03d%02d-%04d", now.getYear() % 1000, now.getMonthValue(), count + 1)
      .toUpperCase();
    return OutsourcingRequestCode.from(code);
  }
}
