package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> getAllMpa() {
        log.info("Get all mpa service method");
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        log.info("Get Mpa by id service method");
        return mpaStorage.getMpaById(id);
    }

}
