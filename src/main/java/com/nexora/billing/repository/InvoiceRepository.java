package com.nexora.billing.repository;

import com.nexora.billing.entity.Invoice;
import com.nexora.billing.entity.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {

    List<Invoice> findByTenantId(UUID tenantId);

    List<Invoice> findByTenantIdAndStatus(UUID tenantId, InvoiceStatus status);
}