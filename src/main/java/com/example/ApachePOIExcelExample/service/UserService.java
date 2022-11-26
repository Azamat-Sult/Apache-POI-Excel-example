package com.example.ApachePOIExcelExample.service;

import com.example.ApachePOIExcelExample.entity.UserEntity;
import com.example.ApachePOIExcelExample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserEntity> findUsers(String search) {
        List<UserEntity> result;
        if (search == null) {
            result = userRepository.findAll();
        } else {
            result = userRepository.findByEmailAndFullNameContainingIgnoreCase(search);
        }
        return result;
    }

}