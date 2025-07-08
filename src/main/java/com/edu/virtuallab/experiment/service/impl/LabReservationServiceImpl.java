package com.edu.virtuallab.experiment.service.impl;

import com.edu.virtuallab.common.exception.BusinessException;
import com.edu.virtuallab.experiment.dao.LabReservationDao;
import com.edu.virtuallab.experiment.model.ExperimentReport;
import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.model.Reservation;
import com.edu.virtuallab.experiment.service.LabReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class LabReservationServiceImpl implements LabReservationService {

    private final LabReservationDao labReservationDao;

    @Autowired
    public LabReservationServiceImpl(LabReservationDao labReservationDao) {
        this.labReservationDao = labReservationDao;
    }

    @Override
    public List<Laboratory> getLaboratories() {
        log.info("开始查询所有实验室信息");
        List<Laboratory> laboratories = labReservationDao.getAllLaboratories();
        log.info("查询到{}条实验室信息", laboratories.size());
        return laboratories;
    }

    @Override
    public List<Reservation> getReservations() {
        log.info("开始查询所有预约信息");
        List<Reservation> reservations = labReservationDao.getAllReservations();
        log.info("查询到{}条预约记录", reservations.size());
        return reservations;
    }

    @Override
    public List<Reservation> getReservationsById(Long userId) {
        log.info("开始查询用户{}预约信息", userId);
        List<Reservation> reservations = labReservationDao.getReservationsById(userId);
        log.info("查询到{}条预约记录", reservations.size());
        return reservations;
    }

    @Override
    @Transactional
    public Reservation createReservation(Reservation reservation){
        reservation.setStatus(Reservation.ReservationStatus.pending);
        reservation.setCreatedAt(LocalDateTime.now());
        labReservationDao.saveReservation(reservation);

        return reservation;
    }

    @Override
    @Transactional
    public Reservation approveReservation(Long reservationId, Long adminId){
        Reservation reservation = labReservationDao.findById(reservationId);
        reservation.setStatus(Reservation.ReservationStatus.approved);
        reservation.setAdminId(adminId);
        labReservationDao.updateStatus(reservation);

        return reservation;
    }

   @Override
   @Transactional
   public Reservation rejectReservation(Long reservationId, Long adminId, String adminComment){
        Reservation reservation = labReservationDao.findById(reservationId);
        if(StringUtils.isBlank(adminComment)){
            throw new IllegalArgumentException("必须填写拒绝理由");
        }
        reservation.setStatus(Reservation.ReservationStatus.rejected);
        reservation.setAdminId(adminId);
        reservation.setAdminComment(adminComment);
        labReservationDao.updateStatus(reservation);

        return reservation;

   }
   @Override
   @Transactional
    public Reservation cancelReservation(Long reservationId, Long userId){
       Reservation reservation = labReservationDao.findById(reservationId);

       // 检查预约状态
       if ("approved".equals(reservation.getStatus()) || "rejected".equals(reservation.getStatus())) {
           throw new BusinessException("已批准或已拒绝的预约不能取消");
       }

       reservation.setStatus(Reservation.ReservationStatus.cancelled);
       labReservationDao.updateStatus(reservation);

       return reservation;
   }

    @Override
    @Transactional
    public Laboratory updateStatus(Long labId, Laboratory.LabStatus status) {
        // 先验证实验室是否存在
        Laboratory lab = labReservationDao.getLabById(labId);
        if (lab == null) {
            throw new RuntimeException("实验室不存在");
        }

        // 验证状态值是否合法
        if (!Arrays.asList("available", "unavailable", "maintenance").contains(status)) {
            throw new RuntimeException("无效的状态值");
        }

        // 执行更新
        log.info("将实验室{}状态更改为{}:",labId, status);
        return updateStatus(labId, status);
    }
}
