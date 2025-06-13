package com.liu.blog.web.admin;


import com.liu.blog.pojo.Tag;
import com.liu.blog.service.TagService;
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
public class TagController {

    private static final String VIEW = "admin/tag";

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC)
                       Pageable pageable, Model model) {
        model.addAttribute("page",tagService.listTags(pageable));
        return "admin/types";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        // 后端校验
        model.addAttribute("tag",new Tag());
        return "admin/types-input";
    }

    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        model.addAttribute("tag", tagService.getTag(id));
        return "admin/tags-input";
    }


    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag tt = tagService.getTagByName(tag.getName());
        if(tt != null) {
            result.rejectValue("name","nameExist","不能添加重复标签");
        }
        if(result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.saveTag(tag);
        if(t == null) {
            attributes.addFlashAttribute("message","操作失败");
        } else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tags/{id}")
    public String editPost(@Valid Tag tag,BindingResult result,@PathVariable Long id, RedirectAttributes attributes) {
        Tag tt = tagService.getTagByName(tag.getName());
        if(tt != null) {
            result.rejectValue("name","nameExist","不能添加重复分类");
        }
        if(result.hasErrors()) {
            return "admin/tags-input";
        }
        Tag t = tagService.updateTag(id, tag);
        if(t == null) {
            attributes.addFlashAttribute("message","更新失败");
        } else {
            attributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }


    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }

}
