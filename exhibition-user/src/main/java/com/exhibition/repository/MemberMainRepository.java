package com.exhibition.repository;


import com.exhibition.entity.member.MemberMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberMainRepository extends JpaRepository<MemberMainEntity, String> {

    Optional<MemberMainEntity> findByEmail(String email); // 根據 email 查找會員

    Boolean existsByEmail(String email);



    boolean existsByEmailAndPassword(String email, String password);

    List<MemberMainEntity> findAllByMemberIdIn(List<String> memberIds);

    Optional<MemberMainEntity> findByMemberId(String id);
}
