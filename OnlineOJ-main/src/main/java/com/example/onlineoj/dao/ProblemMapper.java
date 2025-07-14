package com.example.onlineoj.dao;

import com.example.onlineoj.model.Label;
import com.example.onlineoj.model.Problem;
import com.example.onlineoj.model.Solution;
import com.example.onlineoj.model.Submission;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProblemMapper {
    Integer getMaxProblemId() ;
    Solution getSolutionByProblemId(Integer problemId);
    List<Submission> getSubmissionByProblemIdandName(Integer id,String user);



    int add(Problem problem);
    Problem selectOne(int id);
    List<Problem> selectAll();
    String selectLabel(int id);

    //对内测试接口
    Problem selectOneInDetail(int id);
    int update(Problem problem);
}
