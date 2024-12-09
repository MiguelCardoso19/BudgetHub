package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.InvoiceMapper;
import com.budgetMicroservice.model.Invoice;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.InvoiceRepository;
import com.budgetMicroservice.service.impl.InvoiceServiceImpl;
import com.budgetMicroservice.util.PageableUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class InvoiceServiceImplTest {

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @Mock
    private S3Service s3Service;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private MovementService movementService;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private KafkaTemplate<String, InvoiceDTO> kafkaInvoiceTemplate;

    @Mock
    private KafkaTemplate<String, CustomPageDTO> kafkaCustomPageTemplate;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    @Mock
    private KafkaTemplate<String, InvoiceNotFoundException> kafkaInvoiceNotFoundExceptionTemplate;

    @Mock
    private KafkaTemplate<String, FailedToUploadFileException> kafkaFailedToUploadFileExceptionTemplate;

    private InvoiceDTO invoiceDTO;
    private Invoice invoice;
    private UUID id;

    @BeforeEach
    void setUp() {
        openMocks(this);
        invoiceDTO = new InvoiceDTO();
        invoice = new Invoice();
        id = UUID.randomUUID();
    }

    @Test
    void testCreateInvoice_Success() throws Exception {
        invoiceDTO.setId(id);
        Movement movement = new Movement();

        when(invoiceMapper.toEntity(invoiceDTO)).thenReturn(invoice);
        when(movementService.getMovementEntityById(invoiceDTO.getMovementId())).thenReturn(movement);
        when(invoiceRepository.save(invoice)).thenReturn(invoice);
        when(invoiceMapper.toDTO(invoice)).thenReturn(invoiceDTO);

        InvoiceDTO result = invoiceService.create(invoiceDTO);

        assertNotNull(result);
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testFindInvoiceById_Success() throws Exception {
        invoice.setId(id);

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));

        Invoice result = invoiceService.findInvoiceEntityById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(invoiceRepository, times(1)).findById(id);
    }

    @Test
    void testFindInvoiceById_NotFound() {
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.findInvoiceEntityById(id));
    }

    @Test
    void testAttachMultipartFileToInvoice_Success() throws Exception {
        MultipartFile file = mock(MultipartFile.class);
        invoice.setId(id);

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});
        when(file.getContentType()).thenReturn("image/png");
        when(s3Service.putObject(any())).thenReturn("file-key");

        invoiceService.attachMultipartFileToInvoice(id, file);

        assertEquals("file-key", invoice.getFileKey());
        verify(invoiceRepository, times(1)).save(invoice);
    }

    @Test
    void testGetAllInvoices_Success() {
        CustomPageableDTO pageableDTO = new CustomPageableDTO(id, 1, 10, 0, true, false, null);
        Pageable pageable = PageableUtils.convertToPageable(pageableDTO);

        List<Invoice> invoices = List.of(invoice);
        Page<Invoice> invoicePage = new PageImpl<>(invoices, pageable, invoices.size());

        when(invoiceRepository.findAll(pageable)).thenReturn(invoicePage);
        when(invoiceMapper.toDTO(invoice)).thenReturn(invoiceDTO);

        Page<InvoiceDTO> result = invoiceService.getAll(pageableDTO);

        assertNotNull(result);
        assertEquals(11, result.getTotalElements());
        assertEquals(invoiceDTO, result.getContent().get(0));
        verify(invoiceRepository, times(1)).findAll(pageable);
        verify(invoiceMapper, times(1)).toDTOList(invoicePage);
    }

    @Test
    void testDeleteInvoice_Success() throws Exception {
        invoice.setId(id);

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));

        invoiceService.delete(id);

        verify(invoiceRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteInvoice_NotFound() {
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.delete(id));
    }

    @Test
    void testAttachBase64FileToInvoice_Success() throws Exception {
        AttachFileRequestDTO request = new AttachFileRequestDTO();
        request.setId(id);
        request.setBase64File(Base64.getEncoder().encodeToString(new byte[]{1, 2, 3}));
        request.setContentType("image/png");
        invoice.setId(id);

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(invoice));
        when(s3Service.putObject(any())).thenReturn("file-key");

        invoiceService.attachBase64FileToInvoice(request);

        assertEquals("file-key", invoice.getFileKey());
        verify(invoiceRepository, times(1)).save(invoice);
    }
}
