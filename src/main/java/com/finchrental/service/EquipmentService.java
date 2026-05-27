package com.finchrental.service;

import com.finchrental.entity.Equipment;
import com.finchrental.repository.EquipmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<Equipment> getAllEquipment() {
        return equipmentRepository.findAll();
    }

    public Optional<Equipment> getEquipmentById(Long id) {
        return equipmentRepository.findById(id);
    }

    public Equipment createEquipment(Equipment equipment) {
        return equipmentRepository.save(equipment);
    }

    public Optional<Equipment> updateEquipment(Long id, Equipment equipmentDetails) {
        return equipmentRepository.findById(id).map(existingEquipment -> {
            existingEquipment.setName(equipmentDetails.getName());
            existingEquipment.setCategory(equipmentDetails.getCategory());
            existingEquipment.setDescription(equipmentDetails.getDescription());
            existingEquipment.setPricePerDay(equipmentDetails.getPricePerDay());
            existingEquipment.setAvailable(equipmentDetails.getAvailable());
            existingEquipment.setQuantity(equipmentDetails.getQuantity());
            existingEquipment.setImageUrl(equipmentDetails.getImageUrl());
            return equipmentRepository.save(existingEquipment);
        });
    }

    public boolean deleteEquipment(Long id) {
        return equipmentRepository.findById(id).map(equipment -> {
            equipmentRepository.delete(equipment);
            return true;
        }).orElse(false);
    }
}
