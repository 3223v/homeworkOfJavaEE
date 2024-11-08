package org.mttk.controller;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.mttk.config.JwtProperties;
import org.mttk.pojo.dto.UserLoginDTO;
import org.mttk.pojo.entity.Book;
import org.mttk.pojo.entity.Cart;
import org.mttk.pojo.entity.Order;
import org.mttk.pojo.entity.User;
import org.mttk.pojo.vo.CartListVO;
import org.mttk.pojo.vo.OrderListVO;
import org.mttk.pojo.vo.UserLoginVO;
import org.mttk.service.adminService;
import org.mttk.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/admin")
public class adminController {
    @Autowired
    private adminService aadminservice;
    @Autowired
    private JwtProperties jwtProperties;
    @PostMapping("/login")
    public ResponseEntity<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("登录:{}", userLoginDTO);
        User user = aadminservice.login(userLoginDTO);
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId",user.getId().toString());
        String token = JwtUtil.createJWT(
                jwtProperties.getSecretKey(),
                jwtProperties.getTtl(),
                claims);
        UserLoginVO userLoginVO = UserLoginVO.builder()
                .token(token)
                .id(user.getId())
                .build();
        return ResponseEntity.ok(userLoginVO);
    }
    //书籍，增删改查
    @GetMapping("/book/list")
    public ResponseEntity<List<Book>> getBookList() {
        List<Book> lsb = aadminservice.bookList();
        return ResponseEntity.ok(lsb);
    }
    @PostMapping("/book/add")
    public ResponseEntity<String> bookAdd(@RequestBody Book book, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.bookAdd(book);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/book/update")
    public ResponseEntity<String> bookUpdate(@RequestBody Book book, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.bookUpdate(book);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/book/delete/{id}")
    public ResponseEntity<String> bookDelete(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.deleteBook(id);
        return ResponseEntity.ok("ok");
    }
    //订单查
    @GetMapping("order/list")
    public ResponseEntity<List<OrderListVO>> getOrderList() {
        List<OrderListVO> olv = aadminservice.getOrderList();
        return ResponseEntity.ok(olv);
    }
    //购物车查
    @GetMapping("/cart/list")
    public ResponseEntity<List<CartListVO>> getCartList() {
        List<CartListVO> cartList = aadminservice.getCartList();
        return ResponseEntity.ok(cartList);
    }
    //role=user的用户增删改查
    @GetMapping("/user/list")
    public ResponseEntity<List<User>> getUserList() {
        List<User> lu = aadminservice.getUserList();
        return ResponseEntity.ok(lu);
    }
    @PostMapping("/user/add")
    public ResponseEntity<String> userAdd(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.userAdd(user);
        return ResponseEntity.ok("ok");
    }
    @PostMapping("/user/update")
    public ResponseEntity<String> userUpdate(@RequestBody User user, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.userUpdate(user);
        return ResponseEntity.ok("ok");
    }
    @GetMapping("/user/delete/{id}")
    public ResponseEntity<String> userDelete(@PathVariable Integer id, HttpServletRequest request) {
        String token = request.getHeader(jwtProperties.getTokenName());
        log.debug("token: {}", token);
        if (token == null||token.isEmpty()) {
            throw new RuntimeException("jwt不对");
        }
        log.info("jwt校验:{}", token);
        Claims claims = JwtUtil.parseJWT(jwtProperties.getSecretKey(), token);
        Integer adminId = Integer.valueOf(claims.get("adminId").toString());
        log.info("当前员工id：", adminId);
        aadminservice.userDelete(id);
        return ResponseEntity.ok("ok");
    }
}
