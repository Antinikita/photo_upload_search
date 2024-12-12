package org.example.photo_upload_and_search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/photo")
public class PhotoController {
    @Value("${upload.dir}")
    private String uploadDir;
    @Autowired
    PhotoRepository photoRepository;

    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }
    @GetMapping
    public String photos(Model model) {
        model.addAttribute("photos", photoRepository.findAll());
        return "photo-list";
    }
    @GetMapping("/upload")
    public String uploadForm() {
        return "photo-upload";
    }
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,@RequestParam("desc") String description) throws IOException {
        if (!file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Path path= Paths.get(uploadDir,fileName);
            file.transferTo(path);

            Photo photo = new Photo(fileName,description);
            photoRepository.save(photo);
        }
        return "redirect:/photo";
    }
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Photo> photos = photoRepository.findByTitleContainingIgnoreCase(query);
        model.addAttribute("photos", photos);
        return "photo-list";

    }
}
