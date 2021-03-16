package com.wyx.crm.settings.domain;

public class User {
    private String id;
    private String oginAct;  //登录账号
    private String name ;
    private String loginPwd;
    private String email ;
    private String expireTime;  //失效时间，19位 格式为：yyyy-MM-dd HH:mm:ss
    private String lockState;  //锁定状态 0表示锁定，1表示启用
    private String deptno;
    private String allowIps;  //允许访问的ip地址，即使账号密码正确，ip地址不允许，登陆失败
    private String createTime;  //创建时间  19位
    private String createBy; //创建人
    private String editTime;  //修改时间  19位
    private String editBy;  //修改人

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOginAct() {
        return oginAct;
    }

    public void setOginAct(String oginAct) {
        this.oginAct = oginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}
