package com.finchrental.controller;

import com.finchrental.dto.EquipmentRequestDTO;
import com.finchrental.dto.EquipmentResponseDTO;
import com.finchrental.entity.Equipment;
import com.finchrental.exception.ResourceNotFoundException;
import com.finchrental.mapper.EquipmentMapper;
import com.finchrental.service.EquipmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;
    private final EquipmentMapper equipmentMapper;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, EquipmentMapper equipmentMapper) {
        this.equipmentService = equipmentService;
        this.equipmentMapper = equipmentMapper;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDTO>> getAllEquipment() {
        List<EquipmentResponseDTO> equipmentList = equipmentService.getAllEquipment().stream()
                .map(equipmentMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(equipmentList);
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
}
