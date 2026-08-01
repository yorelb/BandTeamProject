package uk.ac.sheffield.team28.team28.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.sheffield.team28.team28.model.Music;
import uk.ac.sheffield.team28.team28.service.MusicService;


@RestController
@RequestMapping("/music")
public class MusicController {
  private final MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    @GetMapping
    public List<Map<String, String>> getAllMusic() {
        List<Music> musicList = musicService.getAllMusicWithItemDetails();
        return musicList.stream()
                .map(music -> {
                    Map<String, String> musicMap = new HashMap<>();
                    musicMap.put("id", music.getId().toString());
                    musicMap.put("title", music.getItem().getNameTypeOrTitle());
                    musicMap.put("composer", music.getItem().getMakeOrComposer());
                    return musicMap;
                })
                .toList();
    }  
}
