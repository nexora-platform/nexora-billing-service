package com.nexora.billing.service;

import com.nexora.billing.entity.*;
import com.nexora.billing.repository.InvoiceRepository;
import com.nexora.billing.repository.PaymentRepository;
import com.nexora.billing.tenant.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public Invoice createInvoice(UUID subscriptionId, BigDecimal amount) {

        log.info("Creating invoice for tenant={} subscription={} amount={}",
                TenantContext.getTenantId(), subscriptionId, amount);

        Invoice invoice = Invoice.builder()
                .tenantId(TenantContext.getTenantId())
                .subscriptionId(subscriptionId)
                .amount(amount)
                .status(InvoiceStatus.UNPAID)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(30))
                .build();

        Invoice saved = invoiceRepository.save(invoice);

        log.info("Invoice created successfully id={}", saved.getId());

        return saved;
    }

    public Payment payInvoice(UUID invoiceId, String method) {

        log.info("Processing payment invoice={} tenant={}",
                invoiceId, TenantContext.getTenantId());

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

        Payment saved = paymentRepository.save(payment);

        log.info("Payment completed invoice={} amount={}", invoiceId, saved.getAmount());

        return saved;
    }

    public List<Invoice> getTenantInvoices() {

        log.info("Fetching invoices for tenant {}", TenantContext.getTenantId());

        return invoiceRepository.findByTenantId(TenantContext.getTenantId());
    }

    public List<Payment> getTenantPayments() {

        log.info("Fetching payments for tenant {}", TenantContext.getTenantId());

        return paymentRepository.findByTenantId(TenantContext.getTenantId());
    }
}