package com.budgetMicroservice.atest;

import com.budgetMicroservice.dto.*;
import com.budgetMicroservice.enumerator.MovementStatus;
import com.budgetMicroservice.enumerator.MovementType;
import com.budgetMicroservice.exception.GenerateExcelException;
import com.budgetMicroservice.exception.MovementNotFoundException;
import com.budgetMicroservice.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
        private final KafkaTemplate<String, SupplierDTO> kafkaTemplate;
    private final MovementService movementService;

//    @GetMapping()
//    public String sendTestMessage() {
////MovementDTO movementDTO = new MovementDTO();
////        movementDTO.setSupplierId(UUID.fromString("6d72a7f1-d70f-4db6-90ff-ed7f418bdcf0"));
////        movementDTO.setBudgetSubtypeId(UUID.fromString("d797b98b-2df4-4118-85be-8534d0b5e5f2"));
////        movementDTO.setInvoiceId(UUID.fromString("25d61245-5de4-4294-8bc8-a601ed1f82f4"));
////
////        movementDTO.setMovementType(MovementType.CREDIT);  // Or any value from your MovementType enum
////        movementDTO.setDateOfEmission(LocalDate.now());
////        movementDTO.setDescription("Sample movement description");
////        movementDTO.setValueWithoutIva(1000.0);  // Example amount
////        movementDTO.setIvaRate(0.21);  // Example VAT rate (21%)
//
//        SupplierDTO fake = new SupplierDTO();
//        fake.setResponsibleName("kafka");
//        fake.setCompanyName("kafka");
//        fake.setNif("kafka");
//        fake.setEmail("kafka@gmail.com");
//        fake.setPhoneNumber("kafka");
//            fake.setId(UUID.randomUUID());
//
//        kafkaTemplate.send("create-supplier", fake);
//
//        return "Message sent!";
//    }


//        SupplierDTO fake = new SupplierDTO();
//        fake.setResponsibleName("kafka");
//        fake.setCompanyName("kafka");
//        fake.setNif("kafka");
//        fake.setEmail("kafka@gmail.com");
//        fake.setPhoneNumber("kafka");
    //    fake.setId(UUID.randomUUID());

//    @PostMapping("/export")
//    public ResponseEntity<String> exportMovements(
//            @RequestParam(required = false) LocalDate start,
//            @RequestParam(required = false) LocalDate end,
//            @RequestParam(required = false) MovementStatus status,
//            @RequestParam String userEmail) throws GenerateExcelException, MovementNotFoundException, IOException {
//
//            movementService.exportAndSendMovements(start, end, status, userEmail);
//            return ResponseEntity.ok("Movements exported and sent to email: " + userEmail);
//    }


//    String base64File = Base64.getEncoder().encodeToString("hdfhdfffdfsdddnbdnndnhdfhdfhdfh".getBytes()); // Replace with your actual file content
//
//    AttachFileRequestDTO request = new AttachFileRequestDTO();
//        request.setId(UUID.fromString("25d61245-5de4-4294-8bc8-a601ed1f82f4"));
//        request.setBase64File(base64File);
//
//            kafkaTemplate.send("attach-base64-file-to-invoice", request);
//            return "Message sent!";
//}


//    @KafkaListener(topics = "supplier-response", groupId = "response_group", concurrency = "10")
//    public void listen(SupplierDTO supplierDTO) {
//        System.out.println("inside-supplier-response");
//
//        System.out.println(supplierDTO);
//    }



//    @GetMapping()
//    public String sendTestMessage() {
//        PageableDTO pageableDTO = new PageableDTO(0, 10);
//        MovementUpdateStatusRequestDTO movementUpdateStatusRequestDTO = new MovementUpdateStatusRequestDTO();
//        movementUpdateStatusRequestDTO.setStatus(MovementStatus.REFUSED);
//        movementUpdateStatusRequestDTO.setId(UUID.fromString("d7290f5a-135e-4d92-81cb-0d7e4e0d99c2"));
//        kafkaTemplate.send("update-movement-status", movementUpdateStatusRequestDTO);
//
//        return "Message sent!";
//    }


//    @GetMapping()
//    public String sendTestMessage() {
//        MovementDTO movementDTO = new MovementDTO();
//        movementDTO.setSupplierId(UUID.fromString("6d72a7f1-d70f-4db6-90ff-ed7f418bdcf0"));
//        movementDTO.setBudgetSubtypeId(UUID.fromString("d797b98b-2df4-4118-85be-8534d0b5e5f2"));
//        movementDTO.setInvoiceId(UUID.fromString("25d61245-5de4-4294-8bc8-a601ed1f82f4"));
//
//        movementDTO.setMovementType(MovementType.CREDIT);  // Or any value from your MovementType enum
//        movementDTO.setDateOfEmission(LocalDate.now());
//        movementDTO.setDescription("Sample movement description");
//        movementDTO.setValueWithoutIva(1000.0);  // Example amount
//        movementDTO.setIvaRate(0.21);  // Example VAT rate (21%)
//
//        kafkaTemplate.send("create-movement", movementDTO);
//
//        return "Message sent!";
//    }
}
