package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaDbStorageTest {
    /*ПРИВЕТ. ВНИМАНИЕ!!!
    ВСЕ ТЕСТЫ КЛАССОВ ХРАНИЛИЩ БАЗЫ ДАННЫХ ЗАПУСКАЮТСЯ РАЗОМ
    ПУТЕМ ЗАПУСКА FilmorateApplicationTests КЛАССА. ПОЗВОЛИЛ СЕБЕ ВОЛЬНОСТЬ ВСЕ ОБЪЕДЕНИТЬ ДЛЯ УДОБСТВА.
     */

    private final MpaStorage mpaStorage;

    @Test
    public void getAllMpaTest() {
        List<Mpa> mpaList = mpaStorage.getAllMpa().stream()
                .sorted(Comparator.comparing(Mpa::getId))
                .collect(Collectors.toList());
        assertEquals(5, mpaList.size());
        assertEquals(1, mpaList.get(0).getId());
        assertEquals("G", mpaList.get(0).getName());
        assertEquals(2, mpaList.get(1).getId());
        assertEquals("PG", mpaList.get(1).getName());
        assertEquals(3, mpaList.get(2).getId());
        assertEquals("PG-13", mpaList.get(2).getName());
        assertEquals(4, mpaList.get(3).getId());
        assertEquals("R", mpaList.get(3).getName());
        assertEquals(5, mpaList.get(4).getId());
        assertEquals("NC-17", mpaList.get(4).getName());
    }

    @Test
    public void getMpaByIdTest() {
        assertEquals("NC-17", mpaStorage.getMpaById(5).getName());
    }

    @Test
    public void isMpaExistTest() {
        assertTrue(mpaStorage.isMpaExist(1));
        final NotFoundException exp = assertThrows(NotFoundException.class,
                () -> {
                    Boolean test = mpaStorage.isMpaExist(1234);
                }
        );
    }
}