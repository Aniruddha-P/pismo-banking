package com.pismo.accounts.respository;

import com.pismo.accounts.entity.AccountEntity;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

@Hidden
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    Optional<AccountEntity> findById(Long id);

    AccountEntity save(AccountEntity accountEntity);
}
