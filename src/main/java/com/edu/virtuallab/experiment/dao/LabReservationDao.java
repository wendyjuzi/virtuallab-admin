package com.edu.virtuallab.experiment.dao;

import com.edu.virtuallab.experiment.model.Laboratory;
import com.edu.virtuallab.experiment.model.Reservation;
import com.edu.virtuallab.auth.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface LabReservationDao{
    @Select("SELECT * FROM laboratories")
    List<Laboratory> getAllLaboratories();

    @Select("SELECT " +
            "id as labId, " +
            "lab_name as labName, " +
            "location, " +
            "capacity, " +
            "description, " +
            "status " +
            "FROM laboratories " +
            "WHERE id = #{labId}")
    Laboratory getLabById(@Param("labId") Long labId);

    @Update("UPDATE laboratories SET" +
            "status = #{status}, updated_at = NOW()" +
            "WHERE id = #{labId}")
    int updateStatus(@Param("labId") Long labId, @Param("status")Laboratory.LabStatus status);

    @Select("SELECT r.*, l.lab_name, u.username " +
            "FROM reservations r " +
            "LEFT JOIN laboratories l ON r.lab_id = l.lab_id " +
            "LEFT JOIN user u ON r.user_id = u.id")
    List<Reservation> getAllReservations();

    @Select("SELECT r.*, l.lab_name " +
            "FROM reservations r " +
            "JOIN laboratories l ON r.lab_id = l.lab_id " +
            "WHERE r.user_id = #{userId}")
    List<Reservation> getReservationsById(@Param("userId") Long userId);
    @Select("SELECT * FROM reservations WHERE reservation_id = #{reservationId}")
    Reservation findById(Long reservationId);
    @Select("SELECT r.*, l.lab_name FROM reservations r " +
            "JOIN laboratories l ON r.lab_id = l.lab_id " +
            "WHERE r.status = #{status}")
    List<Reservation> findByStatus(@Param("status") Reservation.ReservationStatus status);

    // 插入与更新
    @Insert("INSERT INTO reservations(user_id, lab_id, start_time, end_time, purpose, status, created_at) " +
            "VALUES(#{userId}, #{laboratory.labId}, #{startTime}, #{endTime}, #{purpose}, #{status}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "reservationId")
    int saveReservation(Reservation reservation);

    @Update("UPDATE reservations SET " +
            "status = #{status}, " +
            "admin_id = #{adminId}, " +
            "admin_comment = #{adminComment} " +
            "WHERE reservation_id = #{reservationId}")
    int updateStatus(Reservation reservation);


}