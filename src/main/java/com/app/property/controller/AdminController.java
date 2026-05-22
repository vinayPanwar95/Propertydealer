
package com.app.property.controller;

import com.app.property.model.PropertyDTO;
import com.app.property.model.UpdatePropertyRequest;
import com.app.property.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PropertyService propertyService;

//    private final PropertyMapper mapper;

    @GetMapping("")
    public String dashboard(Model m) {
        m.addAttribute("properties", propertyService.getAll());
        return "admin/dashboard";
    }

    @GetMapping("/add")
    public String addForm() {
        return "admin/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute PropertyDTO propertyDTO) throws IOException {
        propertyService.addProperty(propertyDTO);
        return "redirect:/admin";
    }



    @GetMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        propertyService.delete(id);
        return "redirect:/admin";
    }

    @PutMapping("/api/property/{id}")
    @ResponseBody
    public ResponseEntity<Void> updateProperty(@PathVariable String id,
                                               @RequestBody UpdatePropertyRequest req) {
        propertyService.updateProperty(id, req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/properties")
    public ResponseEntity<List<?>> getProperty() {
        return ResponseEntity.ok(propertyService.getAll());
    }
}
