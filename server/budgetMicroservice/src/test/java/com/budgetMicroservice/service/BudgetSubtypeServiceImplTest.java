package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetSubtype;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetSubtypeRepository;
import com.budgetMicroservice.service.impl.BudgetSubtypeServiceImpl;
import com.budgetMicroservice.util.BudgetUtils;
import com.budgetMicroservice.util.PageableUtils;
import com.budgetMicroservice.validator.BudgetValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class BudgetSubtypeServiceImplTest {

    @InjectMocks
    private BudgetSubtypeServiceImpl budgetSubtypeService;

    @Mock
    private BudgetSubtypeRepository budgetSubtypeRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private BudgetValidator budgetValidator;

    @Mock
    private BudgetTypeService budgetTypeService;

    @Mock
    private BudgetUtils budgetUtils;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    private UUID id;

    private BudgetSubtype budgetSubtype;

    private BudgetSubtypeDTO budgetSubtypeDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        budgetSubtypeDTO = new BudgetSubtypeDTO();
        budgetSubtype = new BudgetSubtype();
    }

    @Test
    void testAddSubtypeToBudget_Success() throws Exception {
        budgetSubtypeDTO.setBudgetTypeId(id);
        budgetSubtypeDTO.setCorrelationId(UUID.randomUUID());
        BudgetType budgetType = new BudgetType();
        BudgetSubtypeDTO savedBudgetSubtypeDTO = new BudgetSubtypeDTO();

        when(budgetTypeService.findBudgetTypeEntityById(budgetSubtypeDTO.getBudgetTypeId())).thenReturn(budgetType);
        doNothing().when(budgetValidator).checkForExistingBudgetSubtype(any(), any());
        doNothing().when(budgetUtils).checkBudgetExceeded(any(), any(), any(), any());
        when(budgetMapper.toEntity(budgetSubtypeDTO)).thenReturn(budgetSubtype);
        when(budgetMapper.toDTO(budgetSubtype)).thenReturn(savedBudgetSubtypeDTO);
        when(budgetSubtypeRepository.save(budgetSubtype)).thenReturn(budgetSubtype);

        BudgetSubtypeDTO result = budgetSubtypeService.addSubtypeToBudget(budgetSubtypeDTO);

        assertEquals(savedBudgetSubtypeDTO, result);
    }

    @Test
    void testUpdateBudgetSubtype_Success() throws Exception {
        budgetSubtypeDTO.setId(id);
        BudgetSubtype updatedBudgetSubtype = new BudgetSubtype();
        BudgetSubtypeDTO updatedBudgetSubtypeDTO = new BudgetSubtypeDTO();

        when(budgetSubtypeRepository.findById(budgetSubtypeDTO.getId())).thenReturn(Optional.of(budgetSubtype));
        doNothing().when(budgetUtils).checkBudgetExceeded(any(), any(), any(), any());
        doNothing().when(budgetValidator).checkForExistingBudgetSubtypeUpdate(any(), any());
        when(budgetMapper.toEntity(budgetSubtypeDTO)).thenReturn(updatedBudgetSubtype);
        when(budgetSubtypeRepository.save(updatedBudgetSubtype)).thenReturn(updatedBudgetSubtype);
        when(budgetMapper.toDTO(updatedBudgetSubtype)).thenReturn(updatedBudgetSubtypeDTO);

        assertEquals(updatedBudgetSubtypeDTO, budgetSubtypeService.updateBudgetSubtype(budgetSubtypeDTO));
    }

    @Test
    void testDeleteBudgetSubtype_Success() throws Exception {
        when(budgetSubtypeRepository.findById(id)).thenReturn(Optional.of(budgetSubtype));
        doNothing().when(budgetUtils).handleDeleteBudgetSubtypeAvailableFunds(any(), any(), any());

        budgetSubtypeService.deleteBudgetSubtype(id);

        verify(kafkaUuidTemplate, times(1)).send(eq("budget-subtype-delete-success-response"), any(UUID.class));
    }

    @Test
    void testDeleteBudgetSubtype_ThrowsNotFoundException() {
        when(budgetSubtypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BudgetSubtypeNotFoundException.class, () -> budgetSubtypeService.deleteBudgetSubtype(id));
    }

    @Test
    void testFindAllBudgetSubtypes_Success() {
        CustomPageableDTO customPageableDTO = new CustomPageableDTO(
                id, 1, 10, 0, true, false, null
        );

        Pageable pageable = PageableUtils.convertToPageable(customPageableDTO);

        List<BudgetSubtype> budgetSubtypes = List.of(budgetSubtype);
        List<BudgetSubtypeDTO> budgetSubtypeDTOs = List.of(budgetSubtypeDTO);
        Page<BudgetSubtype> budgetSubtypePage = new PageImpl<>(budgetSubtypes, pageable, 1);

        when(budgetSubtypeRepository.findAll(pageable)).thenReturn(budgetSubtypePage);
        when(budgetMapper.toDTOSubtypeList(budgetSubtypePage)).thenReturn(budgetSubtypeDTOs);
        when(budgetMapper.toDTO(any(BudgetSubtype.class))).thenReturn(budgetSubtypeDTO);

        Page<BudgetSubtypeDTO> result = budgetSubtypeService.findAllBudgetSubtypes(customPageableDTO);

        verify(budgetSubtypeRepository).findAll(pageable);
        verify(budgetMapper).toDTOSubtypeList(budgetSubtypePage);

        assertNotNull(result);
        assertEquals(11, result.getTotalElements());
        assertEquals(budgetSubtypeDTO, result.getContent().get(0));
    }

    @Test
    void testFindBudgetSubtypeDTOById_Success() throws Exception {
        when(budgetSubtypeRepository.findById(id)).thenReturn(Optional.of(budgetSubtype));
        when(budgetMapper.toDTO(budgetSubtype)).thenReturn(budgetSubtypeDTO);

        assertEquals(budgetSubtypeDTO, budgetSubtypeService.findBudgetSubtypeDTOById(id));
    }
}