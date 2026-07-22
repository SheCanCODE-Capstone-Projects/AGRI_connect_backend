package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.SaleRequest;
import com.scc.Agriconnect.dto.SaleResponse;
import com.scc.Agriconnect.dto.StockRequest;
import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.Customer;
import com.scc.Agriconnect.entity.Product;
import com.scc.Agriconnect.entity.Sale;
import com.scc.Agriconnect.entity.StockType;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.mapper.SaleMapper;
import com.scc.Agriconnect.repository.CustomerRepository;
import com.scc.Agriconnect.repository.ProductRepository;
import com.scc.Agriconnect.repository.SaleRepository;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final StockService stockService;
    private final SaleMapper saleMapper;

    @Transactional
    public SaleResponse recordSale(SaleRequest request) {
        User user = getCurrentUser();
        Cooperative cooperative = requireCooperative(user);

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + request.getProductId()));
        if (!product.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this product");
        }

        Customer customer = null;
        if (request.getCustomerId() != null) {
            customer = customerRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + request.getCustomerId()));
            if (!customer.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
                throw new IllegalStateException("Customer does not beUUID to this cooperative");
            }
        }

        Sale sale = saleMapper.toEntity(request);
        sale.setProduct(product);
        sale.setCooperative(cooperative);
        sale.setCustomer(customer);
        sale.setRecordedBy(user);
        sale.setSaleDate(request.getSaleDate() != null ? request.getSaleDate() : LocalDate.now());
        sale.setTotalAmount(request.getQuantitySold().multiply(product.getUnitPrice()));

        Sale savedSale = saleRepository.save(sale);

        // Insufficient stock throws here and rolls back the sale insert above too,
        // since this call joins the same transaction.
        StockRequest stockOut = StockRequest.builder()
                .productId(product.getProductId())
                .stockType(StockType.OUT)
                .quantity(request.getQuantitySold())
                .saleId(savedSale.getSaleId())
                .stockDate(sale.getSaleDate())
                .notes("Sale #" + savedSale.getSaleId() +
                        (request.getNotes() != null ? " - " + request.getNotes() : ""))
                .build();
        stockService.recordStockMovement(stockOut);

        return saleMapper.toResponse(savedSale);
    }

    public Page<SaleResponse> getCooperativeSales(Pageable pageable) {
        Cooperative cooperative = getCurrentUserCooperative();
        return saleRepository.findByCooperative_CooperativeId(cooperative.getCooperativeId(), pageable)
                .map(saleMapper::toResponse);
    }

    public SaleResponse getSaleById(UUID saleId) {
        Cooperative cooperative = getCurrentUserCooperative();
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + saleId));
        if (!sale.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this sale");
        }
        return saleMapper.toResponse(sale);
    }

    public BigDecimal getTotalRevenue() {
        Cooperative cooperative = getCurrentUserCooperative();
        return saleRepository.getTotalRevenue(cooperative.getCooperativeId());
    }

    // --- helpers ---

    private Cooperative requireCooperative(User user) {
        Cooperative cooperative = user.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative members can manage sales");
        }
        return cooperative;
    }

    private Cooperative getCurrentUserCooperative() {
        return requireCooperative(getCurrentUser());
    }

    private User getCurrentUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
