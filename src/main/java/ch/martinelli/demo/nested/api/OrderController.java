package ch.martinelli.demo.nested.api;

import ch.martinelli.demo.nested.repository.PurchaseOrderDTO;
import ch.martinelli.demo.nested.repository.PurchaseOrderRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("orders")
@RestController
class OrderController {

    private final PurchaseOrderRepository purchaseOrderRepository;

    OrderController(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @GetMapping
    List<PurchaseOrderDTO> getPurchaseOrders() {
        return purchaseOrderRepository.findAll();
    }
}
