package org.mttk.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.mttk.mapper.adminMapper;
import org.mttk.mapper.userMapper;
import org.mttk.pojo.dto.UserLoginDTO;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;
import org.mttk.service.adminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class adminServiceImpl implements adminService {
    @Autowired
    private adminMapper aadminMapper;
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
        if(!user.getRole().equals("admin")){
            throw new RuntimeException("角色不对");
        }
        return user;
    }

    @Override
    public void bookAdd(Book book) {
        aadminMapper.bookAdd(book);
    }

    @Override
    public void bookUpdate(Book book) {
        aadminMapper.bookUpdate(book);
    }

    @Override
    public void deleteBook(Integer id) {
        aadminMapper.deleteBook(id);
    }

    @Override
    public List<Book> bookList() {
        return aadminMapper.bookList();
    }

    @Override
    public List<CartListVO> getCartList() {
        return aadminMapper.getCartList();
    }

    @Override
    public List<OrderListVO> getOrderList() {
        return aadminMapper.getOrderList();
    }

    @Override
    public List<User> getUserList() {
        return aadminMapper.getUserList();
    }

    @Override
    public void userAdd(User user) {
        aadminMapper.userAdd(user);
    }

    @Override
    public void userUpdate(User user) {
        aadminMapper.userUpdate(user);
    }

    @Override
    public void userDelete(Integer id) {
        User user = auserMapper.getUserById(id);
        if(user != null){
            if(user.getRole().equals("user")){
                aadminMapper.userDelete(id);
            }
        }
    }
}
