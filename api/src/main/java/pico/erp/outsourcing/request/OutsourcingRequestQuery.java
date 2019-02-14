package pico.erp.outsourcing.request;

import javax.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutsourcingRequestQuery {

  Page<OutsourcingRequestView> retrieve(@NotNull OutsourcingRequestView.Filter filter,
    @NotNull Pageable pageable);

  Page<OutsourcingRequestAwaitOrderView> retrieve(
    @NotNull OutsourcingRequestAwaitOrderView.Filter filter,
    @NotNull Pageable pageable);

  Page<OutsourcingRequestAwaitAcceptView> retrieve(
    @NotNull OutsourcingRequestAwaitAcceptView.Filter filter, @NotNull Pageable pageable);


}
