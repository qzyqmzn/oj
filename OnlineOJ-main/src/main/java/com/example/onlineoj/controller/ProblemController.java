package com.example.onlineoj.controller;


import com.example.onlineoj.api.aliapi;
import com.example.onlineoj.component.FileUtil;
import com.example.onlineoj.component.Sort;
import com.example.onlineoj.component.Task;
import com.example.onlineoj.dao.ProblemMapper;
import com.example.onlineoj.dao.UserMapper;
import com.example.onlineoj.model.*;
import com.example.onlineoj.tools.UserContext;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Controller
public class ProblemController
{
    private final ProblemMapper problemMapper;
    private final Task task;
    private final FileUtil fileUtil;

    private final Sort sort;
    @Autowired
    private aliapi aliapi1;
    @Autowired
    private UserMapper userMapper;

    public ProblemController(ProblemMapper problemMapper, Task task, FileUtil fileUtil, Sort sort) {
        this.problemMapper = problemMapper;
        this.task = task;
        this.fileUtil = fileUtil;
        this.sort = sort;
    }
    // 通过读文件修改一个题目，需要提前修改题目信息
    @ResponseBody
    @RequestMapping("/user/change")
    public Object updateProblem(Integer id){
        if(id==null) {
            return "id错误";
        }
        Problem problem=fileUtil.getProblemFromFile(id);
        if(problem==null){
            return "确认文件是否存在";
        }
        problem.setId(id);
        int ret=problemMapper.update(problem);
        if(ret>0){
            return "成功,请点击回退";
        }else{
            return "失败,请点击回退";
        }
    }
    // 通过读文件来上传一下新的题目
    @ResponseBody
    @RequestMapping("/user/submit")
    public Object addProblem(Integer id){
        if(id==null) {
            return "id错误,请返回重试";
        }
        Problem isAdmitUpload=problemMapper.selectOne(id);
        if(isAdmitUpload!=null){
            return "该题目已经存在，如果需要请点击修改";
        }
        Problem problem=fileUtil.getProblemFromFile(id);
        if(problem==null){
            return "确认文件是否存在";
        }
        problem.setId(id);
        int ret=problemMapper.add(problem);
        if(ret>0){
            return "成功上传一个题目";
        }else{
            return "失败,原因未知";
        }
    }



    // 查看题目，id为空查看题目列表，确定数字查看具体的题目
    @ResponseBody
    @RequestMapping("/problem")
    public Object getAll(Integer id){
        if(id!=null){
            Problem problem = problemMapper.selectOne(id);
            String code="//已自动为你导入 import java.util.* 如需需要其他，请自行导入\n"+problem.getTemplateCode();
            problem.setTemplateCode(code);
            return problem;
        }else{
            List<Problem> problems=problemMapper.selectAll();
            problems.forEach(problem -> {
                String des= problemMapper.selectLabel(problem.getId());
                problem.setDescription(des);
            });
            return problems;
        }

    }
    //获取最大题目数量
    @ResponseBody
    @RequestMapping("/maxProblemId")
    public Integer getMaxProblemId() {
        return problemMapper.getMaxProblemId();
    }
    // 题目的编译运行模块
    @ResponseBody
    @RequestMapping("/compile")
    public Object compile(@RequestBody CompileRequest compileRequest, HttpSession session) {
        CompileResponse compileResponse=new CompileResponse();
        try{
            // 获取题目的其他代码信息，以便进行代码拼接
            Problem problem=problemMapper.selectOneInDetail(compileRequest.id);
            if(problem==null){
                compileResponse.error = 3;
                compileResponse.reason = "没有找到指定的题目! id=" + compileRequest.id;
                throw new Exception("未查询到题目");
            }
            //todo
            /*
            @qzy
            7.6
            下面这个if else 判断，如果题目的测试代码为0，则使用大模型进行判断，否则使用本地进行判断
            */

            if(problem.getTestCode().equals("0"))
            {
                String thisquestion=problem.getDescription();
                String thisusercode=compileRequest.code;
                String alltext="题目："+thisquestion+"题解："+thisusercode;
                compileResponse.stdout= aliapi.callWithMessage1(alltext);
                String thisanswer=compileResponse.stdout;

                compileResponse.error=1;
                if(aianswercontain(thisanswer)==1)
                {
                    compileResponse.error=0;//对了
                }

                compileResponse.stdout=compileResponse.stdout.replace("\\n","\n");
                compileResponse.reason=compileResponse.stdout;

                //这里是向数据库增加做题数
                //正确是0 todo
                //添加了做题的数据
                //7.8号
                addzuotidata(compileResponse,UserContext.getCurrentUsername(session));


                addzuotidate(compileRequest,UserContext.getCurrentUsername(session),aianswercontain(thisanswer));





                return compileResponse;


            }
            String testCode = problem.getTestCode();
            String requestCode = compileRequest.code;
            if(!task.checkCodeSafe(requestCode)){
                compileResponse.error = 3;
                compileResponse.reason = "提交代码存在非法控制代码,终止运行,请输入题解相关代码";
                throw new Exception("恶意代码");
            }
            String finalCode = mergeCode(requestCode, testCode);
            if (finalCode == null) {
                compileResponse.error = 3;
                compileResponse.reason = "提交的代码不符合要求!";
                throw new Exception();
            }
            Question question = new Question();
            question.setCode(finalCode);
            // 代码的编译运行命令
            Answer answer = task.run(question);
            compileResponse.error = answer.getError();
            if(compileResponse.error==0){
                //正常运行
                int index1 = answer.getStdout().lastIndexOf("is ");
                int index2 = answer.getStdout().indexOf(" ms");
                System.out.println("index1="+index1+" index2 = " + index2);
                if(index1==-1||index2==-1){
                    //这里是没有代码编译错误、没有运行错误。
                    //可能结果是代码的结果错误或者超时被杀死
                    //如果是结果错误，我们约定打印Last Input。如果是超时，则什么也没有
                    int tmp = answer.getStdout().lastIndexOf("Last Input");
                    if(tmp>=0){
                        compileResponse.stdout = answer.getStdout();
                    }else{
                        compileResponse.stdout = "程序超时,请优化时间复杂度或者检测死循环";
                    }
                }else{
                    System.out.println(answer.getStdout().substring(index1+3,index2));
                    String s1 = answer.getStdout().substring(index1+3,index2);
                    Integer t = Integer.valueOf(s1);
                    System.out.println("t = "+t);
                    String s = sort.operation(compileRequest.id,t);
                    System.out.println("s = " + s);
                    compileResponse.stdout=answer.getStdout() +"\n" + s;
                }
            }else if(compileResponse.error==1){
                //编译错误
                compileResponse.reason=answer.getCompileErr();
            }else{
                //运行错误
                compileResponse.reason=answer.getStderr();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        addzuotidata(compileResponse,UserContext.getCurrentUsername(session));
        int shifou=0;//错误
        if(compileResponse.error==0)
        {
            shifou=1;
        }

        addzuotidate(compileRequest,UserContext.getCurrentUsername(session),shifou);
        return compileResponse;
    }

    @ResponseBody
    @GetMapping("/solution")
    public Object getSolution(@RequestParam Integer id, HttpSession session) {
        if (id == null || id <= 0) {
            return "题目ID无效";
        }

        Solution solution = problemMapper.getSolutionByProblemId(id);
        if (solution == null) {
            return "未找到该题目的题解";
        }

        return solution.getContent();
    }
    @ResponseBody
    @GetMapping("/submissions")
    public Object getSubmission(@RequestParam Integer id, HttpSession session) {
        List<Submission> submissions = problemMapper.getSubmissionByProblemIdandName(id,UserContext.getCurrentUsername(session));

        return submissions;

    }


    private String mergeCode(String requestCode, String testCode) {
        String preCode="import java.util.*;\n";
        int pos = requestCode.lastIndexOf("}");
        if (pos == -1) {
            return null;
        }
        String subStr = requestCode.substring(0, pos);
        return preCode+subStr + testCode + "\n}";
    }
    public int aianswercontain(String string)
    {//1是正确，0是错误
        if (string == null || string.isEmpty()) {
            return 0;
        }

        // 只检查前10个字符
        String prefix = string.length() > 10 ? string.substring(0, 10) : string;

        if (prefix.contains("错误")) {
            return 0;
        }
        if (prefix.contains("正确")) {
            return 1;
        }
        if (prefix.contains("您已击败")) {
            return 1;
        }

        return 0;

    }

    public void addzuotidata(CompileResponse compileResponse, String name)
    {

        User user = userMapper.selectByName(name);

        if (user != null)
        {   if (compileResponse.stdout == null) {
            compileResponse.stdout = "请输入代码";
            }

            if (aianswercontain(compileResponse.stdout) == 1)
            {
            // 答题正确：同时增加总提交数和通过数
            user.setAllnumber(user.getAllnumber() + 1);
            user.setAcnumber(user.getAcnumber() + 1);
            }
            else
            {
            // 答题错误：只增加总提交数
            user.setAllnumber(user.getAllnumber() + 1);
            }

        // 更新数据库中的用户统计信息
        userMapper.updateUserStatistics(user);
        }

    }
    public void addzuotidate(CompileRequest compileRequest , String name, int shifou)
    {   String time=LocalDateTime.now().toString();
        int id=compileRequest.id;
        Studydata studydata=new Studydata();
        studydata.setId(id);
        studydata.setUser(name);
        studydata.setDate(time);

        studydata.setYesno(shifou);//0是错误，1是正确
        userMapper.insertstudydata(studydata);

    }




}
