package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.dto.budget.SupplierDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.impl.SupplierServiceImpl;
import com.portalMicroservice.util.PageableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class SupplierServiceImplTest {

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Mock
    private KafkaTemplate<String, SupplierDTO> kafkaSupplierTemplate;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private KafkaTemplate<String, CustomPageableDTO> kafkaPageableTemplate;

    @Mock
    private PageableUtils pageableUtils;

    private UUID correlationId;

    private UUID id;

    private SupplierDTO supplierDTO;

    private Pageable pageable;

    private CustomPageableDTO customPageableDTO;

    private long timeoutDuration;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<SupplierDTO>> pendingRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
        supplierDTO = new SupplierDTO();
        supplierDTO.setCorrelationId(correlationId);
        supplierDTO.setId(id);

        timeoutDuration = 5L;

        pageable = PageRequest.of(0, 10);
        customPageableDTO = pageableUtils.convertToCustomPageable(pageable);

        Field timeoutField = SupplierServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(supplierService, timeoutDuration);

        Field pendingRequestsField = SupplierServiceImpl.class.getDeclaredField("pendingRequests");
        pendingRequestsField.setAccessible(true);
        pendingRequests = (ConcurrentHashMap<UUID, CompletableFuture<SupplierDTO>>) pendingRequestsField.get(supplierService);

        Field pendingPageRequestsField = SupplierServiceImpl.class.getDeclaredField("pendingPageRequests");
        pendingPageRequestsField.setAccessible(true);
        pendingPageRequests = (ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>>) pendingPageRequestsField.get(supplierService);
    }

    @Test
    public void create_Success() throws Exception {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        future.complete(supplierDTO);
        pendingRequests.put(supplierDTO.getCorrelationId(), future);

        SupplierServiceImpl supplierServiceMock = mock(SupplierServiceImpl.class);
        when(supplierServiceMock.create(supplierDTO)).thenReturn(future.get());

        assertEquals(supplierDTO, supplierServiceMock.create(supplierDTO));
        verify(supplierServiceMock).create(supplierDTO);
    }

    @Test
    void create_NoResponse() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(supplierDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> supplierService.create(supplierDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void update_Success() throws Exception {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        future.complete(supplierDTO);
        pendingRequests.put(supplierDTO.getId(), future);

        SupplierServiceImpl supplierServiceMock = mock(SupplierServiceImpl.class);
        when(supplierServiceMock.update(supplierDTO)).thenReturn(future.get());

        assertEquals(supplierDTO, supplierServiceMock.update(supplierDTO));
    }

    @Test
    void update_NoResponse() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(supplierDTO.getId(), future);

        assertThatThrownBy(() -> supplierService.update(supplierDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void delete_Success() throws Exception {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(supplierDTO);

        SupplierServiceImpl supplierServiceMock = mock(SupplierServiceImpl.class);
        doNothing().when(supplierServiceMock).delete(eq(id));

        supplierServiceMock.delete(id);

        verify(supplierServiceMock).delete(eq(id));
    }

    @Test
    void delete_NoResponse() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        assertThatThrownBy(() -> supplierService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getById_Success() throws Exception {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(supplierDTO);

        SupplierServiceImpl supplierServiceMock = mock(SupplierServiceImpl.class);
        when(supplierServiceMock.getById(eq(id))).thenReturn(future.get());

        assertEquals(supplierDTO, supplierServiceMock.getById(id));
    }

    @Test
    void getById_NoResponse() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        assertThatThrownBy(() -> supplierService.getById(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getAll_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        SupplierServiceImpl supplierServiceMock = mock(SupplierServiceImpl.class);
        when(supplierServiceMock.getAll(pageable)).thenReturn(future.get());

        assertEquals(expectedPage, supplierServiceMock.getAll(pageable));
    }

    @Test
    void getAll_NoResponse() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        assertThatThrownBy(() -> supplierService.getAll(pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    void removePendingRequestById_Success() {
        CompletableFuture<SupplierDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        assertThat(supplierService.removePendingRequestById(correlationId, id)).isEqualTo(future);
        assertThat(pendingRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingPageRequestById_Success() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(correlationId, future);

        assertThat(supplierService.removePendingPageRequestById(correlationId)).isEqualTo(future);
        assertThat(pendingPageRequests).doesNotContainKey(correlationId);
    }
}
