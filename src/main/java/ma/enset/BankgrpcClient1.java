package ma.enset;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;

public class BankgrpcClient1 {
    public static void main(String[] args) {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5555).usePlaintext().build();
        BankServiceGrpc.BankServiceBlockingStub blockingStub = BankServiceGrpc.newBlockingStub(managedChannel);
        Bank.ConvertCurrencyRequest request = Bank.ConvertCurrencyRequest.newBuilder().setAmount(100).setCurrencyFrom("MAD").setCurrencyTo("EURO").build();
        Bank.ConvertCurrencyResponse currencyResponse = blockingStub.convert(request);
        System.out.println(currencyResponse);
    }

}
