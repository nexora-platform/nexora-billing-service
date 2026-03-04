package com.nexora.billing.controller;

import com.nexora.billing.entity.Invoice;
import com.nexora.billing.entity.Payment;
import com.nexora.billing.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    public Invoice createInvoice(@RequestParam UUID subscriptionId,
                                 @RequestParam BigDecimal amount) {
        return billingService.createInvoice(subscriptionId, amount);
    }

    @PostMapping("/invoices/{invoiceId}/pay")
    public Payment payInvoice(@PathVariable UUID invoiceId,
                              @RequestParam String method) {
        return billingService.payInvoice(invoiceId, method);
    }

    @GetMapping("/invoices")
    public List<Invoice> getInvoices() {
        return billingService.getTenantInvoices();
    }

    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return billingService.getTenantPayments();
    }
}