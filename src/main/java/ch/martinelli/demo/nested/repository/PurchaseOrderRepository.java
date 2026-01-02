package ch.martinelli.demo.nested.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;

import static ch.martinelli.demo.nested.db.tables.OrderItem.ORDER_ITEM;
import static ch.martinelli.demo.nested.db.tables.PurchaseOrder.PURCHASE_ORDER;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.*;

@Repository
public class PurchaseOrderRepository {

    private final DSLContext ctx;

    public PurchaseOrderRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<PurchaseOrderDTO> findAll() {
        return ctx.select(PURCHASE_ORDER.ID,
                        PURCHASE_ORDER.ORDER_DATE,
                        row(PURCHASE_ORDER.customer().ID,
                                PURCHASE_ORDER.customer().FIRST_NAME,
                                PURCHASE_ORDER.customer().LAST_NAME,
                                PURCHASE_ORDER.customer().STREET,
                                PURCHASE_ORDER.customer().POSTAL_CODE,
                                PURCHASE_ORDER.customer().CITY
                        ).mapping(CustomerDTO::new),
                        multiset(
                                select(ORDER_ITEM.ID,
                                        ORDER_ITEM.QUANTITY,
                                        row(ORDER_ITEM.product().ID,
                                                ORDER_ITEM.product().NAME,
                                                ORDER_ITEM.product().PRICE
                                        ).mapping(ProductDTO::new))
                                        .from(ORDER_ITEM)
                                        .where(ORDER_ITEM.PURCHASE_ORDER_ID.eq(PURCHASE_ORDER.ID))
                                        .orderBy(ORDER_ITEM.ID)
                        ).convertFrom(r -> r.map(mapping(OrderItemDTO::new))))
                .from(PURCHASE_ORDER)
                .orderBy(PURCHASE_ORDER.ORDER_DATE)
                .fetch(mapping(PurchaseOrderDTO::new));
    }
}
