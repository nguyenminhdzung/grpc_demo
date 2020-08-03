package com.dmn.assignment.task1.endpoint;

import com.dmn.assignment.task1.exception.CinemaException;
import com.dmn.assignment.task1.service.CinemaService;
import com.dmn.assignment.task1.service.SeatInfo;
import com.google.common.collect.Lists;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
@Slf4j
public class CinemaEndpoint extends CinemaEndpointGrpc.CinemaEndpointImplBase {

    @Autowired
    private CinemaService cinemaService;

    @Override
    public void configRoom(CinemaRoomConfigRequest request, StreamObserver<ResultResponse> responseObserver) {

        log.info("ConfigRoom received input: '{}'.", request.toString());

        ResultResponse.Builder responseBuilder = ResultResponse.newBuilder();

        try {
            cinemaService.configCinemaRoom(request.getName(),
                                           request.getRows(), request.getSeatsPerRow(),
                                           request.getAllowedDistance());

            log.info("ConfigRoom  succeeded.");

            responseBuilder.setSuccess(true);

        } catch (CinemaException ce) {
            log.error("Error happened when configuring room", ce);

            responseBuilder.setSuccess(false);
            responseBuilder.setMessage(ce.getMessage());
        } catch (Exception ex) {
            log.error("Error happened when configuring room", ex);

            responseBuilder.setSuccess(false);
            responseBuilder.setMessage("Unknown error occurred.");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getAvailableSeats(AvailableSeatsRequest request, StreamObserver<AvailableSeatsResponse> responseObserver) {
        log.info("GetAvailableSeats received input: '{}'.", request.toString());

        List<SeatInfo> availableSeats = cinemaService.getAvailableSeats(request.getRoomName(), request.getNeededSeatCount());

        AvailableSeatsResponse response
                = AvailableSeatsResponse.newBuilder()
                                        .addAllSeats(availableSeats.stream().map(s -> Seat.newBuilder()
                                                                                          .setRowNumber(s.getRowNumber())
                                                                                          .setSeatNumber(s.getSeatNumber())
                                                                                          .build())
                                                                            .collect(Collectors.toList()))
                                        .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    public void reserveSeats(ReserveSeatsRequest request, StreamObserver<ResultResponse> responseObserver) {

        log.info("ReserveSeats received input '{}'.", request.toString());

        ResultResponse.Builder responseBuilder = ResultResponse.newBuilder();

        try {
            cinemaService.reserveSeats(request.getRoomName(),
                                       request.getSeatsList().stream()
                                                             .map(s -> SeatInfo.builder()
                                                                               .rowNumber(s.getRowNumber())
                                                                               .seatNumber(s.getSeatNumber())
                                                                               .build())
                                                             .collect(Collectors.toList()));

            log.info("ReserveSeats  succeeded.");

            responseBuilder.setSuccess(true);

        } catch (CinemaException ce) {
            log.error("Error happened when reserving room", ce);

            responseBuilder.setSuccess(false);
            responseBuilder.setMessage(ce.getMessage());
        } catch (Exception ex) {
            log.error("Error happened when reserving room", ex);

            responseBuilder.setSuccess(false);
            responseBuilder.setMessage("Unknown error occurred.");
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }
}
