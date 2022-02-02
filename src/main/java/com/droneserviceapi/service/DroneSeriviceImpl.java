package com.droneserviceapi.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.droneserviceapi.data.payload.request.DroneDeliveryRequest;
import com.droneserviceapi.data.payload.request.DroneGetBatteryRequest;
import com.droneserviceapi.data.payload.request.DroneRegisterRequest;
import com.droneserviceapi.data.payload.request.LoadDroneRequest;
import com.droneserviceapi.data.payload.response.MessageResponse;
import com.droneserviceapi.modal.Drone;
import com.droneserviceapi.modal.LoadMedication;
import com.droneserviceapi.modal.MedicalDelivery;
import com.droneserviceapi.modal.Medication;
import com.droneserviceapi.repository.DroneDeliveryRepository;
import com.droneserviceapi.repository.DroneRepository;
import com.droneserviceapi.repository.MedicationRepository;
import com.droneserviceapi.repository.loadDroneRepository;

@Service
public class DroneSeriviceImpl implements DroneService {

	@Autowired
	private DroneRepository droneRepository;
	@Autowired
	private MedicationRepository medicationRepository;
	@Autowired
	private loadDroneRepository loadDroneRepository;
	@Autowired
	private DroneDeliveryRepository droneDeliveryRepository;

	@Override
	public MessageResponse register(DroneRegisterRequest droneRequest) {
		Drone newdrone = new Drone();
		newdrone.setSerialNumber(droneRequest.getSerialNumber());
		newdrone.setModel(droneRequest.getModel());
		newdrone.setWeightLimit(droneRequest.getWeightLimit());
		newdrone.setBattery(droneRequest.getBattery());
		newdrone.setState(droneRequest.getState());
		droneRepository.save(newdrone);
		return new MessageResponse("New Drone created successfully");
	}

	@Override
	public Drone getBateryLevel(DroneGetBatteryRequest drequest) {
		Drone newdrone = new Drone();
		newdrone.setSerialNumber(drequest.getSerialNumber());
		return droneRepository.findBySerialNumber(newdrone.getSerialNumber());
	}

	@Override
	public LoadMedication getLoadedMedicationForADrone(String serialno) {

		return loadDroneRepository.findByDrone(serialno);
	}

	@Override
	public List<Drone> getAvailabeDrones() {
		String state = "IDLE";
		return droneRepository.findAllByState(state);
	}
	@Override
	public MessageResponse loadDrone(LoadDroneRequest loadRequest) {
		try {
			droneRepository.setUpdateState("LOADING", loadRequest.getSerialNumber());
			Drone drone = droneRepository.findBySerialNumber(loadRequest.getSerialNumber());
			Medication medication = medicationRepository.findByCode(loadRequest.getCode());

			//validate before loading
			if (drone == null) {
				return new MessageResponse("Drone specified does not exist");
			}

			if (medication == null) {
				return new MessageResponse("Medication specified does not exist");
			}

			if (drone.getWeightLimit() < medication.getWeight()) {
				return new MessageResponse("The Drone cannot load more than the weight limit");

			}
			//check battery
//			if(Integer.valueOf(drone.getBattery().toString()) <0.25){
//				return new MessageResponse("The Drone cannot loaded, battery below 25%");
//			}
			
			//load
			LoadMedication  loadMedication = new LoadMedication();
			loadMedication.setDrone(drone);
			loadMedication.setMedication(medication);
			loadMedication.setSource(loadRequest.getSource());
			loadMedication.setDestination(loadRequest.getDestination());
			loadMedication.setCreatedon(java.time.LocalDateTime.now());
			loadDroneRepository.save(loadMedication);
			droneRepository.setUpdateState("LOADED", loadRequest.getSerialNumber());

		} catch (Exception e) {
			throw new RuntimeException("Exception in loadDrone is "+ e.getMessage());
		} finally {
			//clear objects memmory
		}

		return new MessageResponse("Drone Loaded successfully");
	}

	@Override
	public MessageResponse deliverLoad(DroneDeliveryRequest loadRequest) {
		droneRepository.setUpdateState("DELIVERING", loadRequest.getSerialNumber());
		LoadMedication loadMedication =  loadDroneRepository.findByDrone(loadRequest.getSerialNumber());
		MedicalDelivery newdelivery = new MedicalDelivery();
		newdelivery.setLoadMedication(loadMedication);
		newdelivery.setDeliveryTime(java.time.LocalDateTime.now());
		droneDeliveryRepository.save(newdelivery);
		droneRepository.setUpdateState("DELIVERED", loadRequest.getSerialNumber());

		return new MessageResponse("Delivered successfully");
	}

}
