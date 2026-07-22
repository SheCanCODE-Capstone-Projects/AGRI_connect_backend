package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.StockRequest;
import com.scc.Agriconnect.dto.StockResponse;
import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.Product;
import com.scc.Agriconnect.entity.Sale;
import com.scc.Agriconnect.entity.Stock;
import com.scc.Agriconnect.entity.StockType;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.mapper.StockMapper;
import com.scc.Agriconnect.repository.ProductRepository;
import com.scc.Agriconnect.repository.SaleRepository;
import com.scc.Agriconnect.repository.StockRepository;
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
public class StockService {

    private final StockRepository stockRepository;
    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final StockMapper stockMapper;

    @Transactional
    public StockResponse recordStockMovement(StockRequest request) {
        User user = getCurrentUser();
        Product product = getOwnedProductForUpdate(request.getProductId(), user);

        BigDecimal currentStock = product.getCurrentStockLevel() != null
                ? product.getCurrentStockLevel() : BigDecimal.ZERO;

        Sale sale = null;
        if (request.getStockType() == StockType.OUT) {
            if (currentStock.compareTo(request.getQuantity()) < 0) {
                throw new IllegalArgumentException(
                        "Insufficient stock. Available: " + currentStock + ", requested: " + request.getQuantity());
            }
            if (request.getSaleId() != null) {
                sale = saleRepository.findById(request.getSaleId())
                        .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + request.getSaleId()));
                if (!sale.getProduct().getProductId().equals(product.getProductId())) {
                    throw new IllegalArgumentException("Sale does not match the product");
                }
            }
        } else if (request.getSaleId() != null) {
            throw new IllegalArgumentException("Sale ID must not be set for a STOCK IN movement");
        }

        BigDecimal newStock = request.getStockType() == StockType.IN
                ? currentStock.add(request.getQuantity())
                : currentStock.subtract(request.getQuantity());

        Stock stock = stockMapper.toEntity(request);
        stock.setProduct(product);
        stock.setSale(sale);
        stock.setRecordedBy(user);
        stock.setStockDate(request.getStockDate() != null ? request.getStockDate() : LocalDate.now());

        Stock savedStock = stockRepository.save(stock);

        product.setCurrentStockLevel(newStock);
        productRepository.save(product);

        return stockMapper.toResponse(savedStock);
    }

    public Page<StockResponse> getProductStockHistory(UUID productId, Pageable pageable) {
        getOwnedProduct(productId, getCurrentUser());
        return stockRepository.findByProduct_ProductId(productId, pageable).map(stockMapper::toResponse);
    }

    public Page<StockResponse> getCooperativeStockHistory(Pageable pageable) {
        Cooperative cooperative = getCurrentUserCooperative();
        return stockRepository.findByProduct_Cooperative_CooperativeId(cooperative.getCooperativeId(), pageable)
                .map(stockMapper::toResponse);
    }

    public BigDecimal getCurrentStock(UUID productId) {
        Product product = getOwnedProduct(productId, getCurrentUser());
        return product.getCurrentStockLevel() != null ? product.getCurrentStockLevel() : BigDecimal.ZERO;
    }

    // --- helpers ---

    private Product getOwnedProductForUpdate(UUID productId, User user) {
        Cooperative cooperative = requireCooperative(user);
        Product product = productRepository.findByIdForUpdate(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        assertSameCooperative(product, cooperative);
        return product;
    }

    private Product getOwnedProduct(UUID productId, User user) {
        Cooperative cooperative = requireCooperative(user);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        assertSameCooperative(product, cooperative);
        return product;
    }

    private void assertSameCooperative(Product product, Cooperative cooperative) {
        if (!product.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this product");
        }
    }

    private Cooperative requireCooperative(User user) {
        Cooperative cooperative = user.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative members can manage stock");
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
