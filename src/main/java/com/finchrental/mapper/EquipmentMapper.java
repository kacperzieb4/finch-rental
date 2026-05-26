package com.finchrental.mapper;

import com.finchrental.dto.EquipmentRequestDTO;
import com.finchrental.dto.EquipmentResponseDTO;
import com.finchrental.entity.Equipment;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public EquipmentResponseDTO toResponseDTO(Equipment equipment) {
        if (equipment == null) {
            return null;
        }
        return EquipmentResponseDTO.builder()
                .id(equipment.getId())
                .name(equipment.getName())
                .category(equipment.getCategory())
                .description(equipment.getDescription())
                .pricePerDay(equipment.getPricePerDay())
                .available(equipment.getAvailable())
                .build();
    }

    public Equipment toEntity(EquipmentRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        Equipment equipment = new Equipment();
        updateEntityFromDTO(equipment, dto);
        return equipment;
    }

    public void updateEntityFromDTO(Equipment equipment, EquipmentRequestDTO dto) {
        if (dto == null || equipment == null) {
            return;
        }
        equipment.setName(dto.getName());
        equipment.setCategory(dto.getCategory());
        equipment.setDescription(dto.getDescription());
        equipment.setPricePerDay(dto.getPricePerDay());
        equipment.setAvailable(dto.getAvailable());
    }
}
