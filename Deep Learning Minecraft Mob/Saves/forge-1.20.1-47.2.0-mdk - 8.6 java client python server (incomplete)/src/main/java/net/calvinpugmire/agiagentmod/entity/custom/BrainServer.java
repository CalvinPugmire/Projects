package net.calvinpugmire.agiagentmod.entity.custom;

import io.grpc.ServerBuilder;
import io.grpc.Server;

import java.io.IOException;
import java.util.List;

public class BrainServer {
    private int port;
    private long idleTime;
    private BrainService brainService;
    private Server server;

    public BrainServer(int port, long idleTime){
        System.out.println("making brain 2.1");
        this.port = port;
        System.out.println("making brain 2.2");
        this.idleTime = idleTime;
        System.out.println("making brain 2.3");
    }

    public void start() throws IOException, InterruptedException {
        brainService = new BrainService();

        server = ServerBuilder.forPort(port).addService(brainService).build();

        server.start();
        server.awaitTermination();
    }

    public String[] updateInfo(List<String> newSightInfo, List<String> newHearInfo, List<Float> newRewardInfo) {
        return brainService.updateInfo(newSightInfo, newHearInfo, newRewardInfo);
    }
}