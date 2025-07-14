package com.example.onlineoj.dao;

import com.example.onlineoj.model.Studydata;
import com.example.onlineoj.model.User;
import com.example.onlineoj.model.achivement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectByName(String name);
    int addUser(String name, String password);

    void updateUserStatistics(User user);

    void insertstudydata(Studydata studydata);
    List<User> selectUsersByAcnumber();


}
