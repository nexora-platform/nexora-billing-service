package com.nexora.billing.service;

import com.nexora.billing.entity.*;
import com.nexora.billing.repository.InvoiceRepository;
import com.nexora.billing.repository.PaymentRepository;
import com.nexora.billing.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public Invoice createInvoice(UUID subscriptionId, BigDecimal amount) {

        Invoice invoice = Invoice.builder()
                .tenantId(TenantContext.getTenantId())
                .subscriptionId(subscriptionId)
                .amount(amount)
                .status(InvoiceStatus.UNPAID)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .build();

        return invoiceRepository.save(invoice);
    }

    public Payment payInvoice(UUID invoiceId, String method) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

        Payment payment = Payment.builder()
                .invoiceId(invoiceId)
                .tenantId(TenantContext.getTenantId())
                .amount(invoice.getAmount())
                .paymentMethod(method)
                .paymentDate(LocalDateTime.now())
                .build();

        return paymentRepository.save(payment);
    }

    public List<Invoice> getTenantInvoices() {
        return invoiceRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<Payment> getTenantPayments() {
        return paymentRepository.findByTenantId(TenantContext.getTenantId());
    }
}