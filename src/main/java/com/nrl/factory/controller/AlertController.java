package com.nrl.factory.controller;

import com.nrl.factory.service.AlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class AlertController {

    @Autowired
    private AlertService alarmService;

    @PostMapping(value = "/create")
    public ResponseEntity<String> createAlarm(@RequestBody String message){
      return alarmService.sendAlarm(message);
    }
}
