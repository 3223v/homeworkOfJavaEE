package org.mttk.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;

import java.util.List;

@Mapper
public interface adminMapper {
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
