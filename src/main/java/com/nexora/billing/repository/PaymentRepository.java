package com.nexora.billing.repository;

import com.nexora.billing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByTenantId(UUID tenantId);

    List<Payment> findByInvoiceId(UUID invoiceId);
}