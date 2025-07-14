package com.example.onlineoj.controller;


import com.example.onlineoj.dao.UserMapper;
import com.example.onlineoj.model.AllRank;
import com.example.onlineoj.model.User;
import com.example.onlineoj.model.achivement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.*;
import org.springframework.web.bind.annotation.ResponseBody;
//这里的方法已经弃用，改为上面的包
//qzy
//7.6号
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @RequestMapping("/login")
    public void login(String name, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        User findUser=userMapper.selectByName(name);
        if(findUser==null||!findUser.getPassword().equals(password)){
            response.sendRedirect("/login.html");
        }
        if (findUser != null && findUser.getRole() == 1) {
            request.getSession().setAttribute("user", findUser);
            response.sendRedirect("/admin.html");
        }
        if (findUser != null && findUser.getRole() == 0) {
            request.getSession().setAttribute("user", findUser);
            response.sendRedirect("/index.html");
        }

    }

    @RequestMapping("/register")
    public void register(String name, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        int userId = userMapper.addUser(name,password);
        // You can add additional logic here if needed, such as checking the userId value
    }
    @ResponseBody
    @GetMapping("/achievement")
    public achivement getAchievement(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            throw new RuntimeException("哥们你不登录吗");
        }
        user = userMapper.selectByName(user.getName());
        return getAchievementByUser(user);


    }
    public achivement getAchievementByUser(User user) {
        achivement achi=new achivement(0,0,0,0,0,0,0,0,0,0,0);
        achi.setAcnumber(user.getAcnumber());
        achi.setAllnumber(user.getAllnumber());
        if(user.getAcnumber()>=1)
        {
            achi.setR1(1);
        }
        if(user.getAcnumber()>=10)
        {
            achi.setR10(1);
        }
        if(user.getAcnumber()>=100)
        {
            achi.setR100(1);
        }
        if(user.getAcnumber()>=200)
        {
            achi.setR200(1);
        }
        if(user.getAcnumber()>=500)
        {
            achi.setR500(1);
        }
        if(user.getAcnumber()>=1000)
        {
            achi.setR1000(1);
        }
        int wrong=user.getAllnumber()-user.getAcnumber();
        if(wrong>=1)
        {
            achi.setW1(1);
        }
        if(wrong>=20)
        {
            achi.setW20(1);
        }
        if(wrong>=50)
        {
            achi.setW50(1);
        }
        return achi;

    }
    @ResponseBody
    @GetMapping("/getsort")
    public List<AllRank> getsort() {
        List<User> users=userMapper.selectUsersByAcnumber();
        List<AllRank> allRank=new ArrayList<>();

        for (User user : users) {
            AllRank allRank1 = new AllRank();

            // 安全获取用户数据并缓存
            String name = user.getName();
            Integer acnumberObj = user.getAcnumber();
            Integer allnumberObj = user.getAllnumber();

            int acnumber = acnumberObj == null ? 0 : acnumberObj;
            int allnumber = allnumberObj == null ? 0 : allnumberObj;

            // 设置基础信息
            allRank1.setName(name);
            allRank1.setAcnumber(acnumber);
            allRank1.setAllnumber(allnumber);

            // 计算正确率
            if (allnumber == 0) {
                allRank1.setZql(0.0);
            } else {
                double accuracy = ((double) acnumber / (double) allnumber);
                DecimalFormat DF = new DecimalFormat("0.0000");
                String formattedAccuracy = DF.format(accuracy);
                allRank1.setZql(Double.parseDouble(formattedAccuracy));
            }

            allRank.add(allRank1);
        }
        return allRank;
    }

    // UserController.java


}
