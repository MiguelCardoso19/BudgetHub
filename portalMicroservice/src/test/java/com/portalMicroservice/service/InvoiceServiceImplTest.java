package com.portalMicroservice.service;

import com.portalMicroservice.dto.budget.*;
import com.portalMicroservice.exception.GenericException;
import com.portalMicroservice.exception.budget.FailedToUploadFileException;
import com.portalMicroservice.service.impl.InvoiceServiceImpl;
import com.portalMicroservice.util.PageableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private PageableUtils pageableUtils;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<InvoiceDTO>> pendingRequests;

    @Mock
    private ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>> pendingPageRequests;

    @Mock
    private MultipartFile file;

    private UUID correlationId;

    private UUID id;

    private InvoiceDTO invoiceDTO;

    private Pageable pageable;

    private CustomPageableDTO customPageableDTO;

    private long timeoutDuration;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);

        correlationId = UUID.randomUUID();
        id = UUID.randomUUID();
        invoiceDTO = new InvoiceDTO();
        invoiceDTO.setCorrelationId(correlationId);
        invoiceDTO.setId(id);

        timeoutDuration = 5L;

        pageable = PageRequest.of(0, 10);
        customPageableDTO = pageableUtils.convertToCustomPageable(pageable);

        Field timeoutField = InvoiceServiceImpl.class.getDeclaredField("TIMEOUT_DURATION");
        timeoutField.setAccessible(true);
        timeoutField.set(invoiceService, timeoutDuration);

        Field pendingRequestsField = InvoiceServiceImpl.class.getDeclaredField("pendingRequests");
        pendingRequestsField.setAccessible(true);
        pendingRequests = (ConcurrentHashMap<UUID, CompletableFuture<InvoiceDTO>>) pendingRequestsField.get(invoiceService);

        Field pendingPageRequestsField = InvoiceServiceImpl.class.getDeclaredField("pendingPageRequests");
        pendingPageRequestsField.setAccessible(true);
        pendingPageRequests = (ConcurrentHashMap<UUID, CompletableFuture<CustomPageDTO>>) pendingPageRequestsField.get(invoiceService);
    }

    @Test
    public void create_Success() throws Exception {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        future.complete(invoiceDTO);
        pendingRequests.put(invoiceDTO.getCorrelationId(), future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.create(invoiceDTO)).thenReturn(future.get());

        assertEquals(invoiceDTO, invoiceServiceMock.create(invoiceDTO));
        verify(invoiceServiceMock).create(invoiceDTO);
    }

    @Test
    void create_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceDTO.getCorrelationId(), future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.create(invoiceDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> invoiceService.create(invoiceDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void update_Success() throws Exception {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        future.complete(invoiceDTO);
        pendingRequests.put(invoiceDTO.getId(), future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.update(invoiceDTO)).thenReturn(future.get());

        assertEquals(invoiceDTO, invoiceServiceMock.update(invoiceDTO));
    }

    @Test
    void update_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceDTO.getId(), future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.update(invoiceDTO)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> invoiceService.update(invoiceDTO)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void delete_Success() throws Exception {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(invoiceDTO);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        doNothing().when(invoiceServiceMock).delete(eq(id));

        invoiceServiceMock.delete(id);

        verify(invoiceServiceMock).delete(eq(id));
    }

    @Test
    void delete_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        doNothing().when(invoiceServiceMock).delete(eq(id));

        assertThatThrownBy(() -> invoiceService.delete(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getById_Success() throws Exception {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);
        future.complete(invoiceDTO);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.getById(eq(id))).thenReturn(future.get());

        assertEquals(invoiceDTO, invoiceServiceMock.getById(id));
    }

    @Test
    void getById_NoResponse() throws ExecutionException, InterruptedException, TimeoutException, GenericException {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(id, future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.getById(eq(id))).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> invoiceService.getById(id)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void getAll_Success() throws Exception {
        CustomPageDTO expectedPage = new CustomPageDTO();
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);
        future.complete(expectedPage);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.getAll(pageable)).thenReturn(future.get());

        assertEquals(expectedPage, invoiceServiceMock.getAll(pageable));
    }

    @Test
    void getAll_NoResponse() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(customPageableDTO.getCorrelationId(), future);

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        when(invoiceServiceMock.getAll(pageable)).thenReturn(future.getNow(null));

        assertThatThrownBy(() -> invoiceService.getAll(pageable)).isInstanceOf(TimeoutException.class);
    }

    @Test
    public void attachBase64FileToInvoice_Success() throws Exception {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(invoiceDTO.getId(), future);
        future.complete(invoiceDTO);

        when(file.getBytes()).thenReturn("dummy".getBytes());
        when(file.getContentType()).thenReturn("application/pdf");

        InvoiceServiceImpl invoiceServiceMock = mock(InvoiceServiceImpl.class);
        doNothing().when(invoiceServiceMock).attachBase64FileToInvoice(eq(id), eq(file));

        invoiceServiceMock.attachBase64FileToInvoice(id, file);

        verify(invoiceServiceMock).attachBase64FileToInvoice(eq(id), eq(file));
    }

    @Test
    void attachBase64FileToInvoice_FailedToUploadFile() throws Exception {
        when(file.getBytes()).thenThrow(new IOException());

        assertThatThrownBy(() -> invoiceService.attachBase64FileToInvoice(id, file)).isInstanceOf(FailedToUploadFileException.class);
    }

    @Test
    void removePendingRequestById_Success() {
        CompletableFuture<InvoiceDTO> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);

        assertThat(invoiceService.removePendingRequestById(correlationId, id)).isEqualTo(future);
        assertThat(pendingRequests).doesNotContainKey(correlationId);
    }

    @Test
    void removePendingPageRequestById_Success() {
        CompletableFuture<CustomPageDTO> future = new CompletableFuture<>();
        pendingPageRequests.put(correlationId, future);

        assertThat(invoiceService.removePendingPageRequestById(correlationId)).isEqualTo(future);
        assertThat(pendingPageRequests).doesNotContainKey(correlationId);
    }
}