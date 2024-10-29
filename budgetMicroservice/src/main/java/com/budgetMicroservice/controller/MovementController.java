//package com.budgetMicroservice.controller;
//
//import com.budgetMicroservice.dto.InvoiceDTO;
//import com.budgetMicroservice.dto.MovementDTO;
//import com.budgetMicroservice.model.Invoice;
//import com.budgetMicroservice.model.Movement;
//import com.budgetMicroservice.service.MovementService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/movement")
//@RequiredArgsConstructor
//public class MovementController {
//    private MovementService movementService;
//
//    @PostMapping("/create")
//    public ResponseEntity<Movement> createMovement(@RequestBody MovementDTO dto) {
//        return ResponseEntity.ok(movementService.createMovement(dto));
//    }
//
//    @PutMapping("/updateStatus/{movementId}")
//    public ResponseEntity<Movement> updateMovementStatus(@PathVariable UUID movementId, @RequestParam boolean paidStatus) {
//        return ResponseEntity.ok(movementService.updateMovementStatus(movementId, paidStatus));
//    }
//
//    @PostMapping("/uploadFile")
//    public ResponseEntity<InvoiceDTO> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam UUID movementId) {
//        Movement movement = movementRepository.findById(movementId).orElseThrow();
//        Invoice invoice = new Invoice();
//        invoice.setMovement(movement);
//        invoice.setFile(file.getBytes());
//        invoiceRepository.save(invoice);
//
//        return ResponseEntity.ok().build();
//    }
//}
