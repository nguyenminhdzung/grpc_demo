package com.dmn.assignment.task1.endpoint;

import com.dmn.assignment.task1.configuration.ServicesUnitTestConfiguration;
import com.dmn.assignment.task1.exception.CinemaException;
import com.dmn.assignment.task1.service.CinemaService;
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

    @Test
    public void testConfigRoomSuccess() throws Exception {
        CinemaRoomConfigRequest request = CinemaRoomConfigRequest.newBuilder()
                                                                 .setName("T1")
                                                                 .setRows(30)
                                                                 .setSeatsPerRow(10)
                                                                 .setAllowedDistance(6)
                                                                 .build();
        StreamRecorder<ResultResponse> responseObserver = StreamRecorder.create();
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
}
