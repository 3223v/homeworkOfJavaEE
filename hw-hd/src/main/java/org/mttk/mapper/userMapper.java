package org.mttk.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.Cart;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;

import java.util.List;


@Mapper
public interface userMapper {

    User getUserByUsername(@Param("username") String username);

    List<Book> getBookList();

    Book getBookById(@Param("bookId") Integer bookId);

    User getUserById(@Param("userId") Integer userId);

    void updateUserBalance(@Param("newBalance") Long newBalance,@Param("userId")Integer userId);

    void insertOrder(@Param("userId") Integer userId, @Param("bookId") Integer bookId,@Param("quantity") Integer quantity);

    void insertCart(@Param("userId") Integer userId, @Param("bookId") Integer bookId,@Param("quantity") Integer quantity);

    List<CartListVO> getCartList(@Param("userId") Integer userId);

    Cart getCartById(@Param("cartId") Integer cartId);

    void deleteCart(@Param("cartId") Integer cartId);

    void updateCart(@Param("cartId") Integer cartId,@Param("quantity") Integer quantity);

    List<OrderListVO> getOrderList(@Param("userId") Integer userId);

    void deleteOrder(@Param("orderId") Integer orderId);

    void updateSelf(@Param("userId") Integer userId, @Param("username") String username, @Param("password") String password, @Param("email") String email);
}
