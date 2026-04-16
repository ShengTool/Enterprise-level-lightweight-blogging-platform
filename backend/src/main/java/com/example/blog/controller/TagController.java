package com.example.blog.controller;

import com.example.blog.model.Tag;
import com.example.blog.model.User;
import com.example.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping
    public ResponseEntity<?> getTags() {
        List<Tag> tags = tagService.getAll();
        return ResponseEntity.ok(tags);
    }

    @PostMapping
    public ResponseEntity<?> createTag(@RequestAttribute("user") User user, @RequestBody Tag tag) {
        if (user.isAdmin()) {
            Tag createdTag = tagService.create(tag);
            return ResponseEntity.ok(createdTag);
        }
        return ResponseEntity.status(403).body("Forbidden");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTag(@RequestAttribute("user") User user, @PathVariable Integer id, @RequestBody Tag tag) {
        if (user.isAdmin()) {
            tag.setId(id);
            Tag updatedTag = tagService.update(tag);
            return ResponseEntity.ok(updatedTag);
        }
        return ResponseEntity.status(403).body("Forbidden");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTag(@RequestAttribute("user") User user, @PathVariable Integer id) {
        if (user.isAdmin()) {
            tagService.delete(id);
            return ResponseEntity.ok("Tag deleted");
        }
        return ResponseEntity.status(403).body("Forbidden");
    }
}