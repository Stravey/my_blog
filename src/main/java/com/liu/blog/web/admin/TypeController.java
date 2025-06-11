package com.liu.blog.web.admin;


import com.liu.blog.pojo.Type;
import com.liu.blog.service.TypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    /**
     *
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/types")
    public String list(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC)
                           Pageable pageable,Model model) {
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    /**
     * 后端校验
     * @return 提交页面
     */
    @GetMapping("/types/input")
    public String input(Model model) {
        // 后端校验
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    /**
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    /**
     *
     * @param type
     * @param result
     * @param attributes
     * @return
     */
    @PostMapping("/types")
    public String post(@Valid Type type,BindingResult result, RedirectAttributes attributes) {
        Type tt = typeService.getTypeByName(type.getName());
        if(tt != null) {
            result.rejectValue("name","nameExist","不能添加重复分类");
        }
        if(result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.saveType(type);
        if(t == null) {
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }

    /**
     *
     * @param type 分类
     * @param result 结果
     * @param id 分类对应ID
     * @param attributes 添加消息
     * @return 返回到分类页面
     */
    @PostMapping("/types/{id}")
    public String editPost(@Valid Type type,BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Type tt = typeService.getTypeByName(type.getName());
        if(tt != null) {
            result.rejectValue("name","nameExist","不能添加重复分类");
        }
        if(result.hasErrors()) {
            return "admin/types-input";
        }
        Type t = typeService.updateType(id,type);
        if(t == null) {
            attributes.addFlashAttribute("message","更新失败");
        } else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
