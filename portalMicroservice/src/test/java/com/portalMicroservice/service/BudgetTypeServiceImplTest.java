package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetTypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.impl.BudgetTypeServiceImpl;
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

class BudgetTypeServiceImplTest {

    @InjectMocks
    private BudgetTypeServiceImpl budgetTypeService;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private PageableUtils pageableUtils;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<BudgetTypeDTO>> pendingRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests;

    private UUID correlationId;

    private UUID id;

    private Field timeoutField;

    private BudgetTypeDTO budgetTypeDTO;

    private Pageable pageable;

    private CustomPageableDTO customPageableDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        pageable = PageRequest.of(0, 10);
        customPageableDTO = pageableUtils.convertToCustomPageable(pageable);

        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
        budgetTypeDTO = new BudgetTypeDTO();
        budgetTypeDTO.setCorrelationId(correlationId);
        budgetTypeDTO.setId(id);

        timeoutField = BudgetTypeServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(budgetTypeService, 5L);

        Field pendingRequestsField = BudgetTypeServiceImpl.class.getDeclaredField("pendingRequests");
        pendingRequestsField.setAccessible(true);
        pendingRequests = (ConcurrentHashMap<UUID, CompletableFuture<BudgetTypeDTO>>) pendingRequestsField.get(budgetTypeService);

        Field pendingPageRequestsField = BudgetTypeServiceImpl.class.getDeclaredField("pendingPageRequests");
        pendingPageRequestsField.setAccessible(true);
        pendingPageRequests = (ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>>) pendingPageRequestsField.get(budgetTypeService);
    }

    @Test
    public void create_Success() throws Exception {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        future.complete(budgetTypeDTO);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.create(budgetTypeDTO)).thenReturn(future.get());

        assertEquals(budgetTypeDTO, budgetTypeServiceMock.create(budgetTypeDTO));
        verify(budgetTypeServiceMock).create(budgetTypeDTO);
    }

    @Test
    void create_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetTypeDTO.getCorrelationId(), future);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.create(budgetTypeDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetTypeService.create(budgetTypeDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void update_Success() throws Exception {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        future.complete(budgetTypeDTO);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.update(budgetTypeDTO)).thenReturn(future.get());

        assertEquals(budgetTypeDTO, budgetTypeServiceMock.update(budgetTypeDTO));
        verify(budgetTypeServiceMock).update(budgetTypeDTO);
    }

    @Test
    void update_NoResponse() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetTypeDTO.getCorrelationId(), future);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.update(budgetTypeDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetTypeService.update(budgetTypeDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void delete_Success() throws Exception {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(budgetTypeDTO);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        doNothing().when(budgetTypeServiceMock).delete(eq(id));

        budgetTypeServiceMock.delete(id);

        verify(budgetTypeServiceMock).delete(eq(id));
    }

    @Test
    void delete_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        doNothing().when(budgetTypeServiceMock).delete(eq(id));

        assertThatThrownBy(() -> budgetTypeService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getById_Success() throws Exception {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(budgetTypeDTO);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.getById(eq(id))).thenReturn(future.get());

        assertEquals(budgetTypeDTO, budgetTypeServiceMock.getById(id));
    }

    @Test
    void getById_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.getById(eq(id))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetTypeService.getById(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void findAll_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.findAll(pageable)).thenReturn(future.get());

        assertEquals(expectedPage, budgetTypeServiceMock.findAll(pageable));
    }

    @Test
    void findAll_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        BudgetTypeServiceImpl budgetTypeServiceMock = mock(BudgetTypeServiceImpl.class);
        when(budgetTypeServiceMock.findAll(pageable)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetTypeService.findAll(pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    void removePendingRequestById_Success() {
        CompletableFuture<BudgetTypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        assertThat(budgetTypeService.removePendingRequestById(correlationId, null)).isEqualTo(future);
        assertThat(pendingRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingPageRequestById_Success() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(correlationId, future);

        assertThat(budgetTypeService.removePendingPageRequestById(correlationId)).isEqualTo(future);
        assertThat(pendingPageRequests).doesNotContainKey(correlationId);
    }
}