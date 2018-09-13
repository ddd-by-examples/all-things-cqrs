package io.dddbyexamples.cqrs.persistence;

import io.dddbyexamples.cqrs.ui.WithdrawalDto;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface WithdrawalsRepository extends ReactiveCrudRepository<WithdrawalDto, String> {

    Flux<WithdrawalDto> findAllByCard(String card);
}
