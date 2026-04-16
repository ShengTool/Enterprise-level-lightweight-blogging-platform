package com.example.blog.service;

import com.example.blog.model.Tag;
import com.example.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Tag getById(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    public Tag create(Tag tag) {
        return tagRepository.save(tag);
    }

    public Tag update(Tag tag) {
        return tagRepository.save(tag);
    }

    public void delete(Integer id) {
        tagRepository.deleteById(id);
    }

    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
}