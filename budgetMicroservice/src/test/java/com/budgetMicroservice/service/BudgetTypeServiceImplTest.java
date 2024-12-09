package com.budgetMicroservice.service;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.exception.*;
import com.budgetMicroservice.mapper.BudgetMapper;
import com.budgetMicroservice.model.BudgetType;
import com.budgetMicroservice.repository.BudgetTypeRepository;
import com.budgetMicroservice.service.impl.BudgetTypeServiceImpl;
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

class BudgetTypeServiceImplTest {

    @InjectMocks
    private BudgetTypeServiceImpl budgetTypeService;

    @Mock
    private BudgetTypeRepository budgetTypeRepository;

    @Mock
    private BudgetMapper budgetMapper;

    @Mock
    private BudgetValidator budgetValidator;

    @Mock
    private KafkaTemplate<String, BudgetTypeDTO> kafkaBudgetTypeTemplate;

    @Mock
    private KafkaTemplate<String, UUID> kafkaUuidTemplate;

    private UUID id;

    private BudgetType budgetType;

    private BudgetTypeDTO budgetTypeDTO;

    @BeforeEach
    void setUp() {
        openMocks(this);
        id = UUID.randomUUID();
        budgetTypeDTO = new BudgetTypeDTO();
        budgetType = new BudgetType();
    }

    @Test
    void testCreateBudgetType_Success() throws Exception {
        budgetTypeDTO.setCorrelationId(UUID.randomUUID());

        doNothing().when(budgetValidator).checkForExistingBudgetType(budgetTypeDTO, budgetTypeRepository);
        when(budgetMapper.toEntity(budgetTypeDTO)).thenReturn(budgetType);
        when(budgetMapper.toDTO(budgetType)).thenReturn(budgetTypeDTO);
        when(budgetTypeRepository.save(budgetType)).thenReturn(budgetType);

        assertEquals(budgetTypeDTO, budgetTypeService.createBudgetType(budgetTypeDTO));
    }

    @Test
    void testUpdateBudgetType_Success() throws Exception {
        budgetTypeDTO.setId(id);
        BudgetType updatedBudgetType = new BudgetType();
        BudgetTypeDTO updatedBudgetTypeDTO = new BudgetTypeDTO();

        when(budgetTypeRepository.findById(budgetTypeDTO.getId())).thenReturn(Optional.of(budgetType));
        doNothing().when(budgetValidator).checkForExistingBudgetType(budgetTypeDTO, budgetTypeRepository);
        when(budgetMapper.toEntity(budgetTypeDTO)).thenReturn(updatedBudgetType);
        when(budgetMapper.toDTO(updatedBudgetType)).thenReturn(updatedBudgetTypeDTO);
        when(budgetTypeRepository.save(updatedBudgetType)).thenReturn(updatedBudgetType);

        assertEquals(updatedBudgetTypeDTO, budgetTypeService.updateBudgetType(budgetTypeDTO));
    }

    @Test
    void testDeleteBudgetType_Success() throws Exception {
        when(budgetTypeRepository.existsById(id)).thenReturn(true);
        doNothing().when(budgetTypeRepository).deleteById(id);

        budgetTypeService.deleteBudgetType(id);

        verify(budgetTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteBudgetType_ThrowsNotFoundException() {
        when(budgetTypeRepository.existsById(id)).thenReturn(false);

        assertThrows(BudgetTypeNotFoundException.class, () -> budgetTypeService.deleteBudgetType(id));
    }

    @Test
    void testFindAllBudgetTypes_Success() {
        CustomPageableDTO customPageableDTO = new CustomPageableDTO(UUID.randomUUID(), 1, 10, 0, true, false, null);
        Pageable pageable = PageableUtils.convertToPageable(customPageableDTO);

        List<BudgetType> budgetTypes = List.of(budgetType);
        List<BudgetTypeDTO> budgetTypeDTOs = List.of(budgetTypeDTO);
        Page<BudgetType> budgetTypePage = new PageImpl<>(budgetTypes, pageable, 1);

        when(budgetTypeRepository.findAll(pageable)).thenReturn(budgetTypePage);
        when(budgetMapper.toDTOTypeList(budgetTypePage)).thenReturn(budgetTypeDTOs);
        when(budgetMapper.toDTO(any(BudgetType.class))).thenReturn(budgetTypeDTO);

        Page<BudgetTypeDTO> result = budgetTypeService.findAllBudgetTypes(customPageableDTO);

        assertNotNull(result);
        assertEquals(11, result.getTotalElements());
        assertEquals(budgetTypeDTO, result.getContent().get(0));
    }

    @Test
    void testFindBudgetTypeDTOById_Success() throws Exception {
        when(budgetTypeRepository.findById(id)).thenReturn(Optional.of(budgetType));
        when(budgetMapper.toDTO(budgetType)).thenReturn(budgetTypeDTO);

        BudgetTypeDTO result = budgetTypeService.findBudgetTypeDTOById(id);

        assertNotNull(result);
        assertEquals(budgetTypeDTO, result);
    }
}