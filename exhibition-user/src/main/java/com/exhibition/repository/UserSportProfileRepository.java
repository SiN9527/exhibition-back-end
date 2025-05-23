package com.exhibition.repository;

import com.hititoff.entity.UserSportProfile;
import com.hititoff.entity.UserSportProfileId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserSportProfileRepository extends JpaRepository<UserSportProfile, UserSportProfileId> {


    List<UserSportProfile> findByUserId(String id);
}