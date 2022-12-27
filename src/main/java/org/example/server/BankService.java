package org.example.server;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.example.models.Balance;
import org.example.models.Money;
import org.example.models.BalanceCheckRequest;
import org.example.models.BankServiceGrpc;
import org.example.models.WithdrawRequest;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    @Override
    public void getBalance(BalanceCheckRequest request, StreamObserver<Balance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = Balance.newBuilder()
                .setAmount(BankData.getBalance(accountNumber))
                .build();
        responseObserver.onNext(balance);
        responseObserver.onCompleted();
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<org.example.models.Money> responseObserver) {
        int accountNumber = request.getAccountNumber();
        int amount = request.getAmount();
        int balance = BankData.getBalance(accountNumber);

        if (balance < amount) {
            Status status = Status.FAILED_PRECONDITION.withDescription("Not enough money.");
            responseObserver.onError(status.asRuntimeException());
            return;
        }

        for (int i = 0; i < (amount / 10); i++) {
            Money money = Money.newBuilder()
                    .setValue(10)
                    .build();
            responseObserver.onNext(money);
            BankData.deductBalance(accountNumber, 10);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        responseObserver.onCompleted();
    }
}
