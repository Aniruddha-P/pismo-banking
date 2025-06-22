package com.pismo.accounts.respository;

import com.pismo.accounts.entity.AccountEntity;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Hidden
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findById(Long id);

    AccountEntity save(AccountEntity accountEntity);
}
