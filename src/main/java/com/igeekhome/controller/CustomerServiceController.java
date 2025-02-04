package com.igeekhome.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.igeekhome.biz.ICustomerInfoService;
import com.igeekhome.biz.ICustomerServiceService;
import com.igeekhome.pojo.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * <p>
 * 客服表 前端控制器
 * </p>
 *
 * @author ${author}
 * @since 2021-03-16
 */
@Slf4j
@Controller
@RequestMapping("/customerService")
public class CustomerServiceController {

    @Autowired
    private ICustomerServiceService iCustomerServiceService;

    @Autowired
    private ICustomerInfoService iCustomerInfoService;

    @RequestMapping("/login")
    public String checkCustomerServiceNameAndPwd(CustomerService customerService, Model model, HttpSession session)
    {

        String path="login";
       // System.out.println(customerService);
        QueryWrapper<CustomerService> qu=new QueryWrapper<>();
        qu.eq("email",customerService.getEmail());
        qu.eq("password",customerService.getPassword());
        CustomerService cs=  iCustomerServiceService.getOne(qu);
        if(cs!=null)
        {
            //客户数量
            session.setAttribute("customerCount",this.iCustomerInfoService.count());
            //客服数量
            session.setAttribute("csCount", this.iCustomerServiceService.count());

            session.setAttribute("cs", cs);
            //在数据库匹配到了邮箱和密码信息 跳转到主页
            path = "redirect:/index";
        }
        else {
            // 校验失败 返回登陆界面
            model.addAttribute("msg","用户名密码不正确");
            path = "page-login";
        }
        return path;
    }
    @GetMapping("/registry")
    public String Registry(){
        return "page-register";
    }

    /**
     * 向数据库中写入注册的信息
     * @param cs 表单提交的信息封装为CustomerService
     * @return  返回登陆界面
     */
    @PostMapping("/registry")
    public String AdminRegistry(CustomerService cs, HttpSession session, Model model) {
        //校验失败，返回注册界面，前端接收失败信息，进行提示。
        if(cs.getPassword() != "" && cs.getEmail() != "" && cs.getNickname() != ""){
            //注册成功向表中写入数据
            iCustomerServiceService.save(cs);
            //考虑做alert
            model.addAttribute("registry_msg", "注册成功");
            log.info("注册成功啦");
            return "redirect:/login";
        }
        model.addAttribute("registry_msg", "注册失败");
        log.info("注册失败了");
        return "page-register";
    }
    //修改个人信息
    @GetMapping("modify")
    public String modify(){

        return "modify-infomation";
    }
}

