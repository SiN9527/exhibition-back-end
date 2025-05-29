 package com.exhibition.repo;


 import com.exhibition.entity.RegistrationPaymentInfoEntity;
 import org.springframework.data.jpa.repository.JpaRepository;

 public interface RegistrationPaymentInfoRepository extends JpaRepository<RegistrationPaymentInfoEntity, Long> {
     RegistrationPaymentInfoEntity findByRegistrationId(String groupLeaderId);

     RegistrationPaymentInfoEntity findByRegistrationIdAndIsGroup(String groupLeaderId, boolean b);


 }