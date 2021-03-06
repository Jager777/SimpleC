<%--
  Created by IntelliJ IDEA.
  User: ZhouXiaoyu
  Date: 2018/6/27
  Time: 10:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<div class="am-container header">
    <ul class="message-l">
        <div class="topMessage">
            <div class="menu-hd">
                <a href="${APP_PATH}/toHome" target="_top" class="h">Hi,${sessionScope.username}</a> |
                <a href="${APP_PATH}/Logout" target="_top" class="h">退出账号</a>
            </div>
        </div>
    </ul>
    <ul class="message-r">
        <div class="topMessage home">
            <div class="menu-hd"><a href="${APP_PATH}/toHome" target="_top" class="h">商城首页</a></div>
        </div>
        <div class="topMessage my-shangcheng">
            <div class="menu-hd MyShangcheng"><a href="${APP_PATH}/toUserInfo" target="_top"><i class="am-icon-user am-icon-fw"></i>个人中心</a>
            </div>
        </div>
        <div class="topMessage mini-cart">
            <li class="am-dropdown" data-am-dropdown>
                <a class="am-dropdown-toggle" data-am-dropdown-toggle href="javascript:;">
                    购物车 <span class="am-icon-caret-down"></span>
                </a>
                <ul class="am-dropdown-content" style="white-space: nowrap">
                    <li class="am-dropdown-header" >${sessionScope.username}的购物车</li>
                    <li class="am-divider"></li>
                    <li class="am-active"><a href="${APP_PATH}/toShopcart">前往购物车</a></li>
                    <li class="am-divider"></li>
                    <li><a href="#">网址不变且唯一</a></li>
                    <li><a href="#">内容实时同步更新</a></li>
                    <li><a href="#">云端跨平台适配</a></li>
                    <li><a href="#">专属的一键拨叫</a></li>
                </ul>
            </li>
        </div>
    </ul>
</div>
<!--悬浮搜索框-->
<div class="nav white">
    <div class="logoBig">
        <li>
            <a href="${APP_PATH}/toHome">
                <img src="${APP_PATH}/static/picture/logoPro.png" style="height: 90px;width: 150px"/></a>
        </li>
    </div>
    <div class="search-bar pr">
        <a name="index_none_header_sysc" href="#"></a>
        <form action="${APP_PATH}/Search" method="post">
            <input id="searchInput" name="search" type="text" placeholder="搜索" autocomplete="off">
            <input id="ai-topsearch" class="submit am-btn" value="搜索" type="submit">
        </form>

    </div>
</div>
