package org.example;

import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.example.models.BankServiceGrpc;
import org.example.models.BalanceCheckRequest;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankClientTest {

    private BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup() {
        var managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();
        blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);

    }

    @Test
    public void balanceTest() {
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(2)
                .build();
        var balance = blockingStub.getBalance(request);
        System.out.println(balance.getAmount());
    }
}
