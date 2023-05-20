package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Suite
@SelectClasses({UserDbStorageTest.class, FilmDbStorageTest.class, GenreDbStorageTest.class, MpaDbStorageTest.class})
class FilmorateApplicationTests {
    //ВНЕДРИЛ junit-platform-suite-engine ДЛЯ УДОБСТВА ЗАПУСКА


}
