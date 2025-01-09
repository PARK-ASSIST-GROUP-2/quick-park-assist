package com.quick_park_assist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.quick_park_assist.entity.ServiceEntity;

public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

}
