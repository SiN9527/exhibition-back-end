package com.exhibition.repository;


import com.exhibition.entity.MemberEventEntity;
import com.exhibition.entity.MemberEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberEventRepository extends JpaRepository<MemberEventEntity, MemberEventPkEntity> {

    @Query("SELECT m.memberId FROM MemberEventEntity m WHERE m.eventId = :eventId")
    List<String> findMemberIdsByEventId(String eventId);



    void deleteByMemberIdAndEventId(String memberId, String eventId);
}