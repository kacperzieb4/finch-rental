package com.finchrental.controller;

import com.finchrental.dto.EquipmentRequestDTO;
import com.finchrental.dto.EquipmentResponseDTO;
import com.finchrental.entity.Equipment;
import com.finchrental.exception.ResourceNotFoundException;
import com.finchrental.mapper.EquipmentMapper;
import com.finchrental.repository.ReservationRepository;
import com.finchrental.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
@CrossOrigin
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;
    private final ReservationRepository reservationRepository;

    @Autowired
    public EquipmentController(EquipmentService equipmentService,
                               EquipmentMapper equipmentMapper,
                               ReservationRepository reservationRepository) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
        List<EquipmentResponseDTO> equipmentList = equipmentService.getAllEquipment().stream()
                .map(equipmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(equipmentList);
    }

    @GetMapping("/available")
    public ResponseEntity<List<EquipmentResponseDTO>> getAvailableEquipment(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<Equipment> catalog = equipmentService.getAllEquipment();
        List<EquipmentResponseDTO> result = new ArrayList<>();

        for (Equipment eq : catalog) {
            long overlappingCount = reservationRepository.countOverlappingReservations(
                    eq.getId(), startDate, endDate
            );

            int total = eq.getQuantity() != null ? eq.getQuantity() : 0;
            int available = Math.max(0, total - (int) overlappingCount);

            EquipmentResponseDTO dto = equipmentMapper.toResponseDTO(eq);
            dto.setTotalUnits(total);
            dto.setAvailableUnits(available);
            result.add(dto);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id)
                .map(equipmentMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Sprzęt o podanym ID nie istnieje: " + id));
    }

    @PostMapping
    public ResponseEntity<EquipmentResponseDTO> createEquipment(@Valid @RequestBody EquipmentRequestDTO dto) {
        Equipment equipment = equipmentMapper.toEntity(dto);
        Equipment created = equipmentService.createEquipment(equipment);
        return ResponseEntity.status(HttpStatus.CREATED).body(equipmentMapper.toResponseDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentResponseDTO> updateEquipment(@PathVariable Long id, @Valid @RequestBody EquipmentRequestDTO dto) {
        Equipment equipmentDetails = equipmentMapper.toEntity(dto);
        return equipmentService.updateEquipment(id, equipmentDetails)
                .map(equipmentMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Nie można zaktualizować - sprzęt o podanym ID nie istnieje: " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        boolean deleted = equipmentService.deleteEquipment(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            throw new ResourceNotFoundException("Nie można usunąć - sprzęt o podanym ID nie istnieje: " + id);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Plik jest pusty");
        }

        try {
            Path uploadDir = Paths.get("uploads");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID().toString() + extension;
            Path filePath = uploadDir.resolve(fileName);

            Files.copy(file.getInputStream(), filePath);

            Map<String, String> response = new HashMap<>();
            response.put("url", "/uploads/" + fileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Błąd podczas zapisywania pliku na serwerze");
        }
    }
}
