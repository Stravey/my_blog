package com.liu.blog.web.admin;


import com.liu.blog.pojo.User;
import com.liu.blog.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     *
     * @return 登录页面
     */
    @GetMapping
    public String loginPage(){
        return "admin/login";
    }

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @param session Http服务器端对象
     * @param attributes 添加重定向内容
     * @return 首页或者登录页面
     */
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
        User user = userService.checkUser(username,password);
        if(user != null){
            user.setPassword(null);
            session.setAttribute("user",user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message","用户名和密码错误");
            // 重定向登陆失败再次返回登录页面
            return "redirect:admin/login";
        }
    }

    /**
     *
     * @param session Http服务器端对象
     * @return 重定向管理页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }

}
