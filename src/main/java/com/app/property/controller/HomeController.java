
package com.app.property.controller;

import com.app.property.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    PropertyService service;

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("properties", service.getAll());
        return "public/index";
    }

    @GetMapping("/properties")
    public String properties(Model m) {
        m.addAttribute("properties", service.getAll());
        return "public/properties";
    }

    @GetMapping("/properties/list")
    public String propertiesPage() {
        // This page will load properties via AJAX from /admin/properties
        return "public/Properties";
    }
}
