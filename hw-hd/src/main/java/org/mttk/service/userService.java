package org.mttk.service;

import org.mttk.pojo.dto.UserLoginDTO;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;

import java.util.List;

public interface userService {

    User login(UserLoginDTO userLoginDTO);

     List<Book> getBookList();

    void buyBook(Integer userId, Integer bookId, Integer quantity);

    void addBook(Integer userId, Integer bookId, Integer quantity);

    List<CartListVO> getCartList(Integer userId);

    void payCart(Integer userId, Integer cartId);

    void updateCart(Integer userId, Integer cartId, Integer quantity);

    void deleteCart(Integer cartId);

    List<OrderListVO> getOrderList(Integer userId);

    void deleteOrder(Integer orderId);

    User getUserById(Integer userId);

    void selfAdd(Integer userId, Long count);

    void updateSelf(Integer userId, String username, String password, String email);
}
