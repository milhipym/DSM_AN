package com.dongbu.dsm.webview.agentweb;

/**
 * <b>@项目名：</b> agentweb<br>
 * <b>@包名：</b>com.dongbu.dsm.webview.agentweb<br>
 * <b>@创建者：</b> cxz --  just<br>
 * <b>@创建时间：</b> &{DATE}<br>
 * <b>@公司：</b> <br>
 * <b>@邮箱：</b> cenxiaozhong.qqcom@qq.com<br>
 * <b>@描述</b><br>
 */

public class HookManager {


    public static AgentWeb hookAgentWeb(AgentWeb agentWeb, AgentWeb.AgentBuilder agentBuilder) {
        return agentWeb;
    }

    public static AgentWeb hookAgentWeb(AgentWeb agentWeb, AgentWeb.AgentBuilderFragment agentBuilder) {
        return agentWeb;
    }

    public static boolean permissionHook(String url,String[]permissions){
        return true;
    }


}
