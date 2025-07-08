package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.model.Reservation;

import java.util.List;

public interface LabReservationService {
    List<Laboratory> getLaboratories();
    Laboratory updateStatus(Long labId, Laboratory.LabStatus status);
    List<Reservation> getReservations();
    List<Reservation> getReservationsById(Long userId);
    Reservation createReservation(Reservation reservation);
    Reservation approveReservation(Long reservationId, Long adminId);
    Reservation rejectReservation(Long reservationId, Long adminId, String adminComment);
    Reservation cancelReservation(Long reservationId, Long userId);
}