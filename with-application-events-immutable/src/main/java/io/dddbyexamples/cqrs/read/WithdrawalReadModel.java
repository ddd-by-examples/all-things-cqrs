package io.dddbyexamples.cqrs.read;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawalReadModel {
    private UUID cardId;
    private BigDecimal amount;

}
