// VehicleRepository.java in repository package
package com.quick_park_assist.repository;

import com.quick_park_assist.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByUserId(Long userId);
    Optional<Vehicle> findByIdAndUserId(Long id, Long userId);
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.user.id = :userId")
    Long countByUserId(@Param("userId") Long userId);
}