package io.dddbyexamples.cqrs.sink;

import io.dddbyexamples.cqrs.model.Withdrawal;
import io.dddbyexamples.cqrs.persistence.WithdrawalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
class ReadModelUpdater {

	private final WithdrawalRepository withdrawalRepository;

	ReadModelUpdater(WithdrawalRepository withdrawalRepository) {
		this.withdrawalRepository = withdrawalRepository;
	}

	@StreamListener(Sink.INPUT)
	public void handle(Envelope message) {
		if(message.isUpdate()) {
			saveWithdrawalFrom(message);
		}
	}

	private void saveWithdrawalFrom(Envelope message) {
		String cardId = message.getPayload().getBefore().getId();
		BigDecimal withdrawalAmount
				= balanceAfter(message).subtract(balanceBefore(message));
		withdrawalRepository.save(new Withdrawal(withdrawalAmount, cardId));
	}

	private BigDecimal balanceAfter(Envelope message) {
		return message.getPayload().getAfter().getUsed_limit();
	}

	private BigDecimal balanceBefore(Envelope message) {
		return message.getPayload().getBefore().getUsed_limit();
	}


}
