package me.jeff.ignitepoc.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import me.jeff.ignitepoc.grpc.user.dto.AddUserRequest;
import me.jeff.ignitepoc.grpc.user.dto.SearchUserRequest;
import me.jeff.ignitepoc.grpc.user.dto.UserResponse;
import me.jeff.ignitepoc.grpc.user.service.UserGrpc;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Slf4j
public class UserServiceGrpcImpl extends UserGrpc.UserImplBase {

    @Override
    public void add(AddUserRequest request, StreamObserver<UserResponse> responseObserver) {
        log.info("start add");
        UserResponse reply = UserResponse.newBuilder()
                .setId(1)
                .setName(request.getName())
                .setAge(12)
                .setAddress("Beijing").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
        log.info("end add");
    }

    @Override
    public void list(SearchUserRequest request, StreamObserver<UserResponse> responseObserver) {
       /* LambdaQueryWrapper<UserDemo> lq = new LambdaQueryWrapper<UserDemo>();
        if(StringUtils.hasText(request.getName())) {
            lq.eq(UserDemo::getName, request.getName());
        }
        List<UserDemo> list = userDemoMapper.selectList(lq);
        list.stream().forEach(c -> {
            UserResponse ur = UserResponse.newBuilder().setAddress(c.getAddress()).setAge(c.getAge()).setId(c.getId())
                    .setName(c.getName()).build();
            responseObserver.onNext(ur);
        });
        responseObserver.onCompleted();*/
    }
}
