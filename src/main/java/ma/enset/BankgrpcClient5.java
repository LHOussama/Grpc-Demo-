package ma.enset;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import ma.enset.stubs.Bank;
import ma.enset.stubs.BankServiceGrpc;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
public class BankgrpcClient5 {
    public static void main(String[] args) throws IOException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 5555).usePlaintext().build();
        BankServiceGrpc.BankServiceStub asyncstub = BankServiceGrpc.newStub(managedChannel);
        StreamObserver<Bank.ConvertCurrencyRequest> fullCurrencyStream = asyncstub.fullCurrencyStream(new StreamObserver<Bank.ConvertCurrencyResponse>() {
            @Override
            public void onNext(Bank.ConvertCurrencyResponse convertCurrencyResponse) {
                System.out.println("******");
                System.out.println(convertCurrencyResponse);
                System.out.println("*********");
            }
            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
            @Override
            public void onCompleted() {
                System.out.println("END");
            }
        });
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            int count=1;
            @Override
            public void run() {
            Bank.ConvertCurrencyRequest request= Bank.ConvertCurrencyRequest.newBuilder().setAmount(1+count).build();
           fullCurrencyStream.onNext(request);
           count++;
           if(count==10){
                    fullCurrencyStream.onCompleted();
                    this.cancel();
           }
            }
        },1000,1000);
        System.out.println("*************");
        System.in.read();
    }
}
