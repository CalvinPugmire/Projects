package net.calvinpugmire.agiagentmod.entity.custom;

import io.grpc.stub.StreamObserver;
import net.calvinpugmire.agiagentmod.entity.custom.Brain;
import net.calvinpugmire.agiagentmod.entity.custom.BrainServiceGrpc;

import java.util.List;

public class BrainService extends BrainServiceGrpc.BrainServiceImplBase {
    private List<String> sightInfo;
    private List<String> hearInfo;
    private List<Float> rewardInfo;
    private String[] actionInfo;

    public String[] updateInfo(List<String> newSightInfo, List<String> newHearInfo, List<Float> newRewardInfo) {
        sightInfo = newSightInfo;
        hearInfo = newHearInfo;
        rewardInfo = newRewardInfo;

        return actionInfo;
    }

    @Override
    public void getInput(Brain.InputRequest req, StreamObserver<Brain.InputSend> res){
        Brain.InputSend getInput = Brain.InputSend.newBuilder()
                .addAllNewSightInfo(sightInfo)
                .addAllNewHearInfo(hearInfo)
                .addAllNewRewardInfo(rewardInfo)
                .build();
        res.onNext(getInput);
        res.onCompleted();
    }

    @Override
    public void sendAction(Brain.ActionReceive req, StreamObserver<Brain.ActionResponse> res){
        actionInfo = req.getActionInfoList().toArray(new String[0]);

        Brain.ActionResponse sendAction = Brain.ActionResponse.newBuilder()
                .setMessage("success")
                .build();
        res.onNext(sendAction);
        res.onCompleted();
    }
}