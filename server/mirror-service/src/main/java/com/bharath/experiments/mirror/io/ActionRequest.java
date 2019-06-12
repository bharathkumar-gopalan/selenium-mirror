package com.bharath.experiments.mirror.io;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ActionRequest {
    private ActionContext actionContext;
    private String pageUrl;
    private List<LocationStrategy> locationStrategyList;

    public List<LocationStrategy> getLocationStrategyList() {
        return locationStrategyList;
    }

    public void setLocationStrategyList(List<LocationStrategy> locationStrategyList) {
        this.locationStrategyList = locationStrategyList;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public ActionContext getActionContext() {
        return actionContext;
    }

    public void setActionContext(ActionContext actionContext) {
        this.actionContext = actionContext;
    }

    public static void main(String args[]){
        LocationStrategy str = new LocationStrategy();
        str.setLocator("q");
        str.setMethod("name");
        LocationStrategy str1 = new LocationStrategy();
        str1.setMethod("xpath");
        str1.setLocator("//input[@name='q']");
        List<LocationStrategy> list = Arrays.asList(new LocationStrategy[] {str, str1});
        ActionContext context = new ActionContext();
        context.setAction(ActionContext.Action.TYPE);
        context.setValue("Hello world");


        ActionRequest ar = new ActionRequest();
        ar.setActionContext(context);
        ar.setLocationStrategyList(list);
        ar.setPageUrl("https://www.google.com");

        Gson gson = new Gson();
        System.out.println(gson.toJson(ar));



    }
}
