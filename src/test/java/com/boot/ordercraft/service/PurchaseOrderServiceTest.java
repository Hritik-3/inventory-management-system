package com.boot.ordercraft.service;

import com.boot.ordercraft.dto.*;
import com.boot.ordercraft.model.*;
import com.boot.ordercraft.repository.*;
import com.boot.ordercraft.service.PurchaseOrderService;
import com.boot.ordercraft.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PurchaseOrderServiceTest {

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    @Mock
    private PurchaseOrdersRepository purchaseOrderRepository;
    @Mock
    private PurchaseOrderItemsRepository purchaseOrderItemRepository;
    @Mock
    private SuppliersRepository supplierRepository;
    @Mock
    private ProductsRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RawMaterialsRepository rawMaterialsRepository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createPurchaseOrder_shouldSucceed() {
        String username = "testuser";
        User user = new User(); user.setUserId("1"); user.setUserEmail(username);
        Supplier supplier = new Supplier(); supplier.setSuppliersId(1L);

        PurchaseOrderCreateRequest request = new PurchaseOrderCreateRequest();
        request.setSupplierId(1L);
        request.setPoDeliveryStatus("PENDING");
        request.setPoOrderDate(LocalDateTime.now());
        PurchaseOrderItemRequest itemReq = new PurchaseOrderItemRequest();
        itemReq.setrWId(1L); itemReq.setQuantity(5); itemReq.setCost((float) 100.0);
        request.setItems(List.of(itemReq));

        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setRwId(1L); rawMaterial.setRwQuantity(10); rawMaterial.setRwName("Steel");

        when(userService.getUserByUsername(username)).thenReturn(Optional.of(user));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(purchaseOrderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(rawMaterialsRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(rawMaterialsRepository.save(any())).thenReturn(rawMaterial);
        when(purchaseOrderItemRepository.save(any())).thenReturn(new PurchaseOrderItem());

        ResponseEntity<String> response = purchaseOrderService.createPurchaseOrder(request, username);
        assertEquals("Order Placed successfully.", response.getBody());
    }

    @Test
    void createInternalOrder_shouldSucceed() {
        String username = "testuser";
        User user = new User(); user.setUserEmail(username);
        Supplier bgsw = new Supplier(); bgsw.setSuppliersName("BGSW");
        Product product = new Product(); product.setProductsId(1L); product.setProductsQuantity(10); product.setProductsName("Test"); product.setProductsUnitPrice((float) 50.0);

        OrderforCustomer item = new OrderforCustomer(); item.setProductsId(1L); item.setQuantity(2);
        PurchaseOrderInternalRequest request = new PurchaseOrderInternalRequest();
        request.setItems(List.of(item));

        when(userRepository.findByEmail(username)).thenReturn(Optional.of(user));
        when(supplierRepository.findBySuppliersName("BGSW")).thenReturn(Optional.of(bgsw));
        when(purchaseOrderRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any())).thenReturn(product);
        when(purchaseOrderItemRepository.save(any())).thenReturn(new PurchaseOrderItem());

        assertDoesNotThrow(() -> purchaseOrderService.createInternalOrder(request, username));
    }

    @Test
    void updateOrderAndItems_shouldUpdateSuccessfully() {
        Product product = new Product(); product.setProductsId(1L); product.setProductsQuantity(100); product.setProductsName("Widget");
        PurchaseOrder order = new PurchaseOrder(); order.setPoDeliveryStatus("PENDING");
        PurchaseOrderItem item = new PurchaseOrderItem(); item.setPoiQuantity(5); item.setProduct(product);
        item.setPoiId(1L);

        PurchaseOrderUpdateRequest.ItemUpdateRequest itemUpdate = new PurchaseOrderUpdateRequest.ItemUpdateRequest();
        itemUpdate.setItemId(1L); itemUpdate.setNewQuantity(7); itemUpdate.setNewCost((float) 90.0);
        PurchaseOrderUpdateRequest request = new PurchaseOrderUpdateRequest();
        request.setItems(List.of(itemUpdate));

        when(purchaseOrderRepository.findById(any())).thenReturn(Optional.of(order));
        when(purchaseOrderItemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(productRepository.save(any())).thenReturn(product);
        when(purchaseOrderItemRepository.save(any())).thenReturn(item);

        //assertDoesNotThrow(() -> purchaseOrderService.updateOrderAndItems(1L, request, "admin"));
    }

    @Test
    void cancelOrder_shouldSucceed() {
        Principal principal = () -> "testuser";
        Product product = new Product(); product.setProductsQuantity(10);
        PurchaseOrder order = new PurchaseOrder(); order.setPoDeliveryStatus("PENDING");
        PurchaseOrderItem item = new PurchaseOrderItem(); item.setProduct(product); item.setPoiQuantity(5);

        when(purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(purchaseOrderItemRepository.findByPurchaseOrder(order)).thenReturn(List.of(item));
        when(productRepository.save(any())).thenReturn(product);

        boolean result = purchaseOrderService.cancelOrder(1L, principal);
        assertTrue(result);
    }
}
