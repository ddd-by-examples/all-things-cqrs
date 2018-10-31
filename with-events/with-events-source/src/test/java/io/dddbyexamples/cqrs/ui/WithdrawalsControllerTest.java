package io.dddbyexamples.cqrs.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dddbyexamples.cqrs.application.WithdrawalProcess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WithdrawalsController.class)
public class WithdrawalsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private WithdrawalProcess withdrawalProcess;

    @Test
    public void shouldWithdrawnMoney() throws Exception {
        //Arrange
        WithdrawalCommand request = new WithdrawalCommand();
        request.setAmount(BigDecimal.ONE);
        UUID cardUUID = UUID.randomUUID();
        request.setCard(cardUUID);

        //Act
        ResultActions result = mockMvc.perform(post("/withdrawals")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)));

        //Assert
        verify(withdrawalProcess).withdraw(cardUUID, BigDecimal.ONE);
        result.andExpect(status().isOk());
    }
}
