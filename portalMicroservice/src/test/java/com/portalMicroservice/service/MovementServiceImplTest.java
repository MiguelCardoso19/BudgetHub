package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.impl.MovementServiceImpl;
import com.portalMicroservice.util.PageableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.*;

import static com.portalMicroservice.enumerator.MovementStatus.ACCEPTED;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MovementServiceImplTest {

    @InjectMocks
    private MovementServiceImpl movementService;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private PageableUtils pageableUtils;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<MovementDTO>> pendingRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests;

    private UUID correlationId;
    private UUID id;
    private MovementDTO movementDTO;
    private Pageable pageable;
    private CustomPageableDTO customPageableDTO;
    private long timeoutDuration;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
        movementDTO = new MovementDTO();
        movementDTO.setCorrelationId(correlationId);
        movementDTO.setId(id);

        timeoutDuration = 5L;

        pageable = PageRequest.of(0, 10);
        customPageableDTO = pageableUtils.convertToCustomPageable(pageable);

        Field timeoutField = MovementServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(movementService, timeoutDuration);

        Field pendingRequestsField = MovementServiceImpl.class.getDeclaredField("pendingRequests");
        pendingRequestsField.setAccessible(true);
        pendingRequests = (ConcurrentHashMap<UUID, CompletableFuture<MovementDTO>>) pendingRequestsField.get(movementService);

        Field pendingPageRequestsField = MovementServiceImpl.class.getDeclaredField("pendingPageRequests");
        pendingPageRequestsField.setAccessible(true);
        pendingPageRequests = (ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>>) pendingPageRequestsField.get(movementService);
    }

    @Test
    public void create_Success() throws Exception {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        future.complete(movementDTO);
        pendingRequests.put(movementDTO.getCorrelationId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.create(movementDTO)).thenReturn(future.get());

        assertEquals(movementDTO, movementServiceMock.create(movementDTO));
        verify(movementServiceMock).create(movementDTO);
    }

    @Test
    void create_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getCorrelationId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.create(movementDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.create(movementDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void update_Success() throws Exception {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        future.complete(movementDTO);
        pendingRequests.put(movementDTO.getId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.update(movementDTO)).thenReturn(future.get());

        assertEquals(movementDTO, movementServiceMock.update(movementDTO));
    }

    @Test
    void update_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(movementDTO.getId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.update(movementDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.update(movementDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void delete_Success() throws Exception {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(movementDTO);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        doNothing().when(movementServiceMock).delete(eq(id));

        movementServiceMock.delete(id);

        verify(movementServiceMock).delete(eq(id));
    }

    @Test
    void delete_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        doNothing().when(movementServiceMock).delete(eq(id));

        assertThatThrownBy(() -> movementService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getById_Success() throws Exception {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(movementDTO);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getById(eq(id))).thenReturn(future.get());

        assertEquals(movementDTO, movementServiceMock.getById(id));
    }

    @Test
    void getById_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getById(eq(id))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.getById(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getAll_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getAll(pageable)).thenReturn(future.get());

        assertEquals(expectedPage, movementServiceMock.getAll(pageable));
    }

    @Test
    void getAll_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getAll(pageable)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.getAll(pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getByBudgetType_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getByBudgetType(eq(id), eq(pageable))).thenReturn(future.get());

        assertEquals(expectedPage, movementServiceMock.getByBudgetType(id, pageable));
    }

    @Test
    void getByBudgetType_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getByBudgetType(eq(id), eq(pageable))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.getByBudgetType(id, pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void exportMovementsReport_Success() throws Exception {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        future.complete(movementDTO);

        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        doNothing().when(movementServiceMock).exportMovementsReport(eq(startDate), eq(endDate), eq(ACCEPTED), eq("test@test.com"));

        movementServiceMock.exportMovementsReport(startDate, endDate, ACCEPTED, "test@test.com");

        verify(movementServiceMock).exportMovementsReport(eq(startDate), eq(endDate), eq(ACCEPTED), eq("test@test.com"));
    }

    @Test
    public void getByBudgetSubtype_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getByBudgetSubtype(eq(id), eq(pageable))).thenReturn(future.get());

        assertEquals(expectedPage, movementServiceMock.getByBudgetSubtype(id, pageable));
    }

    @Test
    void getByBudgetSubtype_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        MovementServiceImpl movementServiceMock = mock(MovementServiceImpl.class);
        when(movementServiceMock.getByBudgetSubtype(eq(id), eq(pageable))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> movementService.getByBudgetSubtype(id, pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void removePendingRequestById_Success() {
        CompletableFuture<MovementDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        assertThat(movementService.removePendingRequestById(correlationId, id)).isNotNull();
        assertThat(pendingRequests).doesNotContainKey(correlationId);
    }

    @Test
    public void removePendingRequestById_NotFound() {
        assertThat(movementService.removePendingRequestById(correlationId, id)).isNull();
    }

    @Test
    public void removePendingPageRequestById_Success() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(correlationId, future);

        assertThat(movementService.removePendingPageRequestById(correlationId)).isNotNull();
        assertThat(pendingPageRequests).doesNotContainKey(correlationId);
    }

    @Test
    public void removePendingPageRequestById_NotFound() {
        assertThat(movementService.removePendingPageRequestById(correlationId)).isNull();
    }
}