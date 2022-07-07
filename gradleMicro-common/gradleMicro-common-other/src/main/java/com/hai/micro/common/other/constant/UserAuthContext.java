package com.hai.micro.common.other.constant;

import com.hai.micro.common.other.vo.MachineAuthVO;
import com.hai.micro.common.other.vo.OpenAuthVO;
import com.hai.micro.common.other.vo.UserAuthVO;
import com.hai.micro.common.other.vo.WapAuthVO;

/**
 * @ClassName UserAuthContext
 * @Description 用户授权信息容器
 * @Author ZXH
 * @Date 2022/5/17 16:22
 * @Version 1.0
 **/
public class UserAuthContext {

    /**
     * 管理端具体的用户授权信息
     */
    private static ThreadLocal<UserAuthVO> userAuth = new ThreadLocal<>();

    /**
     * 门锁设备授权信息
     */
    public static ThreadLocal<MachineAuthVO> machineAuth = new ThreadLocal();

    /**
     * 开发平台授权信息
     */
    public static ThreadLocal<OpenAuthVO> openAuth = new ThreadLocal();

    /**
     * 网页应用授权信息
     */
    public static ThreadLocal<WapAuthVO> wapAuth = new ThreadLocal();

    /**
     * 获取管理端用户授权信息
     *
     * @return
     */
    public static UserAuthVO getUserAuth() {
        return userAuth.get();
    }

    /**
     * 对外开放平台相关具体信息
     *
     * @return
     */
    public static OpenAuthVO getOpenAuthVO() {
        return openAuth.get();
    }

    /**
     * 设被授权具体信息
     *
     * @return
     */
    public static MachineAuthVO getMachineAuthVO() {
        return machineAuth.get();
    }

    /**
     * 网页应用授权信息
     *
     * @return
     */
    public static WapAuthVO getWapAuthVO() {
        return wapAuth.get();
    }

    /**
     * 保存管理端用户授权信息
     *
     * @param userAuthVO
     */
    public static void setUserAuth(UserAuthVO userAuthVO) {
        userAuth.set(userAuthVO);
    }

    /**
     * 释放ThreadLocal空间
     */
    public static void remove() {
        userAuth.remove();
    }
}
