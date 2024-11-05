package com.budgetMicroservice.atest;

import com.budgetMicroservice.dto.AttachFileRequestDTO;
import com.budgetMicroservice.dto.SupplierDTO;
import com.budgetMicroservice.service.MovementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.Base64;
import java.util.UUID;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final KafkaTemplate<String, Pageable> kafkaTemplate;

    @GetMapping()
    public String sendTestMessage() {
        kafkaTemplate.send("find-all-suppliers", PageRequest.of(0, 10));
        return "Message sent!";
    }


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
//            @RequestParam String userEmail) {
//        try {
//            movementService.exportAndSendMovements(start, end, status, userEmail);
//
//            return ResponseEntity.ok("Movements exported and sent to email: " + userEmail);
//        } catch (MovementNotFoundException | GenerateExcelException e) {
//            return ResponseEntity.status(500).body("Error processing request: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid date format or parameters: " + e.getMessage());
//        }
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


    @KafkaListener(topics = "supplier-response", groupId = "response_group", concurrency = "10")
    public void listen(SupplierDTO supplierDTO) {
        System.out.println("inside-supplier-response");

        System.out.println(supplierDTO);
    }
}
