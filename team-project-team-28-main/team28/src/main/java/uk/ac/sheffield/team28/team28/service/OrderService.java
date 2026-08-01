package uk.ac.sheffield.team28.team28.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.sheffield.team28.team28.dto.OrderDto;
import uk.ac.sheffield.team28.team28.model.Member;
import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.model.Order;
import uk.ac.sheffield.team28.team28.repository.MusicRepository;
import uk.ac.sheffield.team28.team28.repository.OrderRepository;

import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;


    private MusicRepository musicRepository;

    public OrderService(OrderRepository orderRepository, MusicRepository musicRepository) {
        this.orderRepository = orderRepository;
        this.musicRepository = musicRepository;
    }

    public void changeOrderStatus(Long orderId) throws Exception {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new Exception("Order not found with ID: " + orderId));
        order.setIsFulfilled(true);
        orderRepository.save(order);
    }

    public void save(OrderDto dto) {
        Order order = new Order();
        order.setMember(dto.getMember());
        order.setItem(dto.getItem());
        order.setOrderDate(dto.getOrderDate());
        order.setNote(dto.getNote());
        order.setIsFulfilled(dto.isFulfilled());

        orderRepository.save(order);
    }

    public String getArrangerForOrder(Long orderId) throws Exception {
        Music music = musicRepository.findByOrderId(orderId).orElseThrow(() ->
                new Exception("Order not found with ID: " + orderId));
        return music.getArranger();
    }

}
