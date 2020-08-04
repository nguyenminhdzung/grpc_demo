package com.dmn.assignment.task1.endpoint;

import com.dmn.assignment.task1.configuration.ServicesUnitTestConfiguration;
import com.dmn.assignment.task1.exception.CinemaException;
import com.dmn.assignment.task1.service.CinemaService;
import com.dmn.assignment.task1.service.SeatInfo;
import com.google.common.collect.Lists;
import io.grpc.internal.testing.StreamRecorder;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@SpringJUnitConfig(classes = { ServicesUnitTestConfiguration.class })
public class CinemaEndpointTest {
    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private CinemaEndpoint cinemaEndpoint;

    /** ConfigRoom **/
    @Test
    public void testConfigRoomSuccess() throws Exception {
        CinemaRoomConfigRequest request = CinemaRoomConfigRequest.newBuilder()
                                                                 .setName("T1")
                                                                 .setRows(30)
                                                                 .setSeatsPerRow(10)
                                                                 .setAllowedDistance(6)
                                                                 .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        doNothing().when(cinemaService)
                   .configCinemaRoom(anyString(), anyInt(), anyInt(), anyInt());
        cinemaEndpoint.configRoom(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(true, results.get(0).getSuccess());
        assertTrue(StringUtils.isBlank(results.get(0).getMessage()));
    }

    @Test
    public void testConfigRoomFailedWithUnknowError() throws Exception {
        CinemaRoomConfigRequest request = CinemaRoomConfigRequest.newBuilder()
                                                                .setName("T1")
                                                                .setRows(30)
                                                                .setSeatsPerRow(10)
                                                                .setAllowedDistance(6)
                                                                .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        doThrow(new RuntimeException())
                .when(cinemaService)
                .configCinemaRoom(anyString(), anyInt(), anyInt(), anyInt());
        cinemaEndpoint.configRoom(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(false, results.get(0).getSuccess());
        assertEquals("Unknown error occurred.", results.get(0).getMessage());
    }

    @Test
    public void testConfigRoomFailedWithSpecificError() throws Exception {
        CinemaRoomConfigRequest request = CinemaRoomConfigRequest.newBuilder()
                                                                .setName("T1")
                                                                .setRows(30)
                                                                .setSeatsPerRow(10)
                                                                .setAllowedDistance(6)
                                                                .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        String errorMessage = "Failed to config room when test.";
        doThrow(new CinemaException(errorMessage))
                .when(cinemaService)
                .configCinemaRoom(anyString(), anyInt(), anyInt(), anyInt());
        cinemaEndpoint.configRoom(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(false, results.get(0).getSuccess());
        assertEquals(errorMessage, results.get(0).getMessage());
    }

    /** GetAvailableSeats **/
    @Test
    public void testGetAvailableSeatsSuccess() throws Exception {
        AvailableSeatsRequest request = AvailableSeatsRequest.newBuilder()
                                                             .setRoomName("T1")
                                                             .setNeededSeatCount(3)
                                                             .build();
        StreamRecorder<AvailableSeatsResponse> responseObserver = StreamRecorder.create();

        when(cinemaService.getAvailableSeats(anyString(), anyInt()))
                .thenReturn(Lists.newArrayList(SeatInfo.builder()
                                                       .rowNumber(1).seatNumber(2)
                                                       .build(),
                                               SeatInfo.builder()
                                                       .rowNumber(31).seatNumber(4)
                                                       .build(),
                                               SeatInfo.builder()
                                                       .rowNumber(31).seatNumber(4)
                                                       .build(),
                                               SeatInfo.builder()
                                                       .rowNumber(31).seatNumber(4)
                                                       .build()));
        cinemaEndpoint.getAvailableSeats(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<AvailableSeatsResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(4, results.get(0).getSeatsList().size());
    }

    @Test
    public void testGetAvailableSeatsFailed() throws Exception {
        AvailableSeatsRequest request = AvailableSeatsRequest.newBuilder()
                                                             .setNeededSeatCount(3)
                                                             .build();
        StreamRecorder<AvailableSeatsResponse> responseObserver = StreamRecorder.create();

        when(cinemaService.getAvailableSeats(anyString(), anyInt()))
                .thenThrow(new CinemaException("Room name must be not empty"));
        cinemaEndpoint.getAvailableSeats(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNotNull(responseObserver.getError());
        assertEquals("Room name must be not empty", responseObserver.getError().getMessage());
    }

    /** /** GetAvailableSeats **/
    @Test
    public void testReserveSeatsSuccess() throws Exception {
        ReserveSeatsRequest request = ReserveSeatsRequest.newBuilder()
                                                         .setRoomName("T1")
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())

                                                         .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        doNothing().when(cinemaService)
                .reserveSeats(anyString(), anyList());
        cinemaEndpoint.reserveSeats(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertTrue(results.get(0).getSuccess());
        assertTrue(StringUtils.isBlank(results.get(0).getMessage()));
    }

    @Test
    public void testReserveSeatsFailedWithUnknowError() throws Exception {
        ReserveSeatsRequest request = ReserveSeatsRequest.newBuilder()
                                                         .setRoomName("T1")
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())
                                                        .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        doThrow(new RuntimeException())
                .when(cinemaService)
                .reserveSeats(anyString(), anyList());
        cinemaEndpoint.reserveSeats(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(false, results.get(0).getSuccess());
        assertEquals("Unknown error occurred.", results.get(0).getMessage());
    }

    @Test
    public void testReserveSeatsFailedWithSpecificError() throws Exception {
        ReserveSeatsRequest request = ReserveSeatsRequest.newBuilder()
                                                         .setRoomName("T2")
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())
                                                         .addSeats(Seat.newBuilder()
                                                                       .setRowNumber(1).setSeatNumber(2)
                                                                       .build())
                                                        .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();

        doThrow(new CinemaException("Room doesn't exist."))
                .when(cinemaService)
                .reserveSeats(anyString(), anyList());
        cinemaEndpoint.reserveSeats(request, responseObserver);

        if (!responseObserver.awaitCompletion(5, TimeUnit.SECONDS))
            fail("Failed to call method in time.");

        assertNull(responseObserver.getError());

        List<ResultResponse> results = responseObserver.getValues();
        assertEquals(1, results.size());
        assertEquals(false, results.get(0).getSuccess());
        assertEquals("Room doesn't exist.", results.get(0).getMessage());
    }
}
