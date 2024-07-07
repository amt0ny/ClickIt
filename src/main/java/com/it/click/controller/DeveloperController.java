package com.it.click.controller;

import com.it.click.entities.TaskMaster;
import com.it.click.entities.UserMaster;
import com.it.click.service.IClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeveloperController {

    @Autowired
    private IClickService clickService;

    @PostMapping("/updateOwnProfile")
    public UserMaster updateOwnProfile(@RequestBody UserMaster userMasterData, @RequestHeader String token){
        return clickService.updateOwnProfile(userMasterData, token);
    }

}
