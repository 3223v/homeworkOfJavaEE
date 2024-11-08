package org.mttk.service;

import org.mttk.pojo.dto.UserLoginDTO;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;

import java.util.List;

public interface adminService {
    User login(UserLoginDTO userLoginDTO);

    void bookAdd(Book book);

    void bookUpdate(Book book);

    void deleteBook(Integer id);

    List<Book> bookList();

    List<CartListVO> getCartList();

    List<OrderListVO> getOrderList();

    List<User> getUserList();

    void userAdd(User user);

    void userUpdate(User user);

    void userDelete(Integer id);
}
