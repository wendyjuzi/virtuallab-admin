package com.edu.virtuallab.experiment.service;

import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.model.Reservation;

import java.util.List;

public interface LabReservationService {
    List<Laboratory> getLaboratories();
    List<Reservation> getReservations();
    List<Reservation> getReservationsById(Long userId);
    Reservation createReservation(Reservation reservation);
    Reservation approveReservation(Long reservationId);
    Reservation rejectReservation(Long reservationId);
    Reservation cancelReservation(Long reservationId, Long userId);
}