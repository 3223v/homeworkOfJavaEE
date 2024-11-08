package org.mttk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mttk.mapper.userMapper;
import org.mttk.pojo.dto.UserLoginDTO;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.Cart;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;
import org.mttk.service.userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class userServiceImpl implements userService {
    @Autowired
    private userMapper auserMapper;
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();
        User user = auserMapper.getUserByUsername(username);
        if (user == null) {
            throw new RuntimeException("没这用户");
        }
        if(!user.getPassword().equals(password)){
            throw new RuntimeException("密码不对");
        }
        if(!user.getRole().equals("user")){
            throw new RuntimeException("角色不对");
        }
        return user;
    }

    @Override
    public List<Book> getBookList() {
        return auserMapper.getBookList();
    }

    @Override
    public void buyBook(Integer userId, Integer bookId, Integer quantity) {
        Book book = auserMapper.getBookById(bookId);
        User user = auserMapper.getUserById(userId);
        if(book == null){
            throw new RuntimeException("书籍id错误");
        }
        if(quantity < 0){
            throw new RuntimeException("购买数小于0");
        }
        if(user.getBalance()>=quantity*book.getPrice()){
            Long newBalance = user.getBalance()-quantity*book.getPrice();
            auserMapper.updateUserBalance(newBalance,userId);
            auserMapper.insertOrder(userId,bookId,quantity);
        }
    }
    @Override
    public void addBook(Integer userId, Integer bookId, Integer quantity) {
        auserMapper.insertCart(userId,bookId,quantity);
    }

    @Override
    public List<CartListVO> getCartList(Integer userId) {
        return auserMapper.getCartList(userId);
    }

    @Override
    public void payCart(Integer userId, Integer cartId) {
        User user = auserMapper.getUserById(userId);
        Cart cart = auserMapper.getCartById(cartId);
        if(user == null|| cart == null){
            throw new RuntimeException("null错误");
        }
        Book book = auserMapper.getBookById(cart.getBook_id());
        if(user.getBalance()>=book.getPrice()*cart.getQuantity()){
            Long newBalance = user.getBalance()-book.getPrice()*cart.getQuantity();
            auserMapper.updateUserBalance(newBalance,userId);
            auserMapper.insertOrder(userId, book.getId(), cart.getQuantity());
            auserMapper.deleteCart(cartId);
        }else {
            throw new RuntimeException("钱不够");
        }
    }

    @Override
    public void updateCart(Integer userId, Integer cartId, Integer quantity) {
        auserMapper.updateCart(cartId,quantity);
    }

    @Override
    public void deleteCart(Integer cartId) {
        auserMapper.deleteCart(cartId);
    }

    @Override
    public List<OrderListVO> getOrderList(Integer userId) {
        return auserMapper.getOrderList(userId);
    }

    @Override
    public void deleteOrder(Integer orderId) {
        auserMapper.deleteOrder(orderId);
    }

    @Override
    public User getUserById(Integer userId) {
        return auserMapper.getUserById(userId);
    }

    @Override
    public void selfAdd(Integer userId, Long count) {
        User user = auserMapper.getUserById(userId);
        Long newBalance = user.getBalance()+count;
        auserMapper.updateUserBalance(newBalance,userId);
    }

    @Override
    public void updateSelf(Integer userId, String username, String password, String email) {
        auserMapper.updateSelf(userId,username,password,email);
    }
}
