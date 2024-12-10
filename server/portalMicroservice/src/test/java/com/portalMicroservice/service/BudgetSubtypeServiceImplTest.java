package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.BudgetSubtypeDTO;
import com.portalMicroservice.dto.budget.CustomPageDTO;
import com.portalMicroservice.dto.budget.CustomPageableDTO;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.service.impl.BudgetSubtypeServiceImpl;
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

class BudgetSubtypeServiceImplTest {

    @InjectMocks
    private BudgetSubtypeServiceImpl budgetSubtypeService;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private PageableUtils pageableUtils;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>> pendingRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests;

    private UUID correlationId;

    private UUID id;

    private Field timeoutField;

    private BudgetSubtypeDTO budgetSubtypeDTO;

    private Pageable pageable;

    private CustomPageableDTO customPageableDTO;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        pageable = PageRequest.of(0, 10);
        customPageableDTO = pageableUtils.convertToCustomPageable(pageable);

        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
        budgetSubtypeDTO = new BudgetSubtypeDTO();
        budgetSubtypeDTO.setCorrelationId(correlationId);
        budgetSubtypeDTO.setId(id);

        timeoutField = BudgetSubtypeServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(budgetSubtypeService, 5L);

        Field pendingRequestsField = BudgetSubtypeServiceImpl.class.getDeclaredField("pendingRequests");
        pendingRequestsField.setAccessible(true);
        pendingRequests = (ConcurrentHashMap<UUID, CompletableFuture<BudgetSubtypeDTO>>) pendingRequestsField.get(budgetSubtypeService);
        Field pendingPageRequestsField = BudgetSubtypeServiceImpl.class.getDeclaredField("pendingPageRequests");
        pendingPageRequestsField.setAccessible(true);
        pendingPageRequests = (ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>>) pendingPageRequestsField.get(budgetSubtypeService);
    }

    @Test
    public void addSubtypeToBudget_Success() throws Exception {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        future.complete(budgetSubtypeDTO);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.addSubtypeToBudget(budgetSubtypeDTO)).thenReturn(future.get());

        assertEquals(budgetSubtypeDTO, budgetSubtypeServiceMock.addSubtypeToBudget(budgetSubtypeDTO));
        verify(budgetSubtypeServiceMock).addSubtypeToBudget(budgetSubtypeDTO);
    }

    @Test
    void addSubtypeToBudget_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetSubtypeDTO.getCorrelationId(), future);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.addSubtypeToBudget(budgetSubtypeDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void update_Success() throws Exception {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        future.complete(budgetSubtypeDTO);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.update(budgetSubtypeDTO)).thenReturn(future.get());

        assertEquals(budgetSubtypeDTO, budgetSubtypeServiceMock.update(budgetSubtypeDTO));
        verify(budgetSubtypeServiceMock).update(budgetSubtypeDTO);
    }

    @Test
    void update_NoResponse() throws ExecutionException, InterruptedException, GenericException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(budgetSubtypeDTO.getId(), future);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.update(budgetSubtypeDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetSubtypeService.update(budgetSubtypeDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void delete_Success() throws Exception {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(budgetSubtypeDTO);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        doNothing().when(budgetSubtypeServiceMock).delete(eq(id));

        budgetSubtypeServiceMock.delete(id);

        verify(budgetSubtypeServiceMock).delete(eq(id));
    }

    @Test
    void delete_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        doNothing().when(budgetSubtypeServiceMock).delete(eq(id));

        assertThatThrownBy(() -> budgetSubtypeService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getById_Success() throws Exception {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(budgetSubtypeDTO);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.getById(eq(id))).thenReturn(future.get());

        assertEquals(budgetSubtypeDTO, budgetSubtypeServiceMock.getById(id));
    }

    @Test
    void getById_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.getById(eq(id))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetSubtypeService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void findAll_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.findAll(pageable)).thenReturn(future.get());

        assertEquals(expectedPage, budgetSubtypeServiceMock.findAll(pageable));
    }

    @Test
    void findAll_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        BudgetSubtypeServiceImpl budgetSubtypeServiceMock = mock(BudgetSubtypeServiceImpl.class);
        when(budgetSubtypeServiceMock.findAll(pageable)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> budgetSubtypeService.findAll(pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    void removePendingRequestById_Success() {
        CompletableFuture<BudgetSubtypeDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        assertThat(budgetSubtypeService.removePendingRequestById(correlationId, null)).isEqualTo(future);
        assertThat(pendingRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingPageRequestById_Success() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(correlationId, future);

        assertThat(budgetSubtypeService.removePendingPageRequestById(correlationId)).isEqualTo(future);
        assertThat(pendingPageRequests).doesNotContainKey(correlationId);
    }
}