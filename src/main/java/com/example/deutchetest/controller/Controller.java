package com.example.deutchetest.controller;

import com.example.deutchetest.entity.Article;
import com.example.deutchetest.entity.Draft;
import com.example.deutchetest.entity.History;
import com.example.deutchetest.entity.User;
import com.example.deutchetest.service.ArticleService;
import com.example.deutchetest.service.UserService;
import com.example.deutchetest.userDetail.MyUserDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Controller
public class Controller {



    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;



    @RequestMapping(value = "/postsignup",method = RequestMethod.POST)
    public ModelAndView addUser(@Valid @ModelAttribute User user){
        User newUser = userService.CreateUser(user);
        if(newUser==null){
            ModelAndView modelAndView=new ModelAndView("redirect:getsignup");
            modelAndView.addObject("UserAlreadyExists","Already Exists!");
            return modelAndView;
        }
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/getsignup",method= RequestMethod.GET)
    public String SignUpForm(Model model){
        User user= new User();
        model.addAttribute("myUser",user);
        return "signup";
    }

    @GetMapping("/login")
    public String Login(){
        return "login";
    }

    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public String homePage(Model model){
        ObjectMapper objectMapper =  new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        model.addAttribute("user",email);
        model.addAttribute("role",role);
        model.addAttribute("draft",new Draft());
        model.addAttribute("listDraft",articleService.findAllDraft());
        model.addAttribute("allDraft",articleService.allDraft());
        model.addAttribute("article",articleService.findAllArticle());
        model.addAttribute("adminArticle",articleService.adminArticle());
        model.addAttribute("userArticle",articleService.allArticle(null,null));

        return "dashboard";
    }


    @RequestMapping(value = "/sortAuthor",method = RequestMethod.GET)
    public String homePageSort(Model model){
        ObjectMapper objectMapper =  new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        model.addAttribute("user",email);
        model.addAttribute("role",role);
        model.addAttribute("draft",new Draft());
        model.addAttribute("listDraft",articleService.findAllDraft());
        model.addAttribute("allDraft",articleService.allDraft());
        model.addAttribute("article",articleService.findAllArticle());
        model.addAttribute("adminArticle",articleService.adminArticle());
        model.addAttribute("userArticle",articleService.allArticle("ASC","author"));

        return "dashboard";
    }
    @RequestMapping(value = "/sortAuthor1",method = RequestMethod.GET)
    public String homePageSort1(Model model){
        ObjectMapper objectMapper =  new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        model.addAttribute("user",email);
        model.addAttribute("role",role);
        model.addAttribute("draft",new Draft());
        model.addAttribute("listDraft",articleService.findAllDraft());
        model.addAttribute("allDraft",articleService.allDraft());
        model.addAttribute("article",articleService.findAllArticle());
        model.addAttribute("adminArticle",articleService.adminArticle());
        model.addAttribute("userArticle",articleService.allArticle("ASC","publishedDate"));

        return "dashboard";
    }


    @RequestMapping(value = "/saveAdminDraft",method = RequestMethod.POST)
    public ModelAndView addArticleInDraft(@Valid @ModelAttribute Draft draft){
       Draft draft1 = articleService.saveDraftArticle(draft);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/getDraftAdmin/edit",method = RequestMethod.GET)
    public String editArticleInDraft(@RequestParam(value="id") int id, Model model){
        ObjectMapper objectMapper =  new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        Draft existDraft = articleService.findDraft(id);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        model.addAttribute("user",email);
        model.addAttribute("draft",existDraft);
        model.addAttribute("blog",articleService.findDraft(id));
        model.addAttribute("role",role);
        model.addAttribute("editArticle","No");
        return "edit";
    }

    @RequestMapping(value = "/draftAdmin/edit",method = RequestMethod.POST)
    public ModelAndView editArticleInDraft(@Valid @ModelAttribute Draft draft){
        Draft draft1 = articleService.editDraft(draft.getId(), draft);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/getarticle/edit",method = RequestMethod.GET)
    public String editArticle(@RequestParam(value="id") int id, Model model){
        ObjectMapper objectMapper =  new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        Article existArticle = articleService.findArticle(id);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        model.addAttribute("user",email);
        model.addAttribute("role",role);
        model.addAttribute("existArticle",existArticle);
        model.addAttribute("article",articleService.findArticle(id));
        model.addAttribute("editArticle","yes");
        return "edit";
    }

    @RequestMapping(value = "/article/edit",method = RequestMethod.POST)
    public ModelAndView editArticle(@Valid @ModelAttribute Article article){
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User myUserDetail = objectMapper.convertValue(authentication.getPrincipal(),User.class);
        String email = myUserDetail.getEmail();
        String role = myUserDetail.getRole();
        if(role.equals("superAdmin")) {
            Article article1 = articleService.updateArticle(article.getId(), article);
        }
        else {
            Draft draft = new Draft();
            draft.setArticle(articleService.findArticle(article.getId()));
            draft.setBlog(article.getBlog());
            articleService.saveDraftArticle(draft);
        }
        return new ModelAndView("redirect:/home");
    }



    @RequestMapping(value = "/draftAdmin/delete",method = RequestMethod.GET)
    public ModelAndView deleteDraft(@RequestParam(value = "id")int id){
        articleService.deleteDraft(id);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/article/delete",method = RequestMethod.GET)
    public ModelAndView deleteArticle(@RequestParam(value = "id")int id){
        articleService.deleteArticle(id);
        return new ModelAndView("redirect:/home");
    }

    @RequestMapping(value = "/publish",method = RequestMethod.GET)
    public ModelAndView publishArticle(@RequestParam(value = "id")int id){
        Draft draft = articleService.findDraft(id);
        if (draft.getArticle() == null) {
            articleService.saveSuperAdminArticle(draft, draft.getUser().getEmail());
        }
        else {
            Article article = draft.getArticle();
            article.setBlog(draft.getBlog());
            articleService.updateArticle(article.getId(), article);
        }
        articleService.deleteDraft(id);
        return new ModelAndView("redirect:/home");
    }

    @GetMapping("/viewhistory")
    public ModelAndView viewHistory(Model model){
        model.addAttribute("history",articleService.findhistory());
        return new ModelAndView("history");
    }

}
