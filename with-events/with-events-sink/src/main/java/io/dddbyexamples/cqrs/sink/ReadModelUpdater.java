package io.dddbyexamples.cqrs.sink;

import io.dddbyexamples.cqrs.persistence.WithdrawalsRepository;
import io.dddbyexamples.cqrs.ui.WithdrawalDto;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
class ReadModelUpdater {

	private final WithdrawalsRepository withdrawalsRepository;

	ReadModelUpdater(WithdrawalsRepository withdrawalsRepository) {
		this.withdrawalsRepository = withdrawalsRepository;
	}

	@StreamListener(target = Sink.INPUT, condition = "headers['type'] == 'card-withdrawn'")
	public void handle(CardWithdrawn cardWithdrawn) {
        withdrawalsRepository
				.save(new WithdrawalDto(UUID.randomUUID().toString(), cardWithdrawn.getCardNo().toString(), cardWithdrawn.getAmount())).subscribe();
	}
}
