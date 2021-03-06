package pers.shayz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pers.shayz.bean.*;
import pers.shayz.service.*;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * @author ZhouXiaoyu
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClassifyService classifyService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderdetailsService orderdetailsService;

    @Autowired
    private OrderItemService orderItemService;

    @RequestMapping(value = "/toRegister")
    public String toRegister() {
        return "home/register";
    }

    @RequestMapping(value = "/doRegister", method = RequestMethod.POST)
    @ResponseBody
    public Msg doRegister(User user, ModelMap modelMap) {
        user.setUserid(null);
        System.out.println("/doRegister: " + user);

        modelMap.addAttribute("success", "注册成功，请登录！！！");

        String useremail = user.getUseremail();
        String username = user.getUsername();


        if (useremail.equals("") || username.equals("") || user.getUserpassword().equals("")) {
            return Msg.fail().add("msg", "用户名/邮箱/密码不能为空");
        }

        System.out.println("/doRegister: " + username);
        if (userService.getUserByName(username) != null) {
            return Msg.fail().add("msg", "用户名已存在");
        }

        System.out.println("/doRegister: " + useremail);
        if (userService.getUserByEmail(useremail) != null) {
            return Msg.fail().add("msg", "该邮箱已被注册");
        }

        userService.saveUser(user);
        return Msg.success();
    }

    @RequestMapping(value = "/toLogin")
    public String toLogin() {
        return "home/login";
    }

    @RequestMapping(value = "/Logout")
    public String Logout(HttpSession session) {
        session.invalidate();
        return "redirect:/toLogin";
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String doLogin(HttpSession session, String userlogin) {
        System.out.println("/doLogin: " + userlogin);
        User user;

        if (userlogin.contains("@")) {
            user = userService.getUserByEmail(userlogin);
        } else {
            user = userService.getUserByName(userlogin);
        }

        session.setAttribute("username", user.getUsername());
        session.setAttribute("userid", String.valueOf(user.getUserid()));
        session.setAttribute("userchaopoint", String.valueOf(user.getUserchaopoint()));
        session.setAttribute("image", user.getImage());
        session.setAttribute("phonenumber", user.getUserphone());
        session.setAttribute("useremail", user.getUseremail());
        session.setAttribute("useraddress", user.getAddress());

        return "redirect:/toHome";
    }

    @RequestMapping(value = "/validateLogin", method = RequestMethod.POST)
    @ResponseBody
    public Msg validateLogin(@RequestParam("userlogin") String userlogin,
                             @RequestParam("userpassword") String userpassword) {
        if (userlogin.equals("") || userpassword.equals("")) {
            return Msg.fail().add("msg", "用户名/密码不为空！！！");
        }

        System.out.println("/validateLogin: " + userlogin);
        User user;

        if (userlogin.contains("@")) {
            user = userService.getUserByEmail(userlogin);
        } else {
            user = userService.getUserByName(userlogin);
        }

        if (user == null) {
            return Msg.fail().add("msg", "用户不存在！！！");
        }

        if (user.getUserpassword().equals(userpassword)) {
            return Msg.success();
        } else {
            return Msg.fail().add("msg", "用户名密码不匹配！！！");
        }
    }

//    @RequestMapping(value = "/checkUsername", method = RequestMethod.POST)
//    @ResponseBody
//    public Msg checkUsername(@RequestParam("username") String username) {
//        System.out.println("/checkUsername: " + username);
//        if(userService.getUserByName(username)==null){
//            return Msg.success();
//        }else{
//        return Msg.fail();
//        }
//    }
//
//    @RequestMapping(value = "/checkUseremail", method = RequestMethod.POST)
//    @ResponseBody
//    public Msg checkUseremail(@RequestParam("useremail") String useremail) {
//        System.out.println("/checkUseremail: " + useremail);
//        if(userService.getUserByEmail(useremail)==null){
//            return Msg.success();
//        }else{
//            return Msg.fail();
//        }
//    }

    @RequestMapping(value = "/toHome")
    public String toHome(ModelMap modelMap) {
        modelMap.addAttribute("book", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(1)));
        modelMap.addAttribute("household", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(2)));
        modelMap.addAttribute("snack", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(3)));
        modelMap.addAttribute("phone", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(4)));
        modelMap.addAttribute("sport", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(5)));
        modelMap.addAttribute("beauty", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(6)));
        modelMap.addAttribute("computer", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(7)));
        modelMap.addAttribute("clothes", new ArrayList<Goods>(goodsService.getGoodsByClassifyId(8)));

        Random random = new Random();
        modelMap.addAttribute("one", goodsService.getGoodsByRandomId(random.nextInt()));
        modelMap.addAttribute("two", goodsService.getGoodsByRandomId(random.nextInt()));
        modelMap.addAttribute("three", goodsService.getGoodsByRandomId(random.nextInt()));
        return "home/home";
    }

//    @RequestMapping(value = "/toPublish")
//    public String toPublish(ModelMap modelMap) {
//        List<Classify> classifyList = classifyService.getAllClassify();
//        System.out.println("/toPublish: " + classifyList);
//        modelMap.addAttribute("classifyList", classifyList);
//        return "home/publish";
//    }

    @RequestMapping(value = "/toUserInfo")
    public String toUserInfo() {
        return "person/userinfo";
    }

    @RequestMapping(value = "/toAddress")
    public String toAddress() {
        return "person/address";
    }

    @RequestMapping(value = "/toBill")
    public String toBill() {
        return "person/bill";
    }

    @RequestMapping(value = "/toBillList")
    public String toBillList() {
        return "person/billlist";
    }

    @RequestMapping(value = "/toOrderDetails/{orderitemid}")
    public String toOrderDetails(@PathVariable("orderitemid") String id, ModelMap modelMap) {
        int orderItemId = Integer.parseInt(id);
        System.out.println("/toOrderDetails/{orderitemid}: " + orderItemId);

        List<Goods> goodsList = orderdetailsService.getGoodsByOrderItemId(orderItemId);
        System.out.println("/toOrderDetails/{orderitemid}: " + goodsList);
        modelMap.addAttribute("GoodsList", goodsList);

        Orderitem orderitem = orderItemService.getOrderItemByOrderitemId(orderItemId);
        System.out.println("/toOrderDetails/{orderitemid}: " + orderitem);
        modelMap.addAttribute("OrderItemList", orderitem);

        List<Orderdetails> orderdetailsList = orderdetailsService.getOrderDetailsByOrderItemId(orderItemId);
        System.out.println("/toOrderDetails/{orderitemid}: " + orderdetailsList);
        modelMap.addAttribute("OrderDetailList", orderdetailsList);

        return "person/orderdetails";
    }

    @RequestMapping(value = "/toPassword")
    public String toPassword() {
        return "person/password";
    }

    @RequestMapping(value = "/userInfo/image", method = RequestMethod.POST)
    @ResponseBody
    public Msg changeUserImage(MultipartFile userImage, HttpSession session) throws IOException {

        System.out.println("/doPublish imageFile: " + userImage);

        User user = new User();
        user.setUserid(Integer.parseInt(String.valueOf(session.getAttribute("userid"))));

        System.out.println("comming!");
        String path = "D:\\JetBrains\\SimpleC\\src\\main\\webapp\\GoodsImage";
        System.out.println("path>>" + path);

        String fileName = userImage.getOriginalFilename();
        System.out.println(fileName);
        String[] suffix = fileName.split("\\.");
        System.out.println(Arrays.toString(suffix) + " " + suffix[suffix.length - 1]);

        if (!Objects.equals(suffix[suffix.length - 1], "")) {

            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName = uuid + "." + suffix[suffix.length - 1];
            String image = "GoodsImage/" + fileName;
            System.out.println("fileName>>" + fileName);

            File dir = new File(path, fileName);

            userImage.transferTo(dir);

            user.setImage(image);
        } else {
            user.setImage(null);
        }

        System.out.println("/doPublish: " + user);

        userService.updateUser(user);

        return Msg.success().add("msg", "头像更新成功");

    }
}
