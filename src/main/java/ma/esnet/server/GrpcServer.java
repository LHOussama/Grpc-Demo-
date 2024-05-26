package ma.esnet.server;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import ma.esnet.service.BankGpcService;

import java.io.IOException;

public class GrpcServer {
    public static void main(String[] args) throws IOException, InterruptedException {
      Server server= ServerBuilder.forPort(5555).addService(new BankGpcService()).build();
        server.start();
        server.awaitTermination();
    }
}
