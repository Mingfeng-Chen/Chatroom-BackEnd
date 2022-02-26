package com.example.demo.controller;

import com.example.demo.bean.User;
import com.example.demo.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class Controller {
    @Autowired
    UserDAO userDAO;

    @RequestMapping("/login")
    public String login(String username, String password, HttpSession session){
        System.out.println("成功接收");
        User user1=userDAO.getUser(username,password);
        if(user1!=null){
            session.setAttribute("username",username);
            return "success";
        }
        return "error";
    }
    @RequestMapping("/getUsername")
    public String getUsername(HttpSession session){
        String username=(String)session.getAttribute("username");
        return username;
    }
}
