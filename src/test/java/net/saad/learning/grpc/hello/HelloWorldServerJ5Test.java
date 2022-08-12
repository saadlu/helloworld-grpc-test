package net.saad.learning.grpc.hello;

import java.time.Duration;

import com.asarkar.grpc.test.GrpcCleanupExtension;
import com.asarkar.grpc.test.Resources;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(GrpcCleanupExtension.class)
class HelloWorldServerJ5Test {
    Resources resources;

    @Test
    void testSuccessful() throws Exception {
        String serverName = InProcessServerBuilder.generateName();

        Server server = InProcessServerBuilder
                .forName(serverName).directExecutor().addService(new HelloWorldServer.GreeterImpl()).build().start();

        resources.register(server, Duration.ofSeconds(5));

        ManagedChannel channel = InProcessChannelBuilder.forName(serverName).directExecutor().build();
        GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(channel);

        resources.register(channel, Duration.ofSeconds(5));

        HelloReply reply =
                blockingStub.sayHello(HelloRequest.newBuilder().setName( "test name").build());

        Assertions.assertEquals("Hello test name", reply.getMessage());

    }
}
