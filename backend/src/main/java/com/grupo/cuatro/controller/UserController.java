package com.grupo.cuatro.controller;

import com.grupo.cuatro.model.User;
import com.grupo.cuatro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@Validated
@RequestMapping("/user")
@CrossOrigin("*")//temporal
public class UserController {
    @Autowired
    private UserRepository userRepository;

    //obtener todos los usuarios
    @RequestMapping(value="/all", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getAll() {
        return new ResponseEntity(userRepository.findAll(), HttpStatus.OK);
    }

    //obtener usuario por id
    @RequestMapping(value="/id/{id}", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getById(@PathVariable("id") Long id) {
        User userValue;
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            userValue = user.get();
            return new ResponseEntity(userValue, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    //crear usuario
    @RequestMapping(value="/create", method=RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity create(@RequestBody User resource) {
        return new ResponseEntity(userRepository.save(resource), HttpStatus.CREATED);
    }
}
