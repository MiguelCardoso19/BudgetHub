package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.InvoiceMapper;
import com.budgetMicroservice.mapper.MovementMapper;
import com.budgetMicroservice.model.Movement;
import com.budgetMicroservice.repository.MovementRepository;
import com.budgetMicroservice.service.impl.MovementServiceImpl;
import com.budgetMicroservice.util.MovementUtils;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.MovementValidator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.*;

import static com.budgetMicroservice.enumerator.MovementStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private MovementMapper movementMapper;

    @Mock
    private MovementValidator movementValidator;

    @Mock
    private MovementUtils movementUtils;

    @Mock
    private SupplierService supplierService;

    @Mock
    private InvoiceService invoiceService;

    @Mock
    private BudgetSubtypeService budgetSubtypeService;

    @Mock
    private BudgetTypeService budgetTypeService;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private MovementServiceImpl movementService;

    @Mock
    private KafkaTemplate<String, Object> kafkaCustomPageTemplate;

    private MovementDTO movementDTO;

    private Movement movement;

    private UUID id;

    private CustomPageableDTO customPageableDTO;

    private ExportMovementsRequestDTO exportMovementsRequestDTO;

    private MovementsByBudgetRequestDTO movementsByBudgetRequestDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();

        movementDTO = new MovementDTO();
        movementDTO.setId(id);
        movementDTO.setStatus(SUCCEEDED);
        movement = new Movement();
        movement.setStatus(SUCCEEDED);

        customPageableDTO = new CustomPageableDTO(
                id,
                1,
                10,
                0,
                true,
                false,
                null
        );

        exportMovementsRequestDTO = new ExportMovementsRequestDTO();
        exportMovementsRequestDTO.setStartDate(LocalDate.now().minusDays(1));
        exportMovementsRequestDTO.setEndDate(LocalDate.now());
        exportMovementsRequestDTO.setStatus(SUCCEEDED);
        exportMovementsRequestDTO.setUserEmail("test@example.com");
        exportMovementsRequestDTO.setCorrelationId(UUID.randomUUID());

        movementsByBudgetRequestDTO = new MovementsByBudgetRequestDTO();
        movementsByBudgetRequestDTO.setId(id);
    }

    @Test
    void testCreateMovement_Success() throws Exception {
        when(movementMapper.toEntity(movementDTO)).thenReturn(movement);
        when(movementRepository.save(movement)).thenReturn(movement);
        when(movementMapper.toDTO(movement)).thenReturn(movementDTO);

        MovementDTO result = movementService.create(movementDTO);

        assertNotNull(result);
        assertEquals(movementDTO.getId(), result.getId());
        verify(movementValidator).validateMovement(movementDTO, movementRepository, supplierService, invoiceService);
    }

    @Test
    void testUpdateMovement_Success() throws Exception {
        Movement previousMovement = new Movement();
        previousMovement.setStatus(PROCESSING);
        Movement existingMovement = new Movement();
        existingMovement.setStatus(PROCESSING);

        when(movementRepository.findById(id)).thenReturn(Optional.of(previousMovement));
        when(movementMapper.toEntity(movementDTO)).thenReturn(existingMovement);
        when(movementRepository.save(existingMovement)).thenReturn(existingMovement);
        when(movementMapper.toDTO(existingMovement)).thenReturn(movementDTO);

        doNothing().when(movementValidator).validateMovementUpdate(movementDTO, movementRepository, supplierService, invoiceService);
        doNothing().when(movementUtils).calculateIvaAndTotal(movementDTO);
        doNothing().when(movementUtils).adjustBudgetAmounts(any(), any(), any(), any());
        doNothing().when(movementUtils).setBudget(any(), any(), any(), any());

        MovementDTO result = movementService.update(movementDTO);

        assertNotNull(result);
        assertEquals(movementDTO.getId(), result.getId());
        verify(movementRepository).save(existingMovement);
    }

    @Test
    void testUpdateMovement_NotFound() {
        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovementNotFoundException.class, () -> movementService.update(movementDTO));
    }

    @Test
    void testGetMovementDTOById() throws Exception {
        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));
        when(movementMapper.toDTO(movement)).thenReturn(movementDTO);

        assertNotNull(movementService.getMovementDTOById(id));
        verify(movementMapper).toDTO(movement);
    }

    @Test
    void testGetMovementDTOById_NotFound() {
        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovementNotFoundException.class, () -> movementService.getMovementDTOById(id));
    }

    @Test
    void testDeleteMovement() throws Exception {
        when(movementRepository.findById(id)).thenReturn(Optional.of(movement));

        movementService.delete(id);

        verify(movementRepository).deleteById(id);
        verify(movementUtils).removeMovementValueFromBudget(movement, budgetSubtypeService, budgetTypeService);
    }

    @Test
    void testDeleteMovement_NotFound() {
        when(movementRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MovementNotFoundException.class, () -> movementService.delete(id));
    }

    @Test
    void testUpdateMovementStatus_Success() throws Exception {
        MovementUpdateStatusRequestDTO requestDTO = new MovementUpdateStatusRequestDTO();
        requestDTO.setId(id);
        requestDTO.setStatus(CANCELED);
        requestDTO.setCorrelationId(UUID.randomUUID());

        when(movementRepository.findById(requestDTO.getId())).thenReturn(Optional.of(movement));
        when(movementMapper.toDTO(movement)).thenReturn(movementDTO);

        assertNotNull(movementService.updateMovementStatus(requestDTO));
        assertEquals(requestDTO.getStatus(), movement.getStatus());
        assertEquals(requestDTO.getCorrelationId(), movementDTO.getCorrelationId());
        verify(movementRepository).save(movement);
    }

    @Test
    void testGetAllMovements_Success() {
        Pageable pageable = PageableUtils.convertToPageable(customPageableDTO);

        Movement movement2 = new Movement();
        movement2.setId(UUID.randomUUID());
        List<Movement> movements = Arrays.asList(movement, movement2);

        when(movementRepository.findAll(pageable)).thenReturn(new PageImpl<>(movements, pageable, movements.size()));

        movementDTO.setId(movement.getId());
        MovementDTO movementDTO2 = new MovementDTO();
        movementDTO2.setId(movement2.getId());
        List<MovementDTO> movementDTOs = Arrays.asList(movementDTO, movementDTO2);

        when(movementMapper.toDTOList(any(Page.class))).thenReturn(movementDTOs);

        Page<MovementDTO> result = movementService.getAll(customPageableDTO);

        assertNotNull(result);
        assertEquals(12, result.getTotalElements());
        assertEquals(movements.size(), result.getContent().size());
        verify(movementRepository).findAll(pageable);
        verify(movementMapper).toDTOList(any(Page.class));
    }

    @Test
    void testExportMovements_Success() throws Exception {
        when(movementUtils.filterMovements(movementRepository, exportMovementsRequestDTO.getStartDate(), exportMovementsRequestDTO.getEndDate(), exportMovementsRequestDTO.getStatus()))
                .thenReturn(Collections.singletonList(movement));

        movementService.exportMovements(exportMovementsRequestDTO);

        verify(movementUtils).filterMovements(movementRepository, exportMovementsRequestDTO.getStartDate(), exportMovementsRequestDTO.getEndDate(), exportMovementsRequestDTO.getStatus());
        verify(movementUtils, times(1)).populateSheetWithMovements(any(XSSFSheet.class), anyList());
    }

    @Test
    void testExportMovements_Failure() {
        when(movementUtils.filterMovements(movementRepository, exportMovementsRequestDTO.getStartDate(), exportMovementsRequestDTO.getEndDate(), exportMovementsRequestDTO.getStatus()))
                .thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> movementService.exportMovements(exportMovementsRequestDTO));
    }

    @Test
    void testGetMovementByDocumentNumber_Success() throws DocumentNumberNotFoundException {
        String documentNumber = "DOC123";
        movement.setDocumentNumber(documentNumber);
        movementDTO.setId(movement.getId());
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setId(UUID.randomUUID());

        when(movementRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.of(movement));
        when(movementMapper.toDTO(movement)).thenReturn(movementDTO);
        when(invoiceMapper.toDTO(movement.getInvoice())).thenReturn(invoiceDTO);

        Movement result = movementService.getMovementByDocumentNumber(documentNumber);

        assertNotNull(result);
        assertEquals(movement.getId(), result.getId());
        verify(movementRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    void testGetMovementByDocumentNumber_NotFound() {
        String documentNumber = "DOC123";

        when(movementRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.empty());

        assertThrows(DocumentNumberNotFoundException.class, () -> movementService.getMovementByDocumentNumber(documentNumber));
        verify(movementRepository).findByDocumentNumber(documentNumber);
    }

    @Test
    void testGetMovementsByBudgetType_Success() throws Exception {
        movementsByBudgetRequestDTO.setPageable(customPageableDTO);

        Pageable pageable = PageableUtils.convertToPageable(customPageableDTO);

        List<Movement> movements = List.of(new Movement());
        Page<Movement> mockPage = new PageImpl<>(movements, pageable, movements.size());

        when(movementRepository.findByBudgetTypeId(id, pageable)).thenReturn(mockPage);

        List<MovementDTO> movementDTOList = List.of(new MovementDTO());
        when(movementMapper.toDTOList(mockPage)).thenReturn(movementDTOList);

        Page<MovementDTO> result = movementService.getMovementsByBudgetType(movementsByBudgetRequestDTO);

        assertNotNull(result);
        assertEquals(movementDTOList.size(), result.getContent().size());
    }

    @Test
    void testGetMovementsByBudgetType_NotFound() {
        movementsByBudgetRequestDTO.setPageable(customPageableDTO);

        Page<Movement> mockPage = mock(Page.class);
        when(movementRepository.findByBudgetTypeId(id, PageableUtils.convertToPageable(customPageableDTO))).thenReturn(mockPage);
        when(mockPage.isEmpty()).thenReturn(true);

        assertThrows(MovementsNotFoundForBudgetTypeException.class, () -> {
            movementService.getMovementsByBudgetType(movementsByBudgetRequestDTO);
        });
    }

    @Test
    void testGetMovementsByBudgetSubtype_Success() throws Exception {
        movementsByBudgetRequestDTO.setPageable(customPageableDTO);

        Pageable pageable = PageableUtils.convertToPageable(customPageableDTO);

        List<Movement> movements = List.of(new Movement());
        Page<Movement> mockPage = new PageImpl<>(movements, pageable, movements.size());

        when(movementRepository.findByBudgetSubtypeId(id, pageable)).thenReturn(mockPage);

        List<MovementDTO> movementDTOList = List.of(new MovementDTO());
        when(movementMapper.toDTOList(mockPage)).thenReturn(movementDTOList);

        Page<MovementDTO> result = movementService.getMovementsByBudgetSubtype(movementsByBudgetRequestDTO);

        assertNotNull(result);
        assertEquals(movementDTOList.size(), result.getContent().size());
    }

    @Test
    void testGetMovementsByBudgetSubtype_NotFound() {
        movementsByBudgetRequestDTO.setPageable(customPageableDTO);

        Page<Movement> mockPage = mock(Page.class);
        when(movementRepository.findByBudgetSubtypeId(id, PageableUtils.convertToPageable(customPageableDTO))).thenReturn(mockPage);
        when(mockPage.isEmpty()).thenReturn(true);

        assertThrows(MovementsNotFoundForBudgetSubtypeException.class, () -> {
            movementService.getMovementsByBudgetSubtype(movementsByBudgetRequestDTO);
        });
    }
}