package com.glmapper.controller;

import com.glmapper.controller.httpclient.HttpClientInstance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * HelloController
 * @version 1.0
 * copy from guolei.sgl
 **/
@Controller
public class HelloController {
    @RequestMapping("/hello")
    public ModelAndView hello() throws Exception {
        HttpClientInstance httpClientInstance = new HttpClientInstance(10 * 1000);
        String httpGetUrl = "http://localhost:8089/tracer_jaeger_plugin_demo_war_exploded/httpclient";
        httpClientInstance.executeGet(httpGetUrl);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        return mv;
    }
    @RequestMapping("/httpclient")
    public ModelAndView httpclient() throws Exception {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        return mv;
    }


}
