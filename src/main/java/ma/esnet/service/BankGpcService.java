package ma.esnet.service;
import io.grpc.stub.StreamObserver;
import ma.esnet.stubs.Bank;
import ma.esnet.stubs.BankServiceGrpc;

import java.util.Timer;
import java.util.TimerTask;

public class BankGpcService extends BankServiceGrpc.BankServiceImplBase {
    @Override
    public void convert(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String currencyFrom=request.getCurrencyFrom();
        String currencyTo=request.getCurrencyTo();
        double amount=request.getAmount();
        Bank.ConvertCurrencyResponse convertCurrencyResponse= Bank.ConvertCurrencyResponse.newBuilder()
                .setCurrencyFrom(currencyFrom)
                .setCurrencyTo(currencyTo)
                .setAmount(amount)
                .setResult(amount*12.3)
                .build();
        System.out.println("tester"+convertCurrencyResponse.getCurrencyTo());
        responseObserver.onNext(convertCurrencyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> fullCurrencyStream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertCurrencyRequest>() {
            @Override
            public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
                Bank.ConvertCurrencyResponse response= Bank.ConvertCurrencyResponse.newBuilder().setResult(convertCurrencyRequest.getAmount()*2).build();
                responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                System.out.println("END");
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void getCurrencyStream(Bank.ConvertCurrencyRequest request, StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        String currencyFrom=request.getCurrencyFrom();
        String currencyTo=request.getCurrencyTo();
        double amount=request.getAmount();
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int compteur=0;
            @Override
            public void run() {
                Bank.ConvertCurrencyResponse convertCurrencyResponse= Bank.ConvertCurrencyResponse.newBuilder()
                        .setCurrencyFrom(currencyFrom)
                        .setCurrencyTo(currencyTo)
                        .setAmount(amount)
                        .setResult(amount*(Math.random()*100))
                        .build();
                responseObserver.onNext(convertCurrencyResponse);
                ++compteur;
                if(compteur == 20){
                    responseObserver.onCompleted();
                    timer.cancel();
                }

            }
        }, 1000, 1000);
    }

    @Override
    public StreamObserver<Bank.ConvertCurrencyRequest> performStream(StreamObserver<Bank.ConvertCurrencyResponse> responseObserver) {
        return new StreamObserver<Bank.ConvertCurrencyRequest>() {
            double sum=0;
            @Override
            public void onNext(Bank.ConvertCurrencyRequest convertCurrencyRequest) {
                sum+=convertCurrencyRequest.getAmount();
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
                Bank.ConvertCurrencyResponse response = Bank.ConvertCurrencyResponse.newBuilder().setResult(sum*2).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        };
    }
}
