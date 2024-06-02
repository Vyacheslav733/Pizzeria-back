package com.example.demo.orders.model;

public class ExpandedOrderEntity extends OrderEntity {
    private Double total;

    public ExpandedOrderEntity(OrderEntity order, Double total) {
        // Вызов конструктора суперкласса для установки общих полей заказа
        super(order.getUser(), order.getDate());
        this.setId(order.getId()); // Устанавливаем идентификатор заказа
        this.total = total; // Устанавливаем общую сумму
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
